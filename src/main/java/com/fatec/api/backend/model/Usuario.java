package com.fatec.api.backend.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "usuario")
public class Usuario {
   public enum Role {
        Administrador,
        Consultor,
        Analista,
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "use_nome")
    private String nome;

    @Column(name = "use_email")
    private String email;

    @Column(name = "use_role")
    private Role role;

    @Column(name = "active")
    private Boolean ativo;

    @Column(name = "created_at")
    private Date createdAt;
}
