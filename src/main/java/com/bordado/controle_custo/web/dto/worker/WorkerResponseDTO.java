package com.bordado.controle_custo.web.dto.worker;

import com.bordado.controle_custo.entity.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WorkerResponseDTO {
    private Long id;
    private List<RawMaterial> rawMaterials; // Aqui podemos retornar os objetos completos
    private LaborTypes laborType;
    private ProfitMargin profitMargin;
    private Worker.StatusWorker status;
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Integer timeDedicated;
    private List<SplitTimeResponseDTO> splitTime;
    private BigDecimal totalCost;
}