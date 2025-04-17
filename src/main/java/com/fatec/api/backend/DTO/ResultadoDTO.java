package com.fatec.api.backend.DTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.fatec.api.backend.model.Missao;
import com.fatec.api.backend.model.Resultado;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultadoDTO {

    private Long resultId;
    private String source;
    private MissaoDTO missao;
    private List<DaninhaDTO> daninhasDTO;

    public ResultadoDTO(Resultado resultado, List<DaninhaDTO> daninhas) {
        this.resultId = resultado.getId();
        this.source = resultado.getSource().toString();
        this.missao = new MissaoDTO(resultado.getMissao());
        this.daninhasDTO = daninhas;
    }

    @Getter
    @Setter
    public static class MissaoDTO {
        private Long id;
        private LocalDateTime dataHoraCriacao;
        private String status;
        private List<TalhaoDTO> talhoes;

        public MissaoDTO(Missao missao) {
            this.id = missao.getId();
            this.dataHoraCriacao = missao.getDataHoraCriacao();
            this.status = missao.getStatus().toString();
            this.talhoes = missao.getTalhoes().stream()
                .map(TalhaoDTO::new)
                .collect(Collectors.toList());
        }
    }

    @Getter
    @Setter
    public static class TalhaoDTO {
        private Long id;
        private String nome;
        private String cultura;

        public TalhaoDTO(com.fatec.api.backend.model.Talhao talhao) {
            this.id = talhao.getId();
            this.nome = talhao.getNome();
            this.cultura = talhao.getCultura();
        }
    }
}