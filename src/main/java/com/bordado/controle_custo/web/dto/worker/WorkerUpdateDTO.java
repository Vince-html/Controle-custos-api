package com.bordado.controle_custo.web.dto.worker;

import com.bordado.controle_custo.entity.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WorkerUpdateDTO {
    private Long id;
    private List<Long> rawMaterialIds;
    private Long laborTypeId;
    private Long profitMarginId;
    private Worker.StatusWorker status;
    private Integer timeDedicated;
    private List<SplitTime> splitTime;

}
