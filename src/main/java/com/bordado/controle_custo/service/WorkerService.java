package com.bordado.controle_custo.service;

import com.bordado.controle_custo.entity.*;
import com.bordado.controle_custo.exceptions.ResourceNotFoundException;
import com.bordado.controle_custo.repository.LaborTypesRepository;
import com.bordado.controle_custo.repository.ProfitMarginRepository;
import com.bordado.controle_custo.repository.RawMaterialRepository;
import com.bordado.controle_custo.repository.WorkerRepository;
import com.bordado.controle_custo.repository.projection.WorkerProjection;
import com.bordado.controle_custo.web.dto.mapper.WorkerMapper;
import com.bordado.controle_custo.web.dto.worker.WorkerCreateDTO;
import com.bordado.controle_custo.web.dto.worker.WorkerUpdateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class WorkerService {
    private final WorkerRepository workerRepository;
    private final RawMaterialRepository rawMaterialRepository;
    private final LaborTypesRepository laborTypesRepository;
    private final ProfitMarginRepository profitMarginRepository;

    @Transactional
    public Worker save(WorkerCreateDTO workerCreateDTO) {
        Long idLaborTypes = workerCreateDTO.getLaborTypeId();
        LaborTypes laborTypes = laborTypesRepository.findById(idLaborTypes)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Tipo de mão de obra não encontrada.")
                );

        Long idProfitMargin = workerCreateDTO.getProfitMarginId();
        ProfitMargin profitMargin = profitMarginRepository.findById(idProfitMargin)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Tipo de margem de lucro não encontrado")
                );

        List<Long> rawMaterials = workerCreateDTO.getRawMaterialIds();
        List<RawMaterial> rawMaterialsValid = new ArrayList<>();
        for (Long rawMaterialIds : rawMaterials) {
            RawMaterial rawMaterialExists = rawMaterialRepository.findById(rawMaterialIds).orElseThrow(() ->
                    new ResourceNotFoundException("Tipo de matéria-prima não existe")
            );

            rawMaterialsValid.add(rawMaterialExists);
            rawMaterialExists.setAmount(rawMaterialExists.getAmount() - 1);
        }

        Worker worker = WorkerMapper.toWorker(workerCreateDTO, rawMaterialsValid, laborTypes, profitMargin);

        return workerRepository.save(worker);

    }

    public Page<WorkerProjection> findAll(Pageable pageable) {
        return workerRepository.findAllPage(pageable);
    }

    public Worker update(Long id, WorkerUpdateDTO workerUpdate) {
        Worker worker = workerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fornecedor com ID " + id + " não encontrado."));
        worker.setId(id);

        Long idLaborType = workerUpdate.getLaborTypeId();
        LaborTypes laborTypes = laborTypesRepository.findById(idLaborType)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de mão de obra não encontrada."));

        Long idProfitMargin = workerUpdate.getProfitMarginId();
        ProfitMargin profitMargin = profitMarginRepository.findById(idProfitMargin)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de margem de lucro não encontrado"));

        List<Long> rawMaterials = workerUpdate.getRawMaterialIds();
        List<RawMaterial> rawMaterialsValid = new ArrayList<>();
        List<RawMaterial> rawMaterialListExistsInWorker = worker.getRawMaterial();

        Set<Long> existingRawMaterialIds = rawMaterialListExistsInWorker.stream()
                .map(RawMaterial::getId)
                .collect(Collectors.toSet());

        Set<Long> newRawMaterialIds = new HashSet<>(rawMaterials);

        for (RawMaterial existingRawMaterial : rawMaterialListExistsInWorker) {
            if (!newRawMaterialIds.contains(existingRawMaterial.getId())) {

                existingRawMaterial.setAmount(existingRawMaterial.getAmount() + 1);
            }
        }

        for (Long rawMaterialId : rawMaterials) {
            RawMaterial rawMaterial = rawMaterialRepository.findById(rawMaterialId)
                    .orElseThrow(() -> new ResourceNotFoundException("Tipo de matéria-prima não existe"));

            if (!existingRawMaterialIds.contains(rawMaterialId)) {
                rawMaterial.setAmount(rawMaterial.getAmount() - 1);
            }

            rawMaterialsValid.add(rawMaterial);
        }

        worker.setLaborType(laborTypes);
        worker.setProfitMargin(profitMargin);
        worker.setRawMaterial(rawMaterialsValid);

        Optional.ofNullable(workerUpdate.getSplitTime()).ifPresent(worker::setSplitTime);

        Optional.ofNullable(workerUpdate.getStatus()).ifPresent(worker::setStatus);

        Worker workerToDto = WorkerMapper.toWorkerUpdate(workerUpdate, rawMaterialsValid, laborTypes, profitMargin);

        return workerRepository.save(workerToDto);
    }
}
