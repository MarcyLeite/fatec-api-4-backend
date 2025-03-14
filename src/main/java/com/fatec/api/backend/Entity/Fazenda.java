package com.fatec.api.backend.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "fazenda")
public class Fazenda {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "faz_id")
    private Long fazId;

    @ManyToOne
    @JoinColumn(name = "cid_id")
    private Cidade cidade;

    @Column(name = "faz_nome")
    private String fazNome;

    @Column(name = "faz_prod_anual")
    private float fazProd;

    @Column(name = "faz_area")
    private float fazArea;

    @Column(name = "faz_tipo_solo")
    private String fazTipoSolo;


    public Long getFazId() {
        return fazId;
    }

    public void setFazId(Long fazId) {
        this.fazId = fazId;
    }

    public Cidade getCidade() {
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }

    public String getFazNome() {
        return fazNome;
    }

    public void setFazNome(String fazNome) {
        this.fazNome = fazNome;
    }

    public double getFazProd() {
        return fazProd;
    }

    public void setFazProd(float fazProd) {
        this.fazProd = fazProd;
    }

    public double getFazArea() {
        return fazArea;
    }

    public void setFazArea(float fazArea) {
        this.fazArea = fazArea;
    }

    public String getFazTipoSolo() {
        return fazTipoSolo;
    }

    public void setFazTipoSolo(String fazTipoSolo) {
        this.fazTipoSolo = fazTipoSolo;
    }

    public Fazenda(Long fazId, Cidade cidade, String fazNome, float fazProd, float fazArea, String fazTipoSolo) {
        this.fazId = fazId;
        this.cidade = cidade;
        this.fazNome = fazNome;
        this.fazProd = fazProd;
        this.fazArea = fazArea;
        this.fazTipoSolo = fazTipoSolo;
    }

    public Fazenda() {}
}
