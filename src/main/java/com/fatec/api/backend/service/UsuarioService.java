package com.fatec.api.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fatec.api.backend.model.Usuario;
import com.fatec.api.backend.model.Usuario.Role;
import com.fatec.api.backend.repository.UsuarioRepository;

@Service
public class UsuarioService {
    
    private UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> listarAnalistas() {
        return usuarioRepository.findByRole(Usuario.Role.Analista);
    }

    public Usuario cadastrarUsuario(String nome, String email, Role role) {
        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(nome);
        novoUsuario.setEmail(email);
        novoUsuario.setRole(role);
        Usuario usarioSalvo = usuarioRepository.save(novoUsuario);
        return usarioSalvo;
    }
}
