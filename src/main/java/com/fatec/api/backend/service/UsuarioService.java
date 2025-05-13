package com.fatec.api.backend.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.fatec.api.backend.model.Usuario;
import com.fatec.api.backend.model.Usuario.Role;
import com.fatec.api.backend.repository.UsuarioRepository;

import jakarta.annotation.PostConstruct;

@Service
public class UsuarioService {
    
    private UsuarioRepository usuarioRepository;
    private JWTAuth jwtAuth;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UsuarioService(UsuarioRepository usuarioRepository, JWTAuth jwtAuth) {
        this.usuarioRepository = usuarioRepository;
        this.jwtAuth = jwtAuth;
    }

    public List<Usuario> listarAnalistas() {
        return usuarioRepository.findByRole(Usuario.Role.Analista);
    }

    public Usuario cadastrarUsuario(String nome, String senha, String email, Role role, Boolean ativo) {
        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(nome);
        novoUsuario.setEmail(email);
        novoUsuario.setRole(role);
        novoUsuario.setPassword(encoder.encode(senha));
        novoUsuario.setAtivo(true);
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
            usuarioEditado.setPassword(encoder.encode(dados.getPassword()));
            usuarioEditado.setAtivo(dados.getAtivo());
            
            Usuario usuarioAtualizado = usuarioRepository.save(usuarioEditado);
            return usuarioAtualizado;
        }

        throw new RuntimeException("Usuário de Id " + id + "não encontrado");

    }

    public String loginUsuario(String email, String senha) {
        Usuario usuario = usuarioRepository.findByEmail(email);
        if(usuario.equals(null)) {
            return null;
        }
        
        if(!encoder.matches(senha, usuario.getPassword())) {
            return null;
        }

        if(!usuario.getAtivo()) {
            return null;
        }

        return jwtAuth.createToken(usuario);
    }

    public Usuario verifyToken(String token) {
        try {
            return jwtAuth.extractUser(token);
        } catch (Exception e) {
            return null;
        }
    }

    public Page<Usuario> listarUsuariosPaginados(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return usuarioRepository.findAll(pageable);
    }

    public Boolean verifyAccess (Usuario.Role role, String token) {
        Usuario usuario = jwtAuth.extractUser(token);
        return usuario.getRole().equals(role);
    }
    
    public Boolean verifyAccess (Usuario.Role[] roleList, String token) {
        for (Usuario.Role role: roleList) {
            if (verifyAccess(role, token)) {
                return true;
            }
        }

        return false;
    }

    public Usuario createAdmin () {
        if (usuarioRepository.findByEmail("admin@email.com") != null) {
            return null;
        }
        return cadastrarUsuario("admin", "admin", "admin@email.com", Usuario.Role.Administrador, true);
    }

    @PostConstruct
    public void init() {
        createAdmin();
    }
}
