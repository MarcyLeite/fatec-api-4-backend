package com.fatec.api.backend.DTO;
import lombok.Getter;
import lombok.Setter;

public class TalhaoDTO {

    @Getter
    @Setter
    private Long faz_id;

    public TalhaoDTO() {
    }

    public TalhaoDTO(Long faz_id) {
        this.faz_id = faz_id;
    }

}