package com.fatec.api.backend.controller;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fatec.api.backend.service.TalhaoService;
import com.fatec.api.backend.geojson.TalhaoGeoDTO;

@RequestMapping("talhao")
@RestController
@CrossOrigin(origins = "*")
public class TalhaoController {
    
    private TalhaoService talhaoService;

    public TalhaoController(TalhaoService talhaoService) {
        this.talhaoService = talhaoService;
    }

    @GetMapping("/editar")
    public ResponseEntity<Map<String, Object>> listarFazendasEAnalistas() {
        
        return ResponseEntity.ok(talhaoService.listarFazendaEAnalistas());
    }

    @GetMapping("/listar/{page}/{quantity}")
    public ResponseEntity<Page<TalhaoGeoDTO>> listarTalhoesPaginados(
            @PathVariable int page,
            @PathVariable int quantity) {
            Page<TalhaoGeoDTO> talhoes = talhaoService.listarTalhoesPaginados(page, quantity);
        return ResponseEntity.ok(talhoes);
    }
}
