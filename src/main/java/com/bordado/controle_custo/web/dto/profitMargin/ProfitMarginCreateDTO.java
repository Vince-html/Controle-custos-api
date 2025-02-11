package com.bordado.controle_custo.web.dto.profitMargin;

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
public class ProfitMarginCreateDTO {
    private BigDecimal profitValue;

    @NotBlank
    @Size(max = 100)
    private String description;
}
