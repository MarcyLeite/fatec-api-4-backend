package com.fatec.api.backend.model;

import java.util.Date;
import java.util.List;

import org.locationtech.jts.geom.Geometry;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "resultado")
public class Resultado {

    enum Source {
        AI, QA
    };

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "rel_shape", columnDefinition = "geometry(Geometry, 4326)")
    private Geometry shape;

    @Column(name = "res_source")
    private Source source;

    @ManyToOne
    @JoinColumn(name = "tal_id")
    private Talhao talhao;

    @ManyToMany
    @JoinTable(name = "mtm_resultados_talhaos",
        joinColumns = {@JoinColumn(name="res_id")},
        inverseJoinColumns = {@JoinColumn(name="tal_id")})
    private List<Talhao> talhoes;
}
