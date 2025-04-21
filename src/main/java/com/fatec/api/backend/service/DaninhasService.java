package com.fatec.api.backend.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.locationtech.jts.geom.MultiPolygon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fatec.api.backend.DTO.DaninhaDTO;
import com.fatec.api.backend.model.Daninha;
import com.fatec.api.backend.model.Resultado;
import com.fatec.api.backend.repository.DaninhaRepository;

@Service
public class DaninhasService {

    @Autowired
    private DaninhaRepository daninhaRepository;

    @Autowired
    private GeoJsonProcessor geoJsonProcessor;

    @Autowired
    private ObjectMapper objectMapper; 

    public List<DaninhaDTO> registerDaninhas(JsonNode features, Resultado resultado) throws org.locationtech.jts.io.ParseException{
        List<DaninhaDTO> daninhas = new ArrayList<>();
        if (features != null && features.isArray()) {
            for (JsonNode feature : features) {
                JsonNode geometryNode = feature.get("geometry");
                if (geometryNode != null && "MultiPolygon".equals(geometryNode.get("type").asText())) {
                    String geometryJson = geometryNode.toString();
                    MultiPolygon geometry = geoJsonProcessor.processGeometry(geometryJson);
                    Daninha daninhaDaVez = new Daninha(null, geometry, resultado);
                    daninhaRepository.save(daninhaDaVez);
                    DaninhaDTO daninhaDTO = this.convertToGeoDTO(daninhaDaVez);
                    daninhas.add(daninhaDTO);
                }
            }
        }
        return daninhas;
    }
    private DaninhaDTO convertToGeoDTO(Daninha daninha) {
        ArrayNode geoJson = geoJsonProcessor.extractCoordinates(daninha.getGeom());
        return new DaninhaDTO(geoJson);
    }

    public JsonNode getDaninhasByResult(Long resultado_id, Long talhao_id) {
        String aggregatedGeoJson = daninhaRepository.findAggregatedGeoJsonByResultadoIdAndTalhaoId(resultado_id, talhao_id);
        try {
            return objectMapper.readTree(aggregatedGeoJson);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao processar GeoJSON agregado", e);
        }
    }
}
