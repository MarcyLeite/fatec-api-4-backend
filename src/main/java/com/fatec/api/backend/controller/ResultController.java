package com.fatec.api.backend.controller;
import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fatec.api.backend.model.Resultado;
import com.fatec.api.backend.service.ResultService;

@RestController
@RequestMapping("/result")
@CrossOrigin(origins = "*")
public class ResultController {

    @Autowired
    private ResultService resultService;
    
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Map<String, Resultado>> createResult(
        @RequestPart("talhoes_ids") String talhoesIdsString,
        @RequestPart("file") MultipartFile geoJsonFile) throws ParseException, IOException, org.locationtech.jts.io.ParseException {
        List<Long> talhoesIds = Arrays.stream(talhoesIdsString.split(" "))
                                            .map(String::trim)
                                            .map(str -> Long.valueOf(str))
                                            .collect(Collectors.toList());
        try {
            String geoJsonContent = new String(geoJsonFile.getBytes());
            Resultado resultado = resultService.createResultAI(geoJsonContent, talhoesIds);
            Map<String, Resultado> response = new HashMap<>();
            response.put("resultado", resultado);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            Map<String, Resultado> errorResponse = new HashMap<>();
            errorResponse.put("error", null); 
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
    }
