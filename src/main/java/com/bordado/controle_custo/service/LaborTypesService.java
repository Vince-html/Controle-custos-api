package com.bordado.controle_custo.service;

import com.bordado.controle_custo.entity.LaborTypes;
import com.bordado.controle_custo.exceptions.ResourceNotFoundException;
import com.bordado.controle_custo.exceptions.UniqueNameViolationException;
import com.bordado.controle_custo.repository.LaborTypesRepository;
import com.bordado.controle_custo.repository.projection.LaborTypesProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class LaborTypesService {
    private final LaborTypesRepository laborTypesRepository;

    @Transactional
    public LaborTypes salvar (LaborTypes createDTO) {
        try {
           return laborTypesRepository.save(createDTO);
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            throw new UniqueNameViolationException("Tipo de mão de obra já cadastrado.");
        }
    }

    @Transactional
    public Page<LaborTypesProjection> findAll(Pageable pageable) {
        return laborTypesRepository.findAllPage(pageable);
    }

    @Transactional
    public LaborTypes update(Long id, LaborTypes laborUpdated) {
        LaborTypes laborTypes = laborTypesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de mão de obra com ID " + id + " não encontrado."));
        Optional.ofNullable(laborUpdated.getTypeLabor()).ifPresent(laborTypes::setTypeLabor);
        Optional.ofNullable(laborUpdated.getCost()).ifPresent(laborTypes::setCost);

        return laborTypesRepository.save(laborTypes);
    }

    @Transactional
    public void deleteById(Long id) {

        if (!laborTypesRepository.existsById(id)) {
            throw new ResourceNotFoundException("Tipo de mão de obra com ID " + id + " não encontrado.");
        }
        laborTypesRepository.deleteById(id);

    }
}
