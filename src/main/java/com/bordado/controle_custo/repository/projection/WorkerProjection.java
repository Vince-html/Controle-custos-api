package com.bordado.controle_custo.repository.projection;

import com.bordado.controle_custo.entity.*;

import java.util.List;

public interface WorkerProjection {

    Long getId();

    List<RawMaterial> getRawMaterial();

    LaborTypes getLaborType();

    ProfitMargin getProfitMargin();

    Worker.StatusWorker getStatus();

    Integer getTimeDedicated();

    List<SplitTime> getSplitTime();
}
