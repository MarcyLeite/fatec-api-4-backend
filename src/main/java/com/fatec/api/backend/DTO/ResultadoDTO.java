package com.fatec.api.backend.DTO;


import com.fasterxml.jackson.databind.JsonNode;
import com.fatec.api.backend.model.Resultado;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultadoDTO {

    private Long resultId;
    private String source;
    private JsonNode daninhasDTO;

    public ResultadoDTO(Resultado resultado, JsonNode coorDaninhas) {
        this.resultId = resultado.getId();
        this.source = resultado.getSource().toString();
        this.daninhasDTO = coorDaninhas;
    }
}