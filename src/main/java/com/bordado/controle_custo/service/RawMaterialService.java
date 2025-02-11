package com.bordado.controle_custo.service;


import com.bordado.controle_custo.entity.RawMaterial;
import com.bordado.controle_custo.entity.Supplier;
import com.bordado.controle_custo.exceptions.ResourceNotFoundException;
import com.bordado.controle_custo.exceptions.SupplierNotFoundException;
import com.bordado.controle_custo.repository.RawMaterialRepository;
import com.bordado.controle_custo.repository.SupplierRepository;
import com.bordado.controle_custo.repository.projection.RawMaterialProjection;
import com.bordado.controle_custo.repository.projection.SupplierProjection;
import com.bordado.controle_custo.web.dto.mapper.RawMaterialMapper;
import com.bordado.controle_custo.web.dto.rawmaterial.RawMaterialCreateDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class RawMaterialService {
    private final SupplierRepository supplierRepository;

    private final RawMaterialRepository rawMaterialRepository;


    @Transactional
    public RawMaterial save(RawMaterialCreateDTO createDTO) {
        Supplier supplier = supplierRepository.findById(createDTO.getIdSupplier())
                .orElseThrow(() -> new SupplierNotFoundException(createDTO.getIdSupplier())); // 🚨 Garante que o supplier existe

        RawMaterial rawMaterial = RawMaterialMapper.toRawMaterial(createDTO, supplier);
        return rawMaterialRepository.save(rawMaterial);
    }
    @Transactional
    public Page<RawMaterialProjection> findAll(Pageable pageable) {
        return rawMaterialRepository.findAllPage(pageable);
    }
    @Transactional
    public RawMaterial updateRawMaterial(Long id, RawMaterial updated) {
        RawMaterial rawMaterial = rawMaterialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Matéria-prima com ID " + id + " não encontrada."));

        Supplier supplier = supplierRepository.findById(updated.getSupplier().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Fornecedor com ID " + updated.getSupplier().getId() + " não encontrado."));

        rawMaterial.setSupplier(supplier);

        Optional.ofNullable(updated.getName()).ifPresent(rawMaterial::setName);
        Optional.ofNullable(updated.getCost()).ifPresent(rawMaterial::setCost);
        Optional.ofNullable(updated.getDescription()).ifPresent(rawMaterial::setDescription);
        Optional.ofNullable(updated.getAmount()).ifPresent(rawMaterial::setAmount);

        return rawMaterialRepository.save(rawMaterial);
    }
}
