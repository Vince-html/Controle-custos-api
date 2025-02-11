package com.bordado.controle_custo.service;

import com.bordado.controle_custo.entity.ProfitMargin;
import com.bordado.controle_custo.exceptions.ResourceNotFoundException;
import com.bordado.controle_custo.repository.ProfitMarginRepository;
import com.bordado.controle_custo.repository.projection.ProfitMarginProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProfitMarginService {
    private final ProfitMarginRepository profitMarginRepository;

    @Transactional
    public ProfitMargin salvar (ProfitMargin createDTO) {
        return profitMarginRepository.save(createDTO);
    }

    @Transactional
    public Page<ProfitMarginProjection> findAll(Pageable pageable) {
        return profitMarginRepository.findAllPage(pageable);
    }

    @Transactional
    public ProfitMargin update(Long id, ProfitMargin laborUpdated) {
        ProfitMargin laborTypes = profitMarginRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de margem de lucro com ID " + id + " não encontrado."));
        Optional.ofNullable(laborUpdated.getValue()).ifPresent(laborTypes::setValue);
        Optional.ofNullable(laborUpdated.getDescription()).ifPresent(laborTypes::setDescription);

        return profitMarginRepository.save(laborTypes);
    }

    @Transactional
    public void deleteById(Long id) {

        if (!profitMarginRepository.existsById(id)) {
            throw new ResourceNotFoundException("Tipo de margem de lucro com ID " + id + " não encontrado.");
        }
        profitMarginRepository.deleteById(id);

    }
}
