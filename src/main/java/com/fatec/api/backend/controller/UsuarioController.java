package com.fatec.api.backend.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @PutMapping("/editar/{id}")
    public ResponseEntity<Usuario> editarUsuario(@PathVariable Long id, @RequestBody Usuario dados) {
        return ResponseEntity.ok(usuarioService.editarUsuario(id, dados));
    }
    
    @GetMapping("/listar/{page}/{quantity}")
    public ResponseEntity<Page<Usuario>> listarUsuariosPaginados(
            @PathVariable("page") int page,
            @PathVariable("quantity") int size) {
        Page<Usuario> usuarios = usuarioService.listarUsuariosPaginados(page, size);
        return ResponseEntity.ok(usuarios);
    }
}
