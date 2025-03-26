package com.fatec.api.backend.controller;

import com.fatec.api.backend.service.TalhaoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fatec.api.backend.DTO.FazDTO;
import com.fatec.api.backend.DTO.TalhaoDTO;
import com.fatec.api.backend.model.Fazenda;
import com.fatec.api.backend.repository.FazendaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/talhao")
public class TalhaoController {

    @Autowired
    private TalhaoService talhaoService;

    @Autowired
    private FazendaRepository fazendaRepository;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Map<String, List<TalhaoDTO>>> createTalhao(
            @RequestPart("faz_id") String fazId,
            @RequestPart("file") MultipartFile geoJsonFile) throws ParseException, JsonProcessingException, JsonMappingException {
    
            ObjectMapper objectMapper = new ObjectMapper();
            FazDTO fazDTO = objectMapper.readValue(fazId, FazDTO.class);        
            try {
            Fazenda fazenda = fazendaRepository.getReferenceById(fazDTO.getFaz_id());
            String geoJsonContent = new String(geoJsonFile.getBytes());
            List<TalhaoDTO> talhoes = talhaoService.createTalhoes(geoJsonContent, fazenda);
            Map<String, List<TalhaoDTO>> response = new HashMap<>();
            response.put("talhoes", talhoes);
            return ResponseEntity.ok(response);
        } catch (IOException | org.locationtech.jts.io.ParseException e) {
            Map<String, List<TalhaoDTO>> errorResponse = new HashMap<>();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
        }
    }
    
    @GetMapping("/listar/{page}/{quantity}")
    public ResponseEntity<Page<TalhaoDTO>> listarTalhoesPaginados(
            @RequestParam(defaultValue = "0") int page, 
            @RequestParam(defaultValue = "10") int size) {
            Page<TalhaoDTO> talhoes = talhaoService.listarTalhoesPaginados(page, size);
        return ResponseEntity.ok(talhoes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TalhaoDTO> getTalhao(@PathVariable Long id) {
        TalhaoDTO talhao = talhaoService.getTalhao(id);
        return ResponseEntity.ok(talhao);
    }
}
