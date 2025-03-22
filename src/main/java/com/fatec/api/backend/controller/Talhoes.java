package com.fatec.api.backend.controller;

import com.fatec.api.backend.service.TalhaoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fatec.api.backend.DTO.TalhaoDTO;
import com.fatec.api.backend.model.Fazenda;
import com.fatec.api.backend.repository.FazendaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@RestController
@RequestMapping("/talhoes")
public class Talhoes {

    @Autowired
    private TalhaoService talhaoService;

    @Autowired
    private FazendaRepository fazendaRepository;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<String> createTalhao(
            @RequestPart("talhaoDTO") String talhaoDTOStr,
            @RequestPart("file") MultipartFile geoJsonFile) {
    
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            TalhaoDTO talhaoDTO = objectMapper.readValue(talhaoDTOStr, TalhaoDTO.class);

    
            Fazenda fazenda = fazendaRepository.getReferenceById(talhaoDTO.getFaz_id());
            String geoJsonContent = new String(geoJsonFile.getBytes());
            talhaoService.createTalhoes(geoJsonContent, fazenda);
            return ResponseEntity.ok("Talhão criado com sucesso");
        } catch (IOException | org.locationtech.jts.io.ParseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Erro ao processar talhão: " + e.getMessage());
        }
    }
}