package com.fatec.api.backend.service;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.io.ParseException;

@ExtendWith(MockitoExtension.class)
public class GeoJsonProcessorTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final String GEO_JSON_CONTENT = """
        {
            "type": "FeatureCollection",
            "crs": { "type": "name", "properties": { "name": "EPSG:4326" } },
            "features": [
                {
                    "type": "Feature",
                    "properties": { "MN_TL": "Talhão 1", "CULTURA": "Soja", "AREA_HA_TL": 10.5 },
                    "geometry": { "type": "MultiPolygon", "coordinates": [[[[0,0],[0,1],[1,1],[1,0],[0,0]]]] }
                }
            ]
        }
    """;

    private static final String GEO_JSON_CLEANED = """
        {
            "type": "FeatureCollection",
            "features": [
                {
                    "type": "Feature",
                    "properties": { "MN_TL": "Talhão 1", "CULTURA": "Soja", "AREA_HA_TL": 10.5 },
                    "geometry": { "type": "MultiPolygon", "coordinates": [[[[0,0],[0,1],[1,1],[1,0],[0,0]]]] }
                }
            ]
        }
    """;

    private GeoJsonProcessor processor;

    @BeforeEach
    void setUp() {
        processor = new GeoJsonProcessor();
    }

    @Test
    void cleanGeoJson_Success() throws IOException {
        String cleaned = processor.cleanGeoJson(GEO_JSON_CONTENT);
        JsonNode expectedNode = OBJECT_MAPPER.readTree(GEO_JSON_CLEANED);
        JsonNode actualNode = OBJECT_MAPPER.readTree(cleaned);
        assertEquals(expectedNode, actualNode);
    }

    @Test
    void extractFeatures_Success() throws IOException {
        JsonNode rootNode = processor.extractFeatures(GEO_JSON_CONTENT);
        JsonNode expectedNode = OBJECT_MAPPER.readTree("""
            [
                {
                    "type": "Feature",
                    "properties": { "MN_TL": "Talhão 1", "CULTURA": "Soja", "AREA_HA_TL": 10.5 },
                    "geometry": { "type": "MultiPolygon", "coordinates": [[[[0,0],[0,1],[1,1],[1,0],[0,0]]]] }
                }
            ]
        """);
        assertEquals(expectedNode, rootNode);
    }

    @Test
    void processGeometry_Success() throws ParseException {
        String geometryJson = """
            {
                "type": "MultiPolygon",
                "coordinates": [[[[0,0],[0,1],[1,1],[1,0],[0,0]]]]
            }
        """;
        MultiPolygon geometry = processor.processGeometry(geometryJson);
        assertNotNull(geometry);
        assertEquals(4326, geometry.getSRID());
    }

    @Test
    void processGeometry_InvalidJson_ThrowsParseException() {
        String invalidGeometryJson = """
            {
                "type": "InvalidType",
                "coordinates": [[[[0,0],[0,1],[1,1],[1,0],[0,0]]]]
            }
        """;
        assertThrows(ParseException.class, () -> processor.processGeometry(invalidGeometryJson));
    }
}
