package com.bordado.controle_custo.web.dto.rawmaterial;


import com.bordado.controle_custo.entity.Supplier;
import com.bordado.controle_custo.web.dto.supplier.SupplierResponseDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RawMaterialResponseDTO {

    private Long id;
    private String name;
    private String description;
    private int amount;
    private BigDecimal cost;
    private SupplierResponseDTO supplier;
}
