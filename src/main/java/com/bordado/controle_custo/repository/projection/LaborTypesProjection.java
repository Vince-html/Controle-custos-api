package com.bordado.controle_custo.repository.projection;

import java.math.BigDecimal;

public interface LaborTypesProjection {
    Long getId();
    String getTypeLabor();
    BigDecimal getCost();

}
