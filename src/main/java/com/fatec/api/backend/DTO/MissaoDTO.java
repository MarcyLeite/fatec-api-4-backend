package com.fatec.api.backend.DTO;

import java.time.LocalDateTime;

import com.fatec.api.backend.model.Missao;
import com.fatec.api.backend.model.Missao.Status;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MissaoDTO {
    private Long id;
    private LocalDateTime dataHoraCriacao;
    private Status status;

    public MissaoDTO(Missao missao) {
        this.id = missao.getId();
        this.dataHoraCriacao = missao.getDataHoraCriacao();
        this.status = missao.getStatus();
    }
}