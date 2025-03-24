package com.fatec.api.backend.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fatec.api.backend.model.Fazenda;
import com.fatec.api.backend.model.Usuario;

@Service
public class TalhaoService {
    
    private FazendaService fazendaService;

    private UsuarioService usuarioService;

    public TalhaoService(FazendaService fazendaService, UsuarioService usuarioService) {
        this.fazendaService = fazendaService;
        this.usuarioService = usuarioService;
    }

    public Map<String, Object> listarFazendaEAnalistas() {
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

        return response;
    }

}
