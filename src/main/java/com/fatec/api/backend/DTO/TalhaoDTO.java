package com.fatec.api.backend.DTO;

public class TalhaoDTO {

    private String nome;
    private String cultura;

    public TalhaoDTO() {
    }

    public TalhaoDTO(String nome, Double area, String cultura) {
        this.nome = nome;
        this.cultura = cultura;
    }

}