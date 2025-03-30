package com.fatec.api.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.fatec.api.backend.model.Cidade;
import com.fatec.api.backend.model.Estado;
import com.fatec.api.backend.model.Fazenda;
import com.fatec.api.backend.model.Usuario;
import com.fatec.api.backend.model.Usuario.Role;
import com.fatec.api.backend.repository.CidadeRepository;
import com.fatec.api.backend.repository.EstadoRepository;
import com.fatec.api.backend.repository.FazendaRepository;
import com.fatec.api.backend.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class FazendaServiceTest {
 
    @InjectMocks
    private FazendaService fazendaService;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private FazendaRepository fazendaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private EstadoRepository estadoRepository;

    @Mock
    private CidadeRepository cidadeRepository;

    @Mock
    private Cidade cidade;

    @Test
    void deveriaChamarListaDeFazendasPaginadas() {
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);

        Fazenda fazenda1 = new Fazenda();
        fazenda1.setId(1L);
        fazenda1.setNome("Fazendinha");
        fazenda1.setArea(30F);
        fazenda1.setProdAnual(30F);
        fazenda1.setTipoSolo("Arenoso");
        fazenda1.setCidade(cidade);

        Fazenda fazenda2 = new Fazenda();
        fazenda2.setId(2L);
        fazenda2.setNome("Fazendona");
        fazenda2.setArea(70F);
        fazenda2.setProdAnual(50F);
        fazenda2.setTipoSolo("Argiloso");
        fazenda2.setCidade(cidade);

        List<Fazenda> listaFazendas = new ArrayList<>();
        listaFazendas.add(fazenda1);
        listaFazendas.add(fazenda2);

        Page<Fazenda> fazendasPage = new PageImpl<>(listaFazendas, pageable, listaFazendas.size());

        when(fazendaRepository.findAll(pageable)).thenReturn(fazendasPage);

        Page<Fazenda> resultado = fazendaService.listarFazendasPaginadas(page, size);

        assertEquals(2, resultado.getTotalElements());
        assertEquals("Fazendinha", resultado.getContent().get(0).getNome());
        assertEquals("Fazendona", resultado.getContent().get(1).getNome());

    }

    @Test
    void deveriaFalharAoChamarListaDeFazendas() {
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        
        when(fazendaRepository.findAll(pageable)).thenThrow(new RuntimeException("Erro ao buscar fazendas"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            fazendaService.listarFazendasPaginadas(page, size);
        });

        assertEquals("Erro ao buscar fazendas", exception.getMessage());
    }

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

        Map<String, Object> resultado = fazendaService.listarFazendaEAnalistas(); 

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
    
        when(usuarioService.listarAnalistas()).thenReturn(List.of());
                
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

        when(usuarioService.listarAnalistas()).thenReturn(List.of()); 

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
    void deveCadastrarFazendaQuandoTemCidade() {
        Fazenda fazenda1 = new Fazenda();
        fazenda1.setId(1L);
        fazenda1.setNome("Fazendinha");
        fazenda1.setArea(30F);
        fazenda1.setProdAnual(30F);
        fazenda1.setTipoSolo("Arenoso");
        fazenda1.setCidade(cidade);

        when(cidadeRepository.findById(anyLong())).thenReturn(Optional.of(cidade));

        when(fazendaRepository.save(any(Fazenda.class))).thenReturn(fazenda1);

        Fazenda resultado = fazendaService.cadastrarFazenda(fazenda1);

        assertEquals(1, resultado.getId());
        assertEquals("Fazendinha", resultado.getNome());
        assertEquals(30F, resultado.getArea());
        assertEquals(30F, resultado.getProdAnual());
        assertEquals("Arenoso", resultado.getTipoSolo());
        assertEquals(cidade, resultado.getCidade());

        verify(cidadeRepository, times(1)).findById(anyLong());
        verify(fazendaRepository, times(1)).save(fazenda1);
    }   

    @Test
    void deveCadastrarFazendoQuandoNaoTemCidade() {
        Fazenda fazenda1 = new Fazenda();
        fazenda1.setNome("Fazendinha");
        fazenda1.setArea(30F);
        fazenda1.setProdAnual(30F);
        fazenda1.setTipoSolo("Arenoso");
    
        Estado estado = new Estado();
        estado.setId(1L);
        estado.setNome("Acre");
    
        Cidade cidade = new Cidade();
        cidade.setEstado(estado); 
        fazenda1.setCidade(cidade);
    
        Cidade novaCidade = new Cidade();
        novaCidade.setId(1L);
        novaCidade.setNome("Cidade né");
        novaCidade.setEstado(estado);
    
        when(estadoRepository.findById(1L)).thenReturn(Optional.of(estado)); 
    
        when(cidadeRepository.save(any(Cidade.class))).thenReturn(novaCidade);
    
        when(fazendaRepository.save(any(Fazenda.class))).thenAnswer(invocation -> {
            Fazenda f = invocation.getArgument(0);
            f.setId(1L); 
            f.setCidade(novaCidade); 
            return f;
        });
    
        Fazenda resultado = fazendaService.cadastrarFazenda(fazenda1);
    
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId()); 
        assertEquals("Fazendinha", resultado.getNome());
        assertEquals(30F, resultado.getArea());
        assertEquals(30F, resultado.getProdAnual());
        assertEquals("Arenoso", resultado.getTipoSolo());
        assertNotNull(resultado.getCidade());
        assertEquals(1L, resultado.getCidade().getId());
        assertEquals("Cidade né", resultado.getCidade().getNome());
        assertEquals(estado, resultado.getCidade().getEstado());
    
        verify(estadoRepository, times(1)).findById(1L); 
        verify(cidadeRepository, times(1)).save(any(Cidade.class)); 
        verify(fazendaRepository, times(1)).save(any(Fazenda.class));
    }

    @Test
    void deveFalharAoCadastrarFazendaComCidade() {
        Fazenda fazenda1 = new Fazenda();
        fazenda1.setId(1L);
        fazenda1.setNome("Fazendinha");
        fazenda1.setArea(30F);
        fazenda1.setProdAnual(30F);
        fazenda1.setTipoSolo("Arenoso");
        fazenda1.setCidade(cidade);

        when(cidadeRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            fazendaService.cadastrarFazenda(fazenda1);
        });

        verify(cidadeRepository, times(1)).findById(anyLong());
        verify(fazendaRepository, times(0)).save(any(Fazenda.class));
    }

    @Test
    void deveFalharAoCadastrarFazendaSemCidade() {
        Fazenda fazenda1 = new Fazenda();
        fazenda1.setNome("Fazendinha");
        fazenda1.setArea(30F);
        fazenda1.setProdAnual(30F);
        fazenda1.setTipoSolo("Arenoso");
        
        Cidade cidade = new Cidade();
        cidade.setId(null);
        fazenda1.setCidade(cidade);
    
        assertThrows(NullPointerException.class, () -> {
            fazendaService.cadastrarFazenda(fazenda1);
        });
        
        verify(fazendaRepository, times(0)).save(any(Fazenda.class));
    }
}

