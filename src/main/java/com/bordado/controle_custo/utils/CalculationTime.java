package com.bordado.controle_custo.utils;

import com.bordado.controle_custo.entity.SplitTime;
import org.springframework.validation.FieldError;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class CalculationTime {

    public static Integer CalculationTotalSplitTime(LocalDateTime initialDate, LocalDateTime finalDate) {
        return (int) Duration.between(initialDate, finalDate).toSeconds();
    }

    public static Integer CalculationTotalDedicatedTime(List<SplitTime> splitTimeList) {
        Integer resultTimeDedicated = 0;
        for (SplitTime splitTime : splitTimeList) {
            resultTimeDedicated += splitTime.getTotalTime();
        }
        return resultTimeDedicated;
    }
}
