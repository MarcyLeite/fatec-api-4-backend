package com.fatec.api.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.geojson.GeoJsonReader;
import org.locationtech.jts.io.ParseException;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class GeoJsonProcessor {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final GeoJsonReader geoJsonReader = new GeoJsonReader();

    public String cleanGeoJson(String geoJsonContent) throws IOException {
        JsonNode jsonNode = objectMapper.readTree(geoJsonContent);
        if (jsonNode.has("crs")) {
            ((ObjectNode) jsonNode).remove("crs");
        }
        return objectMapper.writeValueAsString(jsonNode);
    }

    public ArrayNode extractCoordinates(MultiPolygon multiPoly) {
        ArrayNode multiPolygonArray = objectMapper.createArrayNode();

        for (int i = 0; i < multiPoly.getNumGeometries(); i++) {
            Polygon polygon = (Polygon) multiPoly.getGeometryN(i);
            ArrayNode polygonArray = objectMapper.createArrayNode();
            ArrayNode exteriorRing = objectMapper.createArrayNode();

            for (Coordinate coord : polygon.getExteriorRing().getCoordinates()) {
                ArrayNode point = objectMapper.createArrayNode();
                point.add(coord.x);
                point.add(coord.y);
                exteriorRing.add(point);
            }

            polygonArray.add(exteriorRing);
            multiPolygonArray.add(polygonArray);
        }

        return multiPolygonArray;
        }

    public JsonNode extractFeatures(String cleanedGeoJson) throws IOException {
        JsonNode rootNode = objectMapper.readTree(cleanedGeoJson);
        return rootNode.get("features");
    }

    public MultiPolygon processGeometry(String geometryJson) throws ParseException {
        MultiPolygon geometry = (MultiPolygon) geoJsonReader.read(geometryJson);
        geometry.setSRID(4326);
        return geometry;
    }
}