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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fatec.api.backend.DTO.MissaoDTO;
import com.fatec.api.backend.model.Missao;
import com.fatec.api.backend.model.Usuario;
import com.fatec.api.backend.repository.MissaoRepository;
import com.fatec.api.backend.service.MissaoService;
import com.fatec.api.backend.service.UsuarioService;

@RestController
@RequestMapping("/missao")
@CrossOrigin(origins = "*")
public class MissaoController {

    @Autowired
    MissaoRepository missaoRepository;

    @Autowired
    MissaoService missaoService;

    @Autowired
    UsuarioService usuarioService;

    private Usuario.Role[] allPermissionList = new Usuario.Role[]{ Usuario.Role.Administrador, Usuario.Role.Consultor, Usuario.Role.Analista };

    @GetMapping("/all")
    public ResponseEntity<?> getAllMissoes(@RequestParam String token) {
        if(!usuarioService.verifyAccess(allPermissionList, token)) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        List<Missao> missoes = missaoRepository.findAll();
        List<MissaoDTO> missoesDTO = missoes.stream()
            .map(MissaoDTO::new)
            .collect(Collectors.toList());
        return ResponseEntity.ok(missoesDTO);
    }

	@GetMapping("/{talhao_id}")
    public ResponseEntity<?> getMissaoByTalhao(@PathVariable Long talhao_id, @RequestParam String token) {
        if(!usuarioService.verifyAccess(allPermissionList, token)) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        List<Missao> missoes = missaoRepository.findByTalhaoIdNative(talhao_id);
        List<MissaoDTO> missoesDTO = new ArrayList<>();
        for(Missao m : missoes){
            MissaoDTO missaodto = new MissaoDTO(m);
            missoesDTO.add(missaodto);            
        }
        return ResponseEntity.ok(missoesDTO);
    }
    
}
