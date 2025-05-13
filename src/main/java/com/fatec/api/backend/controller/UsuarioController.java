package com.fatec.api.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fatec.api.backend.model.Usuario;
import com.fatec.api.backend.service.UsuarioService;

import org.springframework.web.bind.annotation.RequestBody; 

@RestController
@RequestMapping("/usuario")
@CrossOrigin(origins = "*")
public class UsuarioController {
    
    @Autowired
    private UsuarioService usuarioService;
    private Usuario.Role[] permissionList = new Usuario.Role[]{ Usuario.Role.Administrador };

    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrarUsuario(@RequestBody Usuario dados, @RequestParam String token) {
        if(!usuarioService.verifyAccess(permissionList, token)) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        Usuario usuarioSalvo = usuarioService.cadastrarUsuario(dados.getNome(), dados.getPassword(), dados.getEmail(), dados.getRole(), dados.getAtivo());
        return ResponseEntity.ok(usuarioSalvo);
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<?> editarUsuario(@PathVariable Long id, @RequestBody Usuario dados, @RequestParam String token) {
        if(!usuarioService.verifyAccess(permissionList, token)) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        return ResponseEntity.ok(usuarioService.editarUsuario(id, dados));
    }
    
    @GetMapping("/listar/{page}/{quantity}")
    public ResponseEntity<?> listarUsuariosPaginados(
            @PathVariable("page") int page,
            @PathVariable("quantity") int size,
            @RequestParam String token
    ) {
        if(!usuarioService.verifyAccess(permissionList, token)) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        
        Page<Usuario> usuarios = usuarioService.listarUsuariosPaginados(page, size);
        return ResponseEntity.ok(usuarios);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Usuario usuario) {
        String token = usuarioService.loginUsuario(usuario.getEmail(), usuario.getPassword());
        return ResponseEntity.ok(token);
    }

    @GetMapping("/verifyData/{token}")
    public ResponseEntity<Usuario> verify(@PathVariable String token) {
        return ResponseEntity.ok(usuarioService.verifyToken(token));
    }
}
