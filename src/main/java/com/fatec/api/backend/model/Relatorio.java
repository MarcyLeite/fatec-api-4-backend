package com.fatec.api.backend.model;

import java.util.Date;

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
@Table(name = "relatorio")
public class Relatorio {
    enum Status {
        Pending,
        Aproved,
        Reproved,
        Edited
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    @Getter
    private Long id;

    @Column(name = "start_review")
    @Getter
    @Setter
    private Date startReview;

    @Column(name = "end_review")
    @Getter
    @Setter
    private Date endReview;

    @Column(name = "percentual_diff")
    @Getter
    @Setter
    private Float percentualDifference;

    @Column(name = "rel_status")
    @Getter
    @Setter
    private Status status;
    
    @ManyToOne
    @JoinColumn(name = "use_id")
    @Getter
    @Setter
    private Usuario usuario;
}
