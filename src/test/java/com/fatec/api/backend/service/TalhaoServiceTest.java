package com.fatec.api.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.fatec.api.backend.model.Fazenda;
import com.fatec.api.backend.model.Usuario;
import com.fatec.api.backend.model.Usuario.Role;
import com.fatec.api.backend.repository.FazendaRepository;
import com.fatec.api.backend.repository.UsuarioRepository;

@SpringBootTest
public class TalhaoServiceTest {
    
    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private TalhaoService talhaoService;

    @Mock
    private FazendaService fazendaService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private FazendaRepository fazendaRepository;

    @Test
    void deveListarTodosOsAnalistas() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2012, Calendar.DECEMBER, 12); 
        Date date = calendar.getTime();
        
        Usuario usuario1 = new Usuario();
        usuario1.setRole(Role.Analista);
        usuario1.setNome("Gary");
        usuario1.setEmail("gary@gary.com");
        usuario1.setCreatedAt(date);

        Usuario usuario2 = new Usuario();
        usuario2.setRole(Role.Analista);
        usuario2.setNome("Lary");
        usuario2.setEmail("lary@lary.com");
        usuario2.setCreatedAt(date);
        
        when(usuarioService.listarAnalistas()).thenReturn(Arrays.asList(usuario1, usuario2));

        List<Usuario> resultado = usuarioService.listarAnalistas();

        assertEquals(2, resultado.size());
        assertEquals(Role.Analista, resultado.get(0).getRole());
        assertEquals("Gary", resultado.get(0).getNome());
        assertEquals("gary@gary.com", resultado.get(0).getEmail());
        assertEquals(date, resultado.get(1).getCreatedAt());
        
        assertEquals(Role.Analista, resultado.get(1).getRole());
        assertEquals("Lary", resultado.get(1).getNome());
        assertEquals("lary@lary.com", resultado.get(1).getEmail());
        assertEquals(date, resultado.get(1).getCreatedAt());
    }

    @Test
    void deveListarOMapComIdENomeDeFazendaEAnalista() {
        Usuario usuario1 = new Usuario();
        usuario1.setId(1L);
        usuario1.setRole(Role.Analista);
        usuario1.setNome("Gary");

        Usuario usuario2 = new Usuario();
        usuario2.setId(2L);
        usuario2.setRole(Role.Analista);
        usuario2.setNome("Lary");

        Fazenda fazenda1 = new Fazenda();
        fazenda1.setId(1L);
        fazenda1.setNome("Fazenda1");

        Fazenda fazenda2 = new Fazenda();
        fazenda2.setId(2L);
        fazenda2.setNome("Fazenda2");

        when(usuarioService.listarAnalistas()).thenReturn(Arrays.asList(usuario1, usuario2));
        when(fazendaService.listarTodasFazendas()).thenReturn(Arrays.asList(fazenda1, fazenda2));

        Map<String, Object> resultado = talhaoService.listarFazendaEAnalistas(); 

        // Validando a saída
        List<Map<String, Object>> analistas = (List<Map<String, Object>>) resultado.get("analistas");
        List<Map<String, Object>> fazendas = (List<Map<String, Object>>) resultado.get("fazendas");

        assertEquals(2, analistas.size());
        assertEquals(1L, analistas.get(0).get("id"));
        assertEquals("Gary", analistas.get(0).get("nome"));
        assertEquals(2L, analistas.get(1).get("id"));
        assertEquals("Lary", analistas.get(1).get("nome"));

        assertEquals(2, fazendas.size());
        assertEquals(1L, fazendas.get(0).get("id"));
        assertEquals("Fazenda1", fazendas.get(0).get("nome"));
        assertEquals(2L, fazendas.get(1).get("id"));
        assertEquals("Fazenda2", fazendas.get(1).get("nome"));
    }

    @Test
    void deveFalharAoListarTodosOsAnalistas() {
    
        when(usuarioRepository.findByRole(Role.Analista)).thenReturn(List.of());
                
        List<Usuario> resultado = usuarioService.listarAnalistas();

        assertEquals(0, resultado.size(), "A lista de analistas deveria estar vazia, mas contém elementos.");
    }

    @Test
    void deveFalharAoListarODicionarioDeAnalistaEFazenda() {
        Usuario usuarioInvalido = new Usuario();
        usuarioInvalido.setId(1L);
        usuarioInvalido.setRole(Role.Administrador); 
        usuarioInvalido.setNome("Admin");

        Fazenda fazendaInvalida = new Fazenda();
        fazendaInvalida.setId(1L);
        fazendaInvalida.setNome("Fazenda Inválida");

        when(usuarioRepository.findByRole(Role.Analista)).thenReturn(List.of()); 

        when(fazendaService.listarTodasFazendas()).thenReturn(List.of(fazendaInvalida));

        Map<String, Object> resultado = new HashMap<>();
        resultado.put("analistas", usuarioService.listarAnalistas().stream()
                .map(usuario -> {
                    Map<String, Object> analistaMap = new HashMap<>();
                    analistaMap.put("id", usuario.getId());
                    analistaMap.put("nome", usuario.getNome());
                    return analistaMap;
                })
                .collect(Collectors.toList()));

        resultado.put("fazendas", fazendaService.listarTodasFazendas().stream()
                .map(fazenda -> {
                    Map<String, Object> fazendaMap = new HashMap<>();
                    fazendaMap.put("id", fazenda.getId());
                    fazendaMap.put("nome", fazenda.getNome());
                    return fazendaMap;
                })
                .collect(Collectors.toList()));

        List<Map<String, Object>> analistas = (List<Map<String, Object>>) resultado.get("analistas");
        List<Map<String, Object>> fazendas = (List<Map<String, Object>>) resultado.get("fazendas");

        assertEquals(0, analistas.size(), "A lista de analistas deveria estar vazia, mas contém elementos.");

        assertEquals(1, fazendas.size(), "A lista de fazendas deveria conter 1 elemento.");
        assertEquals(1L, fazendas.get(0).get("id"), "O ID da fazenda deveria ser 1.");
        assertEquals("Fazenda Inválida", fazendas.get(0).get("nome"), "O nome da fazenda deveria ser 'Fazenda Inválida'.");
    }
}
