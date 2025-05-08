package com.fatec.api.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    void deveEditarUsuarioComSucesso() {
        Usuario usuarioExiste = new Usuario();
        usuarioExiste.setNome("Harry");
        usuarioExiste.setEmail("harry@email.com");
        usuarioExiste.setRole(Role.Consultor);
        usuarioExiste.setAtivo(true);

        Usuario usuarioAtualizado = new Usuario();
        usuarioAtualizado.setNome("Harry POTTAR");
        usuarioAtualizado.setEmail("potter@email.com");
        usuarioAtualizado.setRole(Role.Administrador);
        usuarioAtualizado.setAtivo(false);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioExiste));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Usuario resultado = usuarioService.editarUsuario(1L, usuarioAtualizado);

        assertEquals("Harry POTTAR", resultado.getNome());
        assertEquals("potter@email.com", resultado.getEmail());
        assertEquals(Role.Administrador, resultado.getRole());        

    }

    @Test
    void deveFalharAoEditarUsuario() {
        Usuario usuarioExiste = new Usuario();
        usuarioExiste.setNome("Harry");
        usuarioExiste.setEmail("harry@email.com");
        usuarioExiste.setRole(Role.Consultor);
        usuarioExiste.setAtivo(true);

        Usuario usuarioAtualizado = new Usuario();
        usuarioAtualizado.setNome("Harry POTTAR");
        usuarioAtualizado.setEmail("potter@email.com");
        usuarioAtualizado.setRole(Role.Administrador);
        usuarioAtualizado.setAtivo(false);

        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.editarUsuario(999L, usuarioAtualizado);
        });

        assertEquals("Usuário de Id 999não encontrado", exception.getMessage());
    }
}
