package com.bordado.controle_custo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class SplitTime {

    private LocalDateTime initialTime;

    private LocalDateTime endTime;

    private Integer totalTime;
}
