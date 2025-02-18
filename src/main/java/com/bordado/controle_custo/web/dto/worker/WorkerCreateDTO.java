package com.bordado.controle_custo.web.dto.worker;

import com.bordado.controle_custo.entity.SplitTime;
import com.bordado.controle_custo.entity.Worker;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WorkerCreateDTO {
    private List<Long> rawMaterialIds; // Apenas IDs das matérias-primas
    private Long laborTypeId;
    private Long profitMarginId;
    private Worker.StatusWorker status;
    private Integer timeDedicated;
    private List<SplitTime> splitTime;
}