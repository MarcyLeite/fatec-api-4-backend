package com.fatec.api.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fatec.api.backend.model.Talhao;
import com.fatec.api.backend.model.Fazenda;
import org.locationtech.jts.geom.MultiPolygon;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;

@Service
public class TalhaoFactory {

    private final GeoJsonProcessor geoJsonProcessor;

    public TalhaoFactory(GeoJsonProcessor geoJsonProcessor) {
        this.geoJsonProcessor = geoJsonProcessor;
    }

    public Talhao createTalhao(JsonNode feature, Fazenda fazenda) throws IOException, ParseException, org.locationtech.jts.io.ParseException {
        Talhao talhao = new Talhao();

        JsonNode properties = feature.get("properties");
        JsonNode geometryNode = feature.get("geometry");

        if (properties != null) {
            talhao.setNome(properties.has("MN_TL") ? properties.get("MN_TL").asText() : "Desconhecido");
            talhao.setCultura(properties.has("CULTURA") ? properties.get("CULTURA").asText() : "Não especificado");
            talhao.setArea(properties.has("AREA_HA_TL") ? Float.parseFloat(properties.get("AREA_HA_TL").asText()) : 0f);
        }

        if (geometryNode != null) {
            String geometryJson = geometryNode.toString();
            MultiPolygon geometry = geoJsonProcessor.processGeometry(geometryJson);
            talhao.setShape(geometry);
        }

        talhao.setFazenda(fazenda);
        return talhao;
    }
}