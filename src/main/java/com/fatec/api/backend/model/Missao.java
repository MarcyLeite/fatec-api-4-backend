package com.fatec.api.backend.model;

import java.util.Date;
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
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "missao")
public class Missao {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "created_at")
    private Date createdAt; 
    
    @ManyToMany
    @JoinTable(name = "mtm_missoes_talhoes",
        joinColumns = {@JoinColumn(name="mis_id")},
        inverseJoinColumns = {@JoinColumn(name="tal_id")})
    private List<Talhao> talhoes;
}
