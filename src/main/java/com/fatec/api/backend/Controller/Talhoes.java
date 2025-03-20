package com.fatec.api.backend.Controller;

import com.fatec.api.backend.Service.TalhaoService;
import com.fatec.api.backend.DTO.TalhaoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/talhoes")
public class Talhoes {

    @Autowired
    private TalhaoService talhaoService;

    @PostMapping
    public ResponseEntity<String> createTalhao(
            @RequestBody TalhaoDTO talhao,
            @RequestParam("file") MultipartFile geoJsonFile) {
        try {
            GeoJsonDTO geoJson = objectMapper.readValue(geoJsonFile.getInputStream(), GeoJsonDTO.class);
            return ResponseEntity.ok("GeoJSON recebido com " + geoJson.getFeatures().size() + " features.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao processar GeoJSON: " + e.getMessage());
        }
    }

}