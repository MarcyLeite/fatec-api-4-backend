package com.fatec.api.backend.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.fatec.api.backend.model.Cidade;
import com.fatec.api.backend.model.Fazenda;
import com.fatec.api.backend.Repository.FazendaRepository;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class FazendaServiceTest {
 
    @Autowired
    private FazendaService fazendaService;

    @MockitoBean
    private FazendaRepository fazendaRepository;

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
}
