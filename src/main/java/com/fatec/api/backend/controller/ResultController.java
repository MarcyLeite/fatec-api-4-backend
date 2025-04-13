package com.fatec.api.backend.controller;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fatec.api.backend.model.Resultado;
import com.fatec.api.backend.service.ResultService;

@RestController
@RequestMapping("/result")
@CrossOrigin(origins = "*")
public class ResultController {

    @Autowired
    private ResultService resultService;
    
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<String> createResult(
        @RequestPart("talhoes_ids") String talhoes_ids,
        @RequestPart("file") MultipartFile geoJsonFile) throws ParseException, IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        List<Long> talhoesIds = objectMapper.readValue(new java.io.StringReader(talhoes_ids), new TypeReference<List<Long>>() {});
        try {

            String geoJsonContent = new String(geoJsonFile.getBytes());
            Resultado resultado = resultService.createResult(geoJsonContent, talhoesIds);
            Map<String, Resultado> response = new HashMap<>();
            response.put("resultado", resultado);
            return ResponseEntity.ok(objectMapper.writeValueAsString(response));
        } catch (IOException | org.locationtech.jts.io.ParseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid input or parsing error");
        }
    }
    
}
