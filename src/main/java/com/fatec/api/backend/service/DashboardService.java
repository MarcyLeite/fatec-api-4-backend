package com.fatec.api.backend.service;

import com.fatec.api.backend.DTO.DashboardDTO;
import com.fatec.api.backend.DTO.AnalistaDTO;
import com.fatec.api.backend.DTO.MetricaAnalistaDTO;
import com.fatec.api.backend.model.Relatorio;
import com.fatec.api.backend.model.Usuario;
import com.fatec.api.backend.repository.RelatorioRepository;
import com.fatec.api.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    @Autowired
    private RelatorioRepository relatorioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public DashboardDTO getDashboardData(Long analistaId, LocalDate dataInicio, LocalDate dataFim) {
        LocalDateTime inicio = dataInicio != null ? dataInicio.atStartOfDay() : null;
        LocalDateTime fim = dataFim != null ? dataFim.atTime(23, 59, 59) : null;

        List<Relatorio> relatorios = getRelatoriosFiltrados(analistaId, inicio, fim);

        DashboardDTO dashboard = new DashboardDTO();
        
        // Métricas principais
        dashboard.setTotalMapasConcluidos(relatorios.size());
        dashboard.setTempoMedioQA(calcularTempoMedio(relatorios));
        dashboard.setAprovacoesSemEdicao(contarPorStatus(relatorios, Relatorio.Status.Aproved));
        dashboard.setEdicoesRealizadas(contarPorStatus(relatorios, Relatorio.Status.Edited));
        dashboard.setPercentualModificacoes(calcularPercentualModificacoes(relatorios));
        
        // Métricas por analista
        dashboard.setMetricasPorAnalista(calcularMetricasPorAnalista(relatorios));

        return dashboard;
    }

    public List<AnalistaDTO> getAnalistas() {
        List<Usuario> usuarios = usuarioRepository.findByRole(Usuario.Role.Analista);
        return usuarios.stream()
                .map(usuario -> new AnalistaDTO(usuario.getId(), usuario.getNome()))
                .collect(Collectors.toList());
    }

    private List<Relatorio> getRelatoriosFiltrados(Long analistaId, LocalDateTime inicio, LocalDateTime fim) {
        if (analistaId != null && inicio != null && fim != null) {
            return relatorioRepository.findByUsuarioIdAndStartReviewBetween(analistaId, inicio, fim);
        } else if (analistaId != null) {
            return relatorioRepository.findByUsuarioId(analistaId);
        } else if (inicio != null && fim != null) {
            return relatorioRepository.findByStartReviewBetween(inicio, fim);
        } else {
            return relatorioRepository.findAll();
        }
    }

    private double calcularTempoMedio(List<Relatorio> relatorios) {
        return relatorios.stream()
                .filter(r -> r.getStartReview() != null && r.getEndReview() != null)
                .mapToDouble(r -> ChronoUnit.MINUTES.between(r.getStartReview(), r.getEndReview()))
                .average()
                .orElse(0.0);
    }

    private long contarPorStatus(List<Relatorio> relatorios, Relatorio.Status status) {
        return relatorios.stream()
                .filter(r -> r.getStatus() == status)
                .count();
    }

    private double calcularPercentualModificacoes(List<Relatorio> relatorios) {
        if (relatorios.isEmpty()) return 0.0;
        
        long modificados = contarPorStatus(relatorios, Relatorio.Status.Edited) + 
                          contarPorStatus(relatorios, Relatorio.Status.Reproved);
        
        return (modificados * 100.0) / relatorios.size();
    }

    private List<MetricaAnalistaDTO> calcularMetricasPorAnalista(List<Relatorio> relatorios) {
        Map<Usuario, List<Relatorio>> relatoriosPorAnalista = relatorios.stream()
                .filter(r -> r.getUsuario() != null)
                .collect(Collectors.groupingBy(Relatorio::getUsuario));

        return relatoriosPorAnalista.entrySet().stream()
                .map(entry -> {
                    Usuario analista = entry.getKey();
                    List<Relatorio> relatoriosAnalista = entry.getValue();
                    
                    MetricaAnalistaDTO metrica = new MetricaAnalistaDTO();
                    metrica.setNome(analista.getNome());
                    metrica.setTotalMapas(relatoriosAnalista.size());
                    metrica.setTempoMedio(calcularTempoMedio(relatoriosAnalista));
                    metrica.setAprovacoesSemEdicao((int) contarPorStatus(relatoriosAnalista, Relatorio.Status.Aproved));
                    
                    double taxaAprovacao = relatoriosAnalista.isEmpty() ? 0.0 : 
                        (metrica.getAprovacoesSemEdicao() * 100.0) / relatoriosAnalista.size();
                    metrica.setTaxaAprovacao(String.format("%.1f%%", taxaAprovacao));
                    
                    return metrica;
                })
                .collect(Collectors.toList());
    }
}