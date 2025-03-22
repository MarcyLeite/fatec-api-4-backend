package com.fatec.api.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.io.geojson.GeoJsonReader;
import org.locationtech.jts.io.ParseException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fatec.api.backend.model.Talhao;
import com.fatec.api.backend.model.Fazenda;
import com.fatec.api.backend.repository.TalhaoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TalhaoService {

    @Autowired
    private TalhaoRepository talhaoRepository;

    public List<Talhao> createTalhoes(String geoJsonContent, Fazenda fazenda) throws IOException, ParseException {
        String cleanedGeoJson = cleanedGeoJson(geoJsonContent);
        List<Talhao> talhoes = new ArrayList<>();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(cleanedGeoJson);
        JsonNode features = rootNode.get("features");

        if (features != null && features.isArray()) {
            for (JsonNode feature : features) {
                Talhao talhao = createTalhao(feature, fazenda);
                talhoes.add(talhaoRepository.save(talhao));
            }
        }

        return talhoes;
    }

    private MultiPolygon processGeoJson(String cleanedGeoJson) throws IOException, ParseException {
        GeoJsonReader reader = new GeoJsonReader();
        MultiPolygon geometry = (MultiPolygon) reader.read(cleanedGeoJson);
        geometry.setSRID(4326);
        return geometry;
    }

    private Float calculateArea(Geometry geometry) {
        double areaEmMetrosQuadrados = geometry.getArea();
        return (float) (areaEmMetrosQuadrados / 10000.0);
    }

    private String cleanedGeoJson(String geoJsonContent) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(geoJsonContent);

        if (jsonNode.has("crs")) {
            ((ObjectNode) jsonNode).remove("crs");
        }

        return objectMapper.writeValueAsString(jsonNode);
    }

    private Talhao createTalhao(JsonNode feature, Fazenda fazenda) throws IOException, ParseException {
        Talhao talhao = new Talhao();

        JsonNode properties = feature.get("properties");
        JsonNode geometryNode = feature.get("geometry");

        if (properties != null) {
            talhao.setNome(properties.has("MN_TL") ? properties.get("MN_TL").asText() : "Desconhecido");
            talhao.setCultura(properties.has("cultura") ? properties.get("cultura").asText() : "Não especificado");
        }

        if (geometryNode != null) {
            String geometryJson = geometryNode.toString();
            MultiPolygon geometry = processGeoJson(geometryJson);
            talhao.setShape(geometry);
            talhao.setArea(calculateArea(geometry));
        }

        talhao.setFazenda(fazenda);
        return talhao;
    }
}
