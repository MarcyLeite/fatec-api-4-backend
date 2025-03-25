package com.fatec.api.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fatec.api.backend.DTO.TalhaoDTO;
import com.fatec.api.backend.model.Fazenda;
import com.fatec.api.backend.model.Talhao;
import com.fatec.api.backend.repository.TalhaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TalhaoServiceTest {

    @Mock
    private TalhaoRepository talhaoRepository;

    @Mock
    private GeoJsonProcessor geoJsonProcessor;

    @Mock
    private TalhaoFactory talhaoFactory;

    @InjectMocks
    private TalhaoService talhaoService;

    private Fazenda fazenda;
    private String geoJsonContent;
    private JsonNode features;

    @BeforeEach
    void setUp() throws IOException, ParseException, org.locationtech.jts.io.ParseException {
        fazenda = new Fazenda();
        fazenda.setId(1L);
        fazenda.setNome("Fazenda Santíssima");

        geoJsonContent = "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"properties\":{\"MN_TL\":\"Talhão 1\",\"CULTURA\":\"Soja\",\"AREA_HA_TL\":10.5},\"geometry\":{\"type\":\"MultiPolygon\",\"coordinates\":[[[[0,0],[0,1],[1,1],[1,0],[0,0]]]]}}]}";

        ObjectMapper objectMapper = new ObjectMapper();
        features = objectMapper.readTree(geoJsonContent).get("features");
    }

    @Test
    void createTalhoes_Success() throws IOException, ParseException, org.locationtech.jts.io.ParseException {
        Talhao talhao = new Talhao();
        talhao.setNome("Talhão 1");
        talhao.setCultura("Soja");
        talhao.setArea(10.5f);

        when(geoJsonProcessor.cleanGeoJson(geoJsonContent)).thenReturn(geoJsonContent);
        when(geoJsonProcessor.extractFeatures(geoJsonContent)).thenReturn(features);
        when(talhaoFactory.createTalhao(features.get(0), fazenda)).thenReturn(talhao);
        when(talhaoRepository.save(talhao)).thenReturn(talhao);

        List<TalhaoDTO> talhoes = talhaoService.createTalhoes(geoJsonContent, fazenda);

        assertEquals(1, talhoes.size());
        assertEquals("Talhão 1", talhoes.get(0).getNome());
        assertEquals("Soja", talhoes.get(0).getCultura());
        assertEquals(10.5f, talhoes.get(0).getArea());
    }

    @Test
    void createTalhoes_InvalidGeoJson() throws IOException, ParseException, org.locationtech.jts.io.ParseException {
        String invalidGeoJsonContent = "invalid geojson";
        when(geoJsonProcessor.cleanGeoJson(invalidGeoJsonContent)).thenThrow(IOException.class);

        assertThrows(IOException.class, () -> {
            talhaoService.createTalhoes(invalidGeoJsonContent, fazenda);
        });
    }
}