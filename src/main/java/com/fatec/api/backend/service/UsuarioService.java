package com.fatec.api.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fatec.api.backend.model.Usuario;
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
}
