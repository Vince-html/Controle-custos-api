package com.bordado.controle_custo.repository;

import com.bordado.controle_custo.entity.RawMaterial;
import com.bordado.controle_custo.repository.projection.RawMaterialProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RawMaterialRepository extends JpaRepository<RawMaterial, Long> {
    @Query("select r from RawMaterial r")
    Page<RawMaterialProjection> findAllPage(Pageable pageable);
}
