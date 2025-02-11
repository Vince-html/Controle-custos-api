package com.bordado.controle_custo.repository;


import com.bordado.controle_custo.entity.ProfitMargin;
import com.bordado.controle_custo.repository.projection.ProfitMarginProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProfitMarginRepository extends JpaRepository<ProfitMargin, Long> {
    @Query("select p from ProfitMargin p")
    Page<ProfitMarginProjection> findAllPage(Pageable pageable);
}
