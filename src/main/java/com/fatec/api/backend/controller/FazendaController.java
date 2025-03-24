package com.fatec.api.backend.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fatec.api.backend.DTO.EstadoDTO;
import com.fatec.api.backend.model.Fazenda;
import com.fatec.api.backend.service.EstadoService;
import com.fatec.api.backend.service.FazendaService;

@RestController
@RequestMapping("/fazenda")
@CrossOrigin(origins = "*")
public class FazendaController {
    
    private FazendaService fazendaService;

    private EstadoService estadoService;

    public FazendaController(FazendaService fazendaService, EstadoService estadoService) {
        this.fazendaService = fazendaService;
        this.estadoService = estadoService;
    }

    @GetMapping("listar/{page}/{quantity}")
    public ResponseEntity<Page<Fazenda>> listarFazendasPaginadas(
            @RequestParam(defaultValue = "0") int page, 
            @RequestParam(defaultValue = "10") int size) {
        Page<Fazenda> fazendas = fazendaService.listarFazendasPaginadas(page, size);
        return ResponseEntity.ok(fazendas);
    }

    @GetMapping("/editar")
    public ResponseEntity<List<EstadoDTO>> listarEstados() {
        return ResponseEntity.ok(estadoService.listarEstados());
    }
}
