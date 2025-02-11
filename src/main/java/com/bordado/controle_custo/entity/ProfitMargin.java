package com.bordado.controle_custo.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name="profit_margin")
@EntityListeners(AuditingEntityListener.class)
public class ProfitMargin implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="description", length = 250)
    private String description;

    @Column(nullable = false, precision = 7, scale = 2)
    private BigDecimal value = BigDecimal.ZERO;
}
