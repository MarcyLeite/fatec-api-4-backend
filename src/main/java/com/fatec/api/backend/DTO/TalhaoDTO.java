package com.fatec.api.backend.DTO;

import com.fasterxml.jackson.databind.node.ArrayNode;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TalhaoDTO {
    private Long id;
    private String nome;
    private String cultura;
    private Float area;
    private ArrayNode geojson;

    public TalhaoDTO(Long id, String nome, String cultura, Float area, ArrayNode geojson) {
        this.id = id;
        this.nome = nome;
        this.cultura = cultura;
        this.area = area;
        this.geojson = geojson;
    }
}