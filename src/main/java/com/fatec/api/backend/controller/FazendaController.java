package com.fatec.api.backend.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fatec.api.backend.DTO.EstadoDTO;
import com.fatec.api.backend.model.Fazenda;
import com.fatec.api.backend.repository.FazendaRepository;
import com.fatec.api.backend.service.EstadoService;
import com.fatec.api.backend.service.FazendaService;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/fazenda")
@CrossOrigin(origins = "*")
public class FazendaController {
    
    private FazendaService fazendaService;

    private EstadoService estadoService;
    
    @Autowired
    private FazendaRepository fazendaRepository;

    
    public FazendaController(FazendaService fazendaService, EstadoService estadoService) {
        this.fazendaService = fazendaService;
        this.estadoService = estadoService;
    }

    @PostMapping()
    public ResponseEntity<?> createFazenda(@RequestBody Fazenda fazenda) {
        Fazenda savedFazenda = fazendaService.cadastrarFazenda(fazenda);
        return ResponseEntity.ok(savedFazenda);
    }

    @GetMapping("listar/{page}/{quantity}")
    public ResponseEntity<Page<Fazenda>> listarFazendasPaginadas(
            @RequestParam(defaultValue = "0") int page, 
            @RequestParam(defaultValue = "10") int size) {
        Page<Fazenda> fazendas = fazendaService.listarFazendasPaginadas(page, size);
        return ResponseEntity.ok(fazendas);
    }

    @GetMapping("listar")
    public ResponseEntity<List<Map<String, Object>>> listarFazendas() {
        List<Fazenda> fazendas = fazendaRepository.findAll();
        List<Map<String, Object>> fazendasSerializer = new ArrayList<>();

        for (Fazenda fazenda : fazendas) {
            Map<String, Object> i = new HashMap<>();
            i.put("id", fazenda.getId());
            i.put("nome", fazenda.getNome());
            fazendasSerializer.add(i);
        }
        return ResponseEntity.ok(fazendasSerializer);
    }

    @GetMapping("/editar")
    public ResponseEntity<List<EstadoDTO>> listarEstados() {
        return ResponseEntity.ok(estadoService.listarEstados());
    }

    @GetMapping("listar/analista")
    public ResponseEntity<Map<String, Object>> listarFazendasEAnalistas() {
        return ResponseEntity.ok(fazendaService.listarFazendaEAnalistas());
    }

}
