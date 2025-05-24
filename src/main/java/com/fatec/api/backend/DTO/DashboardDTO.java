package com.fatec.api.backend.DTO;

import lombok.Data;
import java.util.List;

@Data
public class DashboardDTO {
    private int totalMapasConcluidos;
    private double tempoMedioQA;
    private long  aprovacoesSemEdicao;
    private long  edicoesRealizadas;
    private double percentualModificacoes;
    private List<MetricaAnalistaDTO> metricasPorAnalista;
}