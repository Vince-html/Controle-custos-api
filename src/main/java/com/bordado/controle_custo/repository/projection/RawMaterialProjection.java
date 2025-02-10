package com.bordado.controle_custo.repository.projection;

import com.bordado.controle_custo.entity.Supplier;

import java.math.BigDecimal;

public interface RawMaterialProjection {
    Long getId();
    String getName();
    String getDescription();
    Supplier getSupplier();
    BigDecimal getCost();
    Integer getAmount();
}
