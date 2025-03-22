package com.fatec.api.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.fatec.api.backend.model.Estado;
import com.fatec.api.backend.repository.EstadoRepository;

@SpringBootTest
@ActiveProfiles("test")
public class EstadoServiceTest {
    
    @Autowired
    private EstadoService estadoService;   

    @Autowired
    private EstadoRepository estadoRepository;

    @Test
    void deveRetornarComSucessoListaDeEstados() {
        Estado estado1 = new Estado();
        estado1.setNome("São Paulo");
        estado1.setSigla("SP");

        Estado estado2 = new Estado();
        estado2.setNome("Minas Gerais");
        estado2.setSigla("MG");

        estadoRepository.saveAll(List.of(estado1, estado2));

        List<Estado> resultado = estadoService.listarEstados();

        assertEquals(2, resultado.size());
        assertEquals("São Paulo", resultado.get(0).getNome());
        assertEquals("SP", resultado.get(0).getSigla());
        assertEquals("Minas Gerais", resultado.get(1).getNome());
        assertEquals("MG", resultado.get(1).getSigla());
    }

    @Test
    void deveFalharAoListarEstados() {
        estadoRepository.deleteAll();

        List<Estado> resultado = estadoService.listarEstados();

        assertEquals(0, resultado.size());
    }
}
