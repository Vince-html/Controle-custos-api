package com.bordado.controle_custo.service;


import com.bordado.controle_custo.entity.Supplier;
import com.bordado.controle_custo.exceptions.ResourceNotFoundException;
import com.bordado.controle_custo.exceptions.UniqueNameViolationException;
import com.bordado.controle_custo.repository.SupplierRepository;
import com.bordado.controle_custo.repository.projection.SupplierProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SupplierService {

    private final SupplierRepository supplierRepository;


    @Transactional
    public void salvar (Supplier supplier) {
        try {
            supplierRepository.save(supplier);
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            throw new UniqueNameViolationException("Fornecedor já cadastrado.");
        }
    }

    @Transactional(readOnly = true)
    public Page<SupplierProjection> findAll(Pageable pageable) {
        return supplierRepository.findAllPage(pageable);
    }
    @Transactional
    public void deleteById(Long id) {
        if (!supplierRepository.existsById(id)) {
            throw new ResourceNotFoundException("Fornecedor com ID " + id + " não encontrado.");
        }
        supplierRepository.deleteById(id);
    }

    @Transactional
    public Supplier updateSupplier(Long id, Supplier updateDTO) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fornecedor com ID " + id + " não encontrado."));

        Optional.ofNullable(updateDTO.getName()).ifPresent(supplier::setName);
        Optional.ofNullable(updateDTO.getContact()).ifPresent(supplier::setContact);

        return supplierRepository.save(supplier);

    }
}
