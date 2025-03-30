package com.fatec.api.backend.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fatec.api.backend.model.Cidade;
import com.fatec.api.backend.model.Estado;
import com.fatec.api.backend.model.Fazenda;
import com.fatec.api.backend.model.Usuario;
import com.fatec.api.backend.repository.CidadeRepository;
import com.fatec.api.backend.repository.EstadoRepository;
import com.fatec.api.backend.repository.FazendaRepository;

@Service
public class FazendaService {
    
    private FazendaRepository fazendaRepository;

    @Autowired
    private CidadeRepository cidadeRepository;
    @Autowired
    private EstadoRepository estadoRepository;

    private UsuarioService usuarioService;

    @Autowired
    public FazendaService(FazendaRepository fazendaRepository, UsuarioService usuarioService) {
        this.fazendaRepository = fazendaRepository;
        this.usuarioService = usuarioService;
    }

    public Fazenda cadastrarFazenda(Fazenda fazenda) {
        Cidade cidade = null;
        if (fazenda.getCidade().getId() == null) {
            Cidade cidadeRaw = fazenda.getCidade();
            Estado estado = estadoRepository.findById(cidadeRaw.getEstado().getId()).get();
            cidadeRaw.setEstado(estado);
            cidade = cidadeRepository.save(cidadeRaw);
        } else {
            cidade = cidadeRepository.findById(fazenda.getCidade().getId()).get();
        }

        fazenda.setCidade(cidade);
            
        Fazenda savedFazenda = fazendaRepository.save(fazenda);
        return savedFazenda;
    }

    public Page<Fazenda> listarFazendasPaginadas(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return fazendaRepository.findAll(pageable);
    }

    public List<Fazenda> listarTodasFazendas() {
        return fazendaRepository.findAll();
    }

        public Map<String, Object> listarFazendaEAnalistas() {
        List<Usuario> analistas = usuarioService.listarAnalistas();
        List<Fazenda> fazendas = listarTodasFazendas();
        
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
