package com.fatec.api.backend.Entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "estados")
public class Estado {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "est_id")
    private Long estId;

    @Column(name = "est_name")
    private String estNome;

    @ManyToMany(mappedBy = "estados")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Cidade> cidades;

    public Long getEstId() {
        return estId;
    }

    public void setEstId(Long estId) {
        this.estId = estId;
    }

    public String getEstNome() {
        return estNome;
    }

    public void setEstNome(String estNome) {
        this.estNome = estNome;
    }

    public Estado(Long estId, String estNome) {
        this.estId = estId;
        this.estNome = estNome;
    }

    public Estado() {}
}
