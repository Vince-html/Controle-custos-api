package com.bordado.controle_custo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "worker")
@EntityListeners(AuditingEntityListener.class)
public class Worker implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    private List<RawMaterial> rawMaterial;

    @ManyToOne
    @JoinColumn(name = "id_laborType", nullable = false)
    private LaborTypes laborType;

    @ManyToOne
    @JoinColumn(name = "id_profitMargin", nullable = false)
    private ProfitMargin profitMargin;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusWorker status;

    @ElementCollection
    private List<SplitTime> splitTime;

    public enum StatusWorker {
        INICIADO, FINALIZADO, EM_ANDAMENTO
    }

}

