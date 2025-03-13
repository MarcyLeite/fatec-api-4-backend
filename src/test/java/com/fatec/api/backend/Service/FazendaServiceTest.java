package com.fatec.api.backend.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.fatec.api.backend.Entity.Cidade;
import com.fatec.api.backend.Entity.Fazenda;
import com.fatec.api.backend.Repository.FazendaRepository;

@ExtendWith(MockitoExtension.class)
public class FazendaServiceTest {
 
    @InjectMocks
    private FazendaService fazendaService;

    @Mock
    private FazendaRepository fazendaRepository;

    @Mock
    private Cidade cidade;

    @Test
    void deveriaChamarListaDeFazendasPaginadas() {
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);

        Fazenda fazenda1 = new Fazenda();
        fazenda1.setFazId(1L);
        fazenda1.setFazNome("Fazendinha");
        fazenda1.setFazArea(50.0);
        fazenda1.setFazProd(30.0);
        fazenda1.setFazTipoSolo("Arenoso");
        fazenda1.setCidade(cidade);

        Fazenda fazenda2 = new Fazenda();
        fazenda2.setFazId(2L);
        fazenda2.setFazNome("Fazendona");
        fazenda2.setFazArea(70.0);
        fazenda2.setFazProd(50.0);
        fazenda2.setFazTipoSolo("Argiloso");
        fazenda2.setCidade(cidade);

        List<Fazenda> listaFazendas = new ArrayList<>();
        listaFazendas.add(fazenda1);
        listaFazendas.add(fazenda2);

        Page<Fazenda> fazendasPage = new PageImpl<>(listaFazendas, pageable, listaFazendas.size());

        when(fazendaRepository.findAll(pageable)).thenReturn(fazendasPage);

        Page<Fazenda> resultado = fazendaService.listarFazendasPaginadas(page, size);

        assertEquals(2, resultado.getTotalElements());
        assertEquals("Fazendinha", resultado.getContent().get(0).getFazNome());
        assertEquals("Fazendona", resultado.getContent().get(1).getFazNome());

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
}
