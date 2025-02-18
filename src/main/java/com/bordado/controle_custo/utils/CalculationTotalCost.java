package com.bordado.controle_custo.utils;

import com.bordado.controle_custo.entity.LaborTypes;
import com.bordado.controle_custo.entity.ProfitMargin;
import com.bordado.controle_custo.entity.RawMaterial;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class CalculationTotalCost {

    public static BigDecimal CalculationTotalCostDedicated(LaborTypes laborTypes, ProfitMargin profitMargin, List<RawMaterial> rawMaterials, Integer totalHoursDedicated) {

        BigDecimal costPerMinute = laborTypes.getCost().divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);

        BigDecimal totalMinutes = BigDecimal.valueOf(totalHoursDedicated).divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);

        BigDecimal totalLaborCost = costPerMinute.multiply(totalMinutes);

        BigDecimal totalRawMaterialCost = BigDecimal.ZERO;
        for (RawMaterial rawMaterial : rawMaterials) {
            totalRawMaterialCost = totalRawMaterialCost.add(rawMaterial.getCost());
        }

        BigDecimal totalCost = totalLaborCost.add(totalRawMaterialCost);

        BigDecimal finalCost = totalCost.multiply(BigDecimal.ONE.add(profitMargin.getProfitValue().divide(BigDecimal.valueOf(100))));

        finalCost = finalCost.setScale(2, RoundingMode.HALF_UP);

        return finalCost;
    }

}
