package com.fatec.api.backend.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fatec.api.backend.model.Talhao;
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
    public ResponseEntity<Page<Talhao>> listarTalhoesPaginados(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Talhao> talhoes = talhaoService.listarTalhoesPaginados(page, size);
        return ResponseEntity.ok(talhoes);
    }
}