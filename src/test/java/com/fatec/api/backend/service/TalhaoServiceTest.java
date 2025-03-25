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
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.fatec.api.backend.geojson.TalhaoGeoDTO;
import com.fatec.api.backend.model.Talhao;
import com.fatec.api.backend.repository.TalhaoRepository;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class TalhaoServiceTest {
    
    @Mock
    private UsuarioService usuarioService;

    @Mock
    private TalhaoRepository talhaoRepository;

    @Mock
    private Fazenda fazenda;

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
    @Test
    void deveriaChamarListaDeTalhoesPaginados() {
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        
        Talhao talhao1 = new Talhao();
        talhao1.setId(1L);
        talhao1.setNome("Talhão 1");
        talhao1.setCultura("Soja");
        talhao1.setArea(100.0f);
        talhao1.setFazenda(fazenda);

        Talhao talhao2 = new Talhao();
        talhao2.setId(2L);
        talhao2.setNome("Talhão 2");
        talhao2.setCultura("Milho");
        talhao2.setArea(150.0f);
        talhao2.setFazenda(fazenda);
        
        List<Talhao> listaTalhoes = new ArrayList<>();
        listaTalhoes.add(talhao1);
        listaTalhoes.add(talhao2);
        
        Page<Talhao> talhoesPage = new PageImpl<>(listaTalhoes, pageable, listaTalhoes.size());
        
        when(talhaoRepository.findAll(pageable)).thenReturn(talhoesPage);
        
        Page<TalhaoGeoDTO> resultado = talhaoService.listarTalhoesPaginados(page, size);
        
        assertEquals(2, resultado.getTotalElements());
        assertEquals("Talhão 1", resultado.getContent().get(0).getNome());
        assertEquals("Soja", resultado.getContent().get(0).getCultura());
        assertEquals("Talhão 2", resultado.getContent().get(1).getNome());
        assertEquals("Milho", resultado.getContent().get(1).getCultura());
    }
    
    @Test
    void deveriaFalharAoChamarListaDeTalhoes() {
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        
        when(talhaoRepository.findAll(pageable)).thenThrow(new RuntimeException("Erro ao buscar talhões"));
        
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            talhaoService.listarTalhoesPaginados(page, size);
        });
        
        assertEquals("Erro ao buscar talhões", exception.getMessage());
    }
}