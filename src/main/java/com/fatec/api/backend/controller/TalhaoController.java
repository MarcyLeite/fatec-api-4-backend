package com.fatec.api.backend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fatec.api.backend.model.Fazenda;
import com.fatec.api.backend.model.Usuario;
import com.fatec.api.backend.service.FazendaService;
import com.fatec.api.backend.service.UsuarioService;

@RequestMapping("talhao")
@RestController
@CrossOrigin(origins = "*")
public class TalhaoController {
    
    private UsuarioService usuarioService;

    private FazendaService fazendaService;

    public TalhaoController(UsuarioService usuarioService, FazendaService fazendaService) {
        this.usuarioService = usuarioService;
        this.fazendaService = fazendaService;
    }

    @GetMapping("/editar")
    public ResponseEntity<Map<String, Object>> listarFazendasEAnalistas() {
        List<Usuario> analistas = usuarioService.listarAnalistas();
        List<Fazenda> fazendas = fazendaService.listarTodasFazendas();
        
        List<Map<String, Object>> fazendasMapeadas = fazendas.stream()
            .map(fazenda -> {
                Map<String, Object> fazendaMap = new HashMap<>();
                fazendaMap.put("id", fazenda.getId());
                fazendaMap.put("nome", fazenda.getNome());
                return fazendaMap;
            })
            .collect(Collectors.toList());

        List<Map<String, Object>> analistasMapeados = analistas.stream()
            .map(analista -> {
                Map<String, Object> analistaMap = new HashMap<>();
                analistaMap.put("id", analista.getId());
                analistaMap.put("nome", analista.getNome());
                return analistaMap;
            })
            .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("fazendas", fazendasMapeadas);
        response.put("analistas", analistasMapeados);

        return ResponseEntity.ok(response);
    }
}
