package com.fatec.api.backend.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.locationtech.jts.geom.MultiPolygon;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.JsonNode;
import com.fatec.api.backend.model.Resultado;

@Service
public class ResultService {

    @Autowired
    private final GeoJsonProcessor geoJsonProcessor;

    public ResultService(GeoJsonProcessor gjp){
        this.geoJsonProcessor = gjp;
    }

    public Resultado createResult(String geoJsonContent, List<Long> talhoes_id) throws IOException, ParseException, org.locationtech.jts.io.ParseException {

        String cleanedGeoJson = geoJsonProcessor.cleanGeoJson(geoJsonContent);
        JsonNode features = geoJsonProcessor.extractFeatures(cleanedGeoJson);

        Resultado resultado = new Resultado();

        if (features != null && features.isArray()) {
            for (JsonNode feature : features) {
                JsonNode geometryNode = feature.get("geometry");
                if (geometryNode != null && "MultiPolygon".equals(geometryNode.get("type").asText())) {
                    String geometryJson = geometryNode.toString();
                    MultiPolygon geometry = geoJsonProcessor.processGeometry(geometryJson);
                }
            }
        }
        return resultado;
    }
}