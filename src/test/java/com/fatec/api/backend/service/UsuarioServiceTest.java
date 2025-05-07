package com.fatec.api.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.fatec.api.backend.model.Usuario;
import com.fatec.api.backend.model.Usuario.Role;
import com.fatec.api.backend.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {
    
    @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Test
    void deveCadastrarUsuarioCerto() {
        Usuario usuario1 = new Usuario();
        usuario1.setId(1L);
        usuario1.setNome("Larry");
        usuario1.setEmail("eolarry@gmail.com");
        usuario1.setRole(Role.Administrador);
        usuario1.setCreatedAt(new Date());

        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario1);

        Usuario usuarioSalvo = usuarioService.cadastrarUsuario("Larry", "eolarry@gmail.com", Role.Administrador);

        assertEquals(usuario1.getNome(), usuarioSalvo.getNome());
        assertEquals(usuario1.getEmail(), usuarioSalvo.getEmail());
        assertEquals(usuario1.getRole(), usuarioSalvo.getRole());
    }   

    @Test
    void deveFalharAoCadastrarUsuario() {
        when(usuarioRepository.save(any(Usuario.class))).thenThrow(new RuntimeException("Erro ao salvar usuário"));

        assertThrows(RuntimeException.class, () -> {
            usuarioService.cadastrarUsuario("Erro", "erro@gmail", Role.Consultor);
        });
    }

    @Test
    void deveListarTodosOsUsuariosPaginados() {
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);

        Usuario usuario1 = new Usuario();
        usuario1.setId(1L);
        usuario1.setNome("Garry");
        usuario1.setEmail("garry@email.com");
        usuario1.setRole(Role.Administrador);

        Usuario usuario2 = new Usuario();
        usuario2.setId(2L);
        usuario2.setNome("Marry");
        usuario2.setEmail("marry@email.com");
        usuario2.setRole(Role.Analista);

        List<Usuario> listarUsuarios = new ArrayList<>();
        listarUsuarios.add(usuario1);
        listarUsuarios.add(usuario2);

        Page<Usuario> usuariosPage = new PageImpl<>(listarUsuarios, pageable, listarUsuarios.size());

        when(usuarioRepository.findAll(pageable)).thenReturn(usuariosPage);
        Page<Usuario> resultado = usuarioService.listarUsuariosPaginados(page, size);

        assertEquals(2, resultado.getTotalElements());
        assertEquals("Garry", resultado.getContent().get(0).getNome());
        assertEquals("Marry", resultado.getContent().get(1).getNome());
    }

    @Test
    void deveFalharAoListarUsuariosPaginados() {
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);

        when(usuarioRepository.findAll(pageable)).thenThrow(new RuntimeException("Erro ao buscar usuários"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.listarUsuariosPaginados(page, size);
        });

        assertEquals("Erro ao buscar usuários", exception.getMessage());
    }
}
