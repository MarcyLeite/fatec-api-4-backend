package com.fatec.api.backend.model;

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
@Table(name = "fazenda")
public class Fazenda {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    @Getter
    private Long id;

    @Column(name = "faz_nome")
    @Getter
    @Setter
    private String nome;

    @Column(name = "faz_prod_anual")
    @Getter
    @Setter
    private Float prodAnual;

    @Column(name = "faz_area")
    @Getter
    @Setter
    private Float area;

    @Column(name = "faz_tipo_solo")
    @Getter
    @Setter
    private String tipoSolo;

    @ManyToOne
    @JoinColumn(name = "cid_id")
    @Getter
    @Setter
    private Cidade cidade;
}
