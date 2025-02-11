package com.bordado.controle_custo.web.dto.profitMargin;

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
    private BigDecimal cost;
    private Long id;
}
