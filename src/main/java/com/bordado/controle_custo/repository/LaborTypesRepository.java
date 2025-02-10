package com.bordado.controle_custo.repository;

import com.bordado.controle_custo.entity.LaborTypes;
import com.bordado.controle_custo.repository.projection.LaborTypesProjection;
import com.bordado.controle_custo.repository.projection.RawMaterialProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LaborTypesRepository extends JpaRepository<LaborTypes, Long> {
    @Query("select l from LaborTypes l")
    Page<LaborTypesProjection> findAllPage(Pageable pageable);
}
