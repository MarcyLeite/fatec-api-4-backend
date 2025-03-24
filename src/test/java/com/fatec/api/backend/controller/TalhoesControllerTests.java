package com.fatec.api.backend.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fatec.api.backend.DTO.FazDTO;
import com.fatec.api.backend.DTO.TalhaoDTO;
import com.fatec.api.backend.model.Cidade;
import com.fatec.api.backend.model.Fazenda;
import com.fatec.api.backend.repository.FazendaRepository;
import com.fatec.api.backend.service.TalhaoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class TalhoesControllerTest {

    @Autowired
    private TalhaoService talhaoService;

    @MockitoBean
    private FazendaRepository fazendaRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private Cidade cidade;

    @InjectMocks
    private TalhoesController talhoesController;

    private final String validGeoJson = "{\"features\":[{\"properties\":{\"MN_TL\":\"Talhão 1\",\"CULTURA\":\"Soja\",\"AREA_HA_TL\":10.5},\"geometry\":{\"type\":\"MultiPolygon\",\"coordinates\":[]}}]}";
    private final String validFazIdJson = "{\"faz_id\":1}";
    private final FazDTO validFazDTO = new FazDTO(1L);
    private final Fazenda validFazenda = new Fazenda();
    private MultipartFile geoJsonFile;
    private String geoJsonContent;


    @BeforeEach
    void setUp() throws IOException {
        when(objectMapper.readValue(validFazIdJson, FazDTO.class)).thenReturn(validFazDTO);
        validFazenda.setId(1L);
        validFazenda.setNome("Fazendinha");
        validFazenda.setArea(30F);
        validFazenda.setProdAnual(30F);
        validFazenda.setTipoSolo("Arenoso");
        validFazenda.setCidade(cidade);
        fazendaRepository.save(validFazenda);
        
        // Inicialize as variáveis
        this.geoJsonFile = new MockMultipartFile("file", "test.geojson", "application/json", validGeoJson.getBytes());
        this.geoJsonContent = new String(geoJsonFile.getBytes());
    }

    @Test
    void createTalhao_Success() throws Exception {
        // Arrange
        List<TalhaoDTO> expectedTalhoes = Collections.singletonList(new TalhaoDTO(1L, "Talhão 1", "Soja", 10.5f));

        when(fazendaRepository.findById(1L)).thenReturn(Optional.of(validFazenda));
        when(talhaoService.createTalhoes(geoJsonContent, validFazenda)).thenReturn(expectedTalhoes);

        // Act
        ResponseEntity<Map<String,List<TalhaoDTO>>> response = talhoesController.createTalhao(validFazIdJson, geoJsonFile);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        
        Map<String, List<TalhaoDTO>> responseBody = (Map<String, List<TalhaoDTO>>) response.getBody();
        assertEquals(1, responseBody.get("talhoes").size());
        assertEquals("Talhão 1", responseBody.get("talhoes").get(0).getNome());
    }


    @Test
    void createTalhao_InvalidJsonFormat() throws Exception {
        String invalidJson = "invalid json";
        
        when(objectMapper.readValue(invalidJson, FazDTO.class)).thenThrow(JsonProcessingException.class);

        // Act
        ResponseEntity<Map<String,List<TalhaoDTO>>> response = talhoesController.createTalhao(invalidJson, geoJsonFile);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        
        Map<String, List<TalhaoDTO>> responseBody = (Map<String, List<TalhaoDTO>>) response.getBody();
        assertTrue(responseBody.get("errors").contains("Invalid JSON format for farm ID"));
    }

    @Test
    void createTalhao_FarmNotFound() throws Exception {
        when(fazendaRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Map<String,List<TalhaoDTO>>> response = talhoesController.createTalhao(validFazIdJson, geoJsonFile);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Map<String, List<TalhaoDTO>> responseBody = (Map<String, List<TalhaoDTO>>) response.getBody();
        assertTrue(responseBody.get("errors").contains("Farm not found with id: 1"));
    }
}