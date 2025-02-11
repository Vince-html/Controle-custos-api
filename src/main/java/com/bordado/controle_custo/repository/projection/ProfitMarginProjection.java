package com.bordado.controle_custo.repository.projection;

import java.math.BigDecimal;

public interface ProfitMarginProjection {
    Long getId();
    String getDescription();
    BigDecimal getValue();
}
