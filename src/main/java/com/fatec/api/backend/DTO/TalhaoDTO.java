package com.fatec.api.backend.DTO;
import lombok.Getter;
import lombok.Setter;

public class TalhaoDTO {

    @Getter
    @Setter
    private String nome;

    @Getter
    @Setter
    private String cultura;

    @Getter
    @Setter
    private Long faz_id;

    public TalhaoDTO() {
    }

    public TalhaoDTO(String nome, Double area, String cultura, Long faz_id) {
        this.nome = nome;
        this.cultura = cultura;
        this.faz_id = faz_id;
    }

}