package com.fatec.api.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "estado")
public class Estado {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    @Getter
    @Setter
    private Long id;

    @Column(name = "est_nome")
    @Getter
    @Setter
    private String nome;    

    @Column(name = "est_sigla")
    @Getter
    @Setter
    private String sigla; 
}
