package com.fatec.api.backend.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.fatec.api.backend.geojson.TalhaoGeoDTO;
import com.fatec.api.backend.model.Talhao;
import com.fatec.api.backend.repository.TalhaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fatec.api.backend.model.Fazenda;
import com.fatec.api.backend.model.Usuario;

@Service
public class TalhaoService {
    
    private FazendaService fazendaService;

    private UsuarioService usuarioService;

    private final TalhaoRepository talhaoRepository;

    public TalhaoService(FazendaService fazendaService, UsuarioService usuarioService, TalhaoRepository talhaoRepository) {
        this.fazendaService = fazendaService;
        this.usuarioService = usuarioService;
        this.talhaoRepository = talhaoRepository;
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

    
    @Autowired
    public TalhaoService(TalhaoRepository talhaoRepository) {
        this.talhaoRepository = talhaoRepository;
    }
    
    public Page<TalhaoGeoDTO> listarTalhoesPaginados(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Talhao> talhoesPage = talhaoRepository.findAll(pageable);
        
        List<TalhaoGeoDTO> talhoesDTO = talhoesPage.getContent().stream()
        .map(this::convertToGeoDTO)
        .collect(Collectors.toList());
        
        return new PageImpl<>(talhoesDTO, pageable, talhoesPage.getTotalElements());
    }
    
    private TalhaoGeoDTO convertToGeoDTO(Talhao talhao) {
        return new TalhaoGeoDTO(
            talhao.getId(),
                talhao.getNome(),
                talhao.getCultura(),
                talhao.getArea(),
                talhao.getShape()
                );
    }
}

