package com.fatec.api.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.fatec.api.backend.model.Fazenda;
import com.fatec.api.backend.model.Talhao;
import com.fatec.api.backend.repository.TalhaoRepository;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class TalhaoServiceTest {

    @Autowired
    private TalhaoService talhaoService;

    @MockitoBean
    private TalhaoRepository talhaoRepository;

    @Mock
    private Fazenda fazenda;

    @Test
    void deveriaChamarListaDeTalhoesPaginados() {
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);

        Talhao talhao1 = new Talhao();
        talhao1.setId(1L);
        talhao1.setNome("Talhão 1");
        talhao1.setCultura("Soja");
        talhao1.setFazenda(fazenda);

        Talhao talhao2 = new Talhao();
        talhao2.setId(2L);
        talhao2.setNome("Talhão 2");
        talhao2.setCultura("Milho");
        talhao2.setFazenda(fazenda);

        List<Talhao> listaTalhoes = new ArrayList<>();
        listaTalhoes.add(talhao1);
        listaTalhoes.add(talhao2);

        Page<Talhao> talhoesPage = new PageImpl<>(listaTalhoes, pageable, listaTalhoes.size());

        when(talhaoRepository.findAll(pageable)).thenReturn(talhoesPage);

        Page<Talhao> resultado = talhaoService.listarTalhoesPaginados(page, size);

        assertEquals(2, resultado.getTotalElements());
        assertEquals("Talhão 1", resultado.getContent().get(0).getNome());
        assertEquals("Talhão 2", resultado.getContent().get(1).getNome());
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