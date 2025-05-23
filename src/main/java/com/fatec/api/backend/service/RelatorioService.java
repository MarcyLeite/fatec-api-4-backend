package com.fatec.api.backend.service;

import com.fatec.api.backend.DTO.AcuraciaDTO;
import com.fatec.api.backend.model.Missao;
import com.fatec.api.backend.model.Relatorio;
import com.fatec.api.backend.model.Resultado;
import com.fatec.api.backend.model.Usuario;
import com.fatec.api.backend.repository.RelatorioRepository;
import com.fatec.api.backend.repository.ResultadoRepository;
import com.fatec.api.backend.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class RelatorioService {
    
    @Autowired
    private RelatorioRepository relatorioRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private MissaoService missaoService;

    @Autowired
    private ResultadoRepository resultadoRepository;

    /**
     * Processa e salva um relatório no banco de dados
     * 
     * @param startReview Data de início da revisão
     * @param endReview Data final da revisão
     * @param percentualDifference Percentual de diferença
     * @param status Status do relatório
     * @param userId ID do usuário que está gerando o relatório
     * @param missaoId ID da missão relacionada ao relatório
     * @return O relatório salvo
     */
    public Relatorio processarRelatorio(
            LocalDateTime startReview,
            LocalDateTime endReview,
            Relatorio.Status status,
            Long userId,
            Long missaoId) {
        
        Optional<Usuario> user = usuarioRepository.findById(userId);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }

        Resultado res_ai = resultadoRepository.GetByMissionIdAndType(missaoId, Resultado.Source.AI);
        Resultado res_qa = resultadoRepository.GetByMissionIdAndType(missaoId, Resultado.Source.QA);
        
        if (res_ai == null || res_qa == null) {
            throw new IllegalArgumentException("Resultados não encontrados para a missão");
        }
        
        AcuraciaDTO metricas= relatorioRepository.calcularMetricas(res_qa.getId(), res_ai.getId());

        Usuario usuario = user.get();
        
        Relatorio relatorio = new Relatorio();
        Missao missao = missaoService.getMissaobyId(missaoId);
        
        relatorio.setStartReview(startReview);
        relatorio.setEndReview(endReview);
        relatorio.setStatus(status);
        relatorio.setUsuario(usuario);
        relatorio.setMissao(missao);

        relatorio.setPercentualConcordancia(metricas.getConcordancia());
        relatorio.setPercentualFalsoNegativo(metricas.getFalsoNegativo());
        relatorio.setPercentualFalsoPositivo(metricas.getFalsoPositivo());

        
        Relatorio relatorioSalvo = relatorioRepository.save(relatorio);
        
        if (missaoId != null
        && (status == Relatorio.Status.Aproved || status == Relatorio.Status.Reproved)) {
            
            missaoService.finalizeMissao(missao);
        }
        
        return relatorioSalvo;
    }
}