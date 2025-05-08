package com.fatec.api.backend.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

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
        novoUsuario.setCreatedAt(new Date());
        Usuario usarioSalvo = usuarioRepository.save(novoUsuario);
        return usarioSalvo;
    }

    public Usuario editarUsuario(Long id, Usuario dados) {
        Optional<Usuario> usuarioExiste = usuarioRepository.findById(id);

        if(usuarioExiste.isPresent()) {
            Usuario usuarioEditado = usuarioExiste.get();
            usuarioEditado.setNome(dados.getNome());
            usuarioEditado.setEmail(dados.getEmail());
            usuarioEditado.setRole(dados.getRole());
            usuarioEditado.setAtivo(dados.getAtivo());
            
            Usuario usuarioAtualizado = usuarioRepository.save(usuarioEditado);
            return usuarioAtualizado;
        }

        throw new RuntimeException("Usuário de Id " + id + "não encontrado");
    }
}
