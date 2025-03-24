package com.fatec.api.backend.DTO;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class FazDTO {
    private Long faz_id;

    public FazDTO(Long faz_id) {
        this.faz_id = faz_id;
    }
    
}

