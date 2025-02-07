package com.bordado.controle_custo.repository;

import com.bordado.controle_custo.entity.Supplier;
import com.bordado.controle_custo.repository.projection.SupplierProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    @Query("select s from Supplier s")
    Page<SupplierProjection> findAllPage(Pageable pageable);
}
