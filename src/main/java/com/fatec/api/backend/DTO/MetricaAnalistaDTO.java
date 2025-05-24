package com.fatec.api.backend.DTO;

import lombok.Data;

@Data
public class MetricaAnalistaDTO {
    private String nome;
    private int totalMapas;
    private double tempoMedio;
    private int aprovacoesSemEdicao;
    private String taxaAprovacao;
}