package com.fatec.api.backend.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fatec.api.backend.DTO.MissaoDTO;
import com.fatec.api.backend.model.Missao;
import com.fatec.api.backend.repository.MissaoRepository;
import com.fatec.api.backend.service.MissaoService;

@RestController
@RequestMapping("/missao")
@CrossOrigin(origins = "*")
public class MissaoController {

    @Autowired
    MissaoRepository missaoRepository;

    @Autowired
    MissaoService missaoService;

    @GetMapping("/all")
    public ResponseEntity<List<MissaoDTO>> getAllMissoes() {
        List<Missao> missoes = missaoRepository.findAll();
        List<MissaoDTO> missoesDTO = missoes.stream()
            .map(MissaoDTO::new)
            .collect(Collectors.toList());
        return ResponseEntity.ok(missoesDTO);
    }

	@GetMapping("/{talhao_id}")
    public ResponseEntity<List<MissaoDTO>> getMissaoByTalhao(@PathVariable Long talhao_id) {

        List<Missao> missoes = missaoRepository.findByTalhaoIdNative(talhao_id);
        List<MissaoDTO> missoesDTO = new ArrayList<>();
        for(Missao m : missoes){
            MissaoDTO missaodto = new MissaoDTO(m);
            missoesDTO.add(missaodto);            
        }
        return ResponseEntity.ok(missoesDTO);
    }
    
}
