package com.fatec.api.backend.model;

import org.locationtech.jts.geom.Geometry;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "talhao")
public class Talhao {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    @Getter
    private Long id;

    @Column(name = "tal_nome")
    @Getter
    @Setter
    private String nome;

    @Column(name = "tal_cultura")
    @Getter
    @Setter
    private String cultura;

    @Getter
    @Setter
    @Column(name = "rel_shape", columnDefinition = "geometry(Geometry, 4326)")
    private Geometry shape;

    @Column(name = "tal_area")
    @Getter
    @Setter
    private Float area;

    @ManyToOne
    @JoinColumn(name = "faz_id")
    @Getter
    @Setter
    private Fazenda fazenda;
}

