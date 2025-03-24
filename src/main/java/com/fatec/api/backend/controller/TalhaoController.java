package com.fatec.api.backend.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fatec.api.backend.geojson.TalhaoGeoDTO;
import com.fatec.api.backend.service.TalhaoService;

@RestController
@RequestMapping("/talhao")
@CrossOrigin(origins = "*")
public class TalhaoController {

    private TalhaoService talhaoService;

    public TalhaoController(TalhaoService talhaoService) {
        this.talhaoService = talhaoService;
    }

    @GetMapping("/listar/{page}/{quantity}")
    public ResponseEntity<Page<TalhaoGeoDTO>> listarTalhoesPaginados(
            @PathVariable int page,
            @PathVariable int quantity) {
            Page<TalhaoGeoDTO> talhoes = talhaoService.listarTalhoesPaginados(page, quantity);
        return ResponseEntity.ok(talhoes);
    }
}