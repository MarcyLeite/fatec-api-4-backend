package com.fatec.api.backend.DTO;

import java.util.List;

import com.fatec.api.backend.model.Resultado;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultadoDTO {

    private Resultado resultado;
    private List<DaninhaDTO> daninhasDTO;

    public ResultadoDTO(Resultado resultado, List<DaninhaDTO> daninhas){
        this.resultado = resultado;
        this.daninhasDTO = daninhas;

    }

}
