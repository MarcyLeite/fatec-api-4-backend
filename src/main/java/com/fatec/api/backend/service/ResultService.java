package com.fatec.api.backend.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Async;

import com.fasterxml.jackson.databind.JsonNode;
import com.fatec.api.backend.DTO.ResultadoDTO;
import com.fatec.api.backend.model.Missao;
import com.fatec.api.backend.model.Resultado;
import com.fatec.api.backend.model.Talhao;
import com.fatec.api.backend.repository.ResultadoRepository;

@Service
public class ResultService {

    @Autowired
    private final GeoJsonProcessor geoJsonProcessor;

    @Autowired
    private MissaoService missaoService;

    @Autowired
    private TalhaoService talhaoService;

    @Autowired
    private ResultadoRepository resultadoRepository;

    @Autowired
    private DaninhasService daninhasService;

    @Autowired
    private RelatorioService relatorioService;

    public ResultService(GeoJsonProcessor gjp){
        this.geoJsonProcessor = gjp;
    }

    public Resultado salvarResultado(Resultado.Source tipoFonte, Missao missao){
        Resultado resultado = new Resultado(null, tipoFonte, missao);
        try {
            return resultadoRepository.save(resultado);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Já existe um resultado com o mesmo tipo para esta missão.");
        }
    }

    @Async
    public void createResultAIAsync(String geoJsonContent, List<Long> talhoes_id) throws IOException, org.locationtech.jts.io.ParseException { // aqui deveria ser um patter provavelmente de factory

        
        List<Talhao> talhoes = talhaoService.filterTalhoesByIds(talhoes_id);
        Missao missao = missaoService.CreateMissao(talhoes);
        Resultado resultado = salvarResultado(Resultado.Source.AI, missao);
        String cleanedGeoJson = geoJsonProcessor.cleanGeoJson(geoJsonContent);
        JsonNode features = geoJsonProcessor.extractFeatures(cleanedGeoJson);
        daninhasService.registerDaninhas(features, resultado);
    }

    @Async
    public void createResultQAAsyn(String geoJsonContent, Long missao_id) throws IOException, org.locationtech.jts.io.ParseException { // aqui deveria ser um patter provavelmente de factory
        Missao missao = missaoService.getMissaobyId(missao_id);
        Resultado resultado = salvarResultado(Resultado.Source.QA, missao);
        String cleanedGeoJson = geoJsonProcessor.cleanGeoJson(geoJsonContent);
        JsonNode features = geoJsonProcessor.extractFeatures(cleanedGeoJson);
        daninhasService.registerDaninhas(features, resultado);
        missaoService.finalizeMissao(missao);
        relatorioService.setAcuracia(missao.getId());

    }

    public ResultadoDTO getResultadoByMission(Long mission_id, String type_result, Long talhao_id){
        Resultado.Source source = Resultado.Source.valueOf(type_result.toUpperCase());
        Resultado resultado = resultadoRepository.GetByMissionIdAndType(mission_id, source);
        Talhao talhao = talhaoService.getTalhao(talhao_id);
        JsonNode daninhasDTO = daninhasService.getDaninhasByResult(resultado.getId(), talhao.getId());
        ResultadoDTO resultadoDTO = new ResultadoDTO(resultado, daninhasDTO);
        return resultadoDTO;
    }
}