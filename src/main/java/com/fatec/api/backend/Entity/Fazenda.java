package com.fatec.api.backend.Entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "fazendas")
public class Fazenda {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "faz_id")
    private Long fazId;

    @Column(name = "cid_id")
    private Cidade cidade;

    @Column(name = "faz_nome")
    private String fazNome;

    @Column(name = "faz_prod_anual")
    private double fazProd;

    @Column(name = "faz_area")
    private double fazArea;

    @Column(name = "faz_tipo_solo")
    private String fazTipoSolo;

    @ManyToMany
    @JoinTable(name = "Fazenda_talhao",
        joinColumns = {@JoinColumn(name = "faz_id")},
        inverseJoinColumns = {@JoinColumn(name = "cid_id")})
    private List<Cidade> cidades;

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

    public void setFazProd(double fazProd) {
        this.fazProd = fazProd;
    }

    public double getFazArea() {
        return fazArea;
    }

    public void setFazArea(double fazArea) {
        this.fazArea = fazArea;
    }

    public String getFazTipoSolo() {
        return fazTipoSolo;
    }

    public void setFazTipoSolo(String fazTipoSolo) {
        this.fazTipoSolo = fazTipoSolo;
    }

    public Fazenda(Long fazId, Cidade cidade, String fazNome, double fazProd, double fazArea, String fazTipoSolo) {
        this.fazId = fazId;
        this.cidade = cidade;
        this.fazNome = fazNome;
        this.fazProd = fazProd;
        this.fazArea = fazArea;
        this.fazTipoSolo = fazTipoSolo;
    }

    public Fazenda() {}
}
