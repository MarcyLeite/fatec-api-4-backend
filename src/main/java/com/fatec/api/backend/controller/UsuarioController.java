package com.fatec.api.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fatec.api.backend.model.Usuario;
import com.fatec.api.backend.service.UsuarioService;

import org.springframework.web.bind.annotation.RequestBody; 

@RestController
@RequestMapping("/usuario")
@CrossOrigin(origins = "*")
public class UsuarioController {
    
    private UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<Usuario> cadastrarUsuario(@RequestBody Usuario dados) {
        Usuario usuarioSalvo = usuarioService.cadastrarUsuario(dados.getNome(), dados.getEmail(), dados.getRole());
        return ResponseEntity.ok(usuarioSalvo);
    }
}
