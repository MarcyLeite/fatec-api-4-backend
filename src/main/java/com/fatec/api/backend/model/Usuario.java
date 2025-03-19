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

@Entity
@Table(name = "usuario")
public class Usuario {
    enum Role {
        Administrador,
        Consultor,
        Analista,
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    @Getter
    private Long id;

    @Column(name = "use_nome")
    @Getter
    @Setter
    private String nome;

    @Column(name = "use_email")
    @Getter
    @Setter
    private String email;

    @Column(name = "use_role")
    @Getter
    @Setter
    private Role role;

    @Column(name = "created_at")
    @Getter
    @Setter
    private Date createdAt;
}
