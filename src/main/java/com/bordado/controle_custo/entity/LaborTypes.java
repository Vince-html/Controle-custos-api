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
@Table(name="labor_types")
@EntityListeners(AuditingEntityListener.class)
public class LaborTypes implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="labor_type", length = 250, unique = true)
    private String typeLabor;

    @Column(nullable = false, precision = 7, scale = 2)
    private BigDecimal cost = BigDecimal.ZERO;
}
