package com.fatec.api.backend.DTO;

import com.fasterxml.jackson.databind.node.ArrayNode;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DaninhaDTO {
    private ArrayNode geojson;

    public DaninhaDTO(ArrayNode geojson) {
        this.geojson = geojson;
    }
    
}
