package com.bordado.controle_custo.web.dto.labortypes;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LaborTypesCreateDTO {

    @NotBlank
    @Size(max=250)
    private String typeLabor;

    private BigDecimal cost;
}
