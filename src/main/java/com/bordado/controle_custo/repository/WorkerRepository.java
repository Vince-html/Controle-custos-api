package com.bordado.controle_custo.repository;


import com.bordado.controle_custo.entity.Worker;
import com.bordado.controle_custo.repository.projection.WorkerProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WorkerRepository extends JpaRepository<Worker, Long> {
    @Query("select w from Worker w")
    Page<WorkerProjection> findAllPage(Pageable pageable);
}
