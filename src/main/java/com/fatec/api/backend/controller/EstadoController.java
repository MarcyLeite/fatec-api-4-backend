package com.fatec.api.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fatec.api.backend.model.Estado;
import com.fatec.api.backend.service.EstadoService;

@RestController
@RequestMapping("estado")
@CrossOrigin(origins = "*")
public class EstadoController {
    
    private EstadoService estadoService;

    public EstadoController(EstadoService estadoService) {
        this.estadoService = estadoService;
    }

    @GetMapping("/listar")
    private ResponseEntity<List<Estado>> listarEstados() {
        List<Estado> estados = estadoService.listarEstados();
        return ResponseEntity.ok(estados);
    }
}
