package com.fatec.api.backend.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "relatorio")
public class Relatorio {
    public enum Status {
        Pending,
        Aproved,
        Reproved,
        Edited
    }
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "start_review")
    private LocalDateTime startReview;

    @Column(name = "end_review")
    private LocalDateTime endReview;

    @Column(name = "percentual_concordancia")
    private Double percentualConcordancia;

    @Column(name = "percentual_falso_negativo")
    private Double percentualFalsoNegativo;

    @Column(name = "percentual_falso_positivo")
    private Double percentualFalsoPositivo;

    @Column(name = "rel_status")
    private Status status;

    @OneToOne
    @JoinColumn(name = "mis_id")
    private Missao missao;
    
    @ManyToOne
    @JoinColumn(name = "use_id")
    private Usuario usuario;
}
