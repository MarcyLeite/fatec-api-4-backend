package com.fatec.api.backend.controller;

import com.fatec.api.backend.service.TalhaoService;
import com.fatec.api.backend.DTO.TalhaoDTO;
import com.fatec.api.backend.model.Fazenda;
import com.fatec.api.backend.repository.FazendaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @PostMapping
    public ResponseEntity<String> createTalhao(
            @RequestBody TalhaoDTO talhaoDTO,
            @RequestParam("file") MultipartFile geoJsonFile) {
            Fazenda fazenda = fazendaRepository.getReferenceById(talhaoDTO.getFaz_id());
        try {
            talhaoService.createTalhao(talhaoDTO, geoJsonFile, fazenda);
            return ResponseEntity.ok("Talhão criado com sucesso");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Erro ao processar talhão: " + e.getMessage());
        }
    }
}