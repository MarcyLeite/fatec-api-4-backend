package com.fatec.api.backend.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fatec.api.backend.service.TalhaoService;

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
}