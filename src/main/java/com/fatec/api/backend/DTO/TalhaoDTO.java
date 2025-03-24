package com.fatec.api.backend.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TalhaoDTO {
    private Long id;
    private String nome;
    private String cultura;
    private Float area;

    public TalhaoDTO(Long id, String nome, String cultura, Float area) {
        this.id = id;
        this.nome = nome;
        this.cultura = cultura;
        this.area = area;
    }
}