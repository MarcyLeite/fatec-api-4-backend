package com.fatec.api.backend.Entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

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
@Table(name = "cidades")
public class Cidade {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cid_id")
    private Long cidId;

    @Column(name = "cid_name")
    private String cidNome;

    @Column(name = "est_id")
    private Estado estado;

    @ManyToMany(mappedBy = "cidades")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Fazenda> fazendas;

    @ManyToMany
    @JoinTable(name = "Cidade_estado",
        joinColumns = {@JoinColumn(name = "cid_id")},
        inverseJoinColumns = {@JoinColumn(name = "est_id")})
    private List<Estado> estados;

    public Long getCidId() {
        return cidId;
    }

    public void setCidId(Long cidId) {
        this.cidId = cidId;
    }

    public String getCidNome() {
        return cidNome;
    }

    public void setCidNome(String cidNome) {
        this.cidNome = cidNome;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Cidade(Long cidId, String cidNome, Estado estado) {
        this.cidId = cidId;
        this.cidNome = cidNome;
        this.estado = estado;
    }

    public Cidade() {}
}
