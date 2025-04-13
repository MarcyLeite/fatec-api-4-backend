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

@Getter
@Setter
@Entity
@Table(name = "fazenda")
public class Fazenda {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "faz_nome")
    private String nome;

    @Column(name = "faz_prod_anual")
    private Float prodAnual;

    @Column(name = "faz_area")
    private Float area;

    @Column(name = "faz_tipo_solo")
    private String tipoSolo;

    @ManyToOne
    @JoinColumn(name = "cid_id")
    private Cidade cidade;
}
