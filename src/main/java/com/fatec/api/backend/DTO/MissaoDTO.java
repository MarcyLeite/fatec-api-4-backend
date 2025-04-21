package com.fatec.api.backend.DTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
    private List<Long> talhoes_id; 

    public MissaoDTO(Missao missao) {
        this.id = missao.getId();
        this.dataHoraCriacao = missao.getDataHoraCriacao();
        this.talhoes_id = missao.getTalhoes().stream()
                                .map(t -> t.getId())
                                .collect(Collectors.toList());
    }
}