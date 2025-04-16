package com.fatec.api.backend.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.locationtech.jts.geom.MultiPolygon;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.JsonNode;
import com.fatec.api.backend.model.Daninha;
import com.fatec.api.backend.model.Missao;
import com.fatec.api.backend.model.Resultado;
import com.fatec.api.backend.model.Talhao;
import com.fatec.api.backend.repository.DaninhaRepository;
import com.fatec.api.backend.repository.MissaoRepository;
import com.fatec.api.backend.repository.ResultadoRepository;
import com.fatec.api.backend.repository.TalhaoRepository;

@Service
public class ResultService {

    @Autowired
    private final GeoJsonProcessor geoJsonProcessor;

    @Autowired
    private MissaoService missaoService;

    @Autowired
    private TalhaoRepository talhaoRepository;

    @Autowired
    private DaninhaRepository daninhaRepository;

    @Autowired
    private ResultadoRepository resultadoRepository;

    public ResultService(GeoJsonProcessor gjp){
        this.geoJsonProcessor = gjp;
    }

    public Resultado createResultAI(String geoJsonContent, List<Long> talhoes_id) throws IOException, org.locationtech.jts.io.ParseException { // aqui deveria ser um patter provavelmente de factory

        List<Talhao> talhoes = talhaoRepository.findAllById(talhoes_id);
        Missao missao = missaoService.CreateMissao(talhoes);
        Resultado resultado = new Resultado(null, Resultado.Source.AI, missao);
        resultado = resultadoRepository.save(resultado);
        String cleanedGeoJson = geoJsonProcessor.cleanGeoJson(geoJsonContent);
        JsonNode features = geoJsonProcessor.extractFeatures(cleanedGeoJson);
        registerDaninhas(features, resultado);
        return resultado;
    }

    private List<Daninha> registerDaninhas(JsonNode features, Resultado resultado) throws org.locationtech.jts.io.ParseException{
        List<Daninha> daninhas = new ArrayList<>();
        if (features != null && features.isArray()) {
            for (JsonNode feature : features) {
                JsonNode geometryNode = feature.get("geometry");
                if (geometryNode != null && "MultiPolygon".equals(geometryNode.get("type").asText())) {
                    String geometryJson = geometryNode.toString();
                    MultiPolygon geometry = geoJsonProcessor.processGeometry(geometryJson);
                    Daninha daninhaDaVez = new Daninha(null, geometry, resultado);
                    daninhaRepository.save(daninhaDaVez);
                    daninhas.add(daninhaDaVez);
                }
            }
        }
        return daninhas;
    }
}