package com.fatec.api.backend.DTO;

import lombok.Data;

@Data
public class AnalistaDTO {
    private Long id;
    private String nome;
    
    public AnalistaDTO(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }
    
    public AnalistaDTO() {}
}