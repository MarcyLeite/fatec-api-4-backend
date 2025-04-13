package com.fatec.api.backend.model;

import java.util.List;

import org.locationtech.jts.geom.MultiPolygon;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "talhao")
public class Talhao {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "tal_nome")
    private String nome;

    @Column(name = "tal_cultura")
    private String cultura;

    @Column(name = "rel_shape", columnDefinition = "geometry(MultiPolygon, 4326)")
    private MultiPolygon shape;

    @Column(name = "tal_area")
    private Float area;

    @ManyToOne
    @JoinColumn(name = "faz_id")
    private Fazenda fazenda;

    @ManyToMany(mappedBy ="talhoes")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Missao> missao;
}

