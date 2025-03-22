package com.fatec.api.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fatec.api.backend.model.Usuario;
import com.fatec.api.backend.service.UsuarioService;

@RequestMapping("analista")
@RestController
@CrossOrigin(origins = "*")
public class AnalistaController {
    
    private UsuarioService usuarioService;

    public AnalistaController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Usuario>> listarAnalistas() {
        List<Usuario> analistas = usuarioService.listarAnalistas();
        return ResponseEntity.ok(analistas);
    }
}
