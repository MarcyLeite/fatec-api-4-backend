package com.fatec.api.backend.model;

import java.util.Date;

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
@Table(name = "resultado")
public class Resultado {

    enum Source {
        AI, QA
    };

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    @Getter
    private Long id;

    @Column(name = "created_at")
    @Getter
    @Setter
    private Date createdAt;

    @Getter
    @Setter
    @Column(name = "rel_shape", columnDefinition = "geometry(Geometry, 4326)")
    private Geometry shape;

    @Column(name = "rel_sourch")
    @Getter
    @Setter
    private Source source;

    @ManyToOne
    @JoinColumn(name = "tal_id")
    @Getter
    @Setter
    private Talhao talhao;

    @ManyToOne
    @JoinColumn(name = "rel_id")
    @Getter
    @Setter
    private Resultado resultado;
}
