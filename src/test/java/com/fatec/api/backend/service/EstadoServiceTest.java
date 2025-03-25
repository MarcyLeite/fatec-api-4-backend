package com.fatec.api.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fatec.api.backend.DTO.EstadoDTO;
import com.fatec.api.backend.model.Estado;
import com.fatec.api.backend.repository.EstadoRepository;

public class EstadoServiceTest {
    
    @InjectMocks
    private EstadoService estadoService;   

    @Mock
    private EstadoRepository estadoRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveRetornarComSucessoListaDeEstados() {
        Estado estado1 = new Estado();
        estado1.setId(1L);
        estado1.setNome("São Paulo");

        Estado estado2 = new Estado();
        estado2.setId(2L);
        estado2.setNome("Minas Gerais");

        when(estadoRepository.findEstadoByIdAndName()).thenReturn(
            List.of(new EstadoDTO(1L, "São Paulo"), new EstadoDTO(2L, "Minas Gerais"))
        );
        
        List<EstadoDTO> resultado = estadoService.listarEstados();

        assertEquals(2, resultado.size());
        assertEquals(1L, resultado.get(0).id());
        assertEquals("São Paulo", resultado.get(0).nome());
        assertEquals(2L, resultado.get(1).id());
        assertEquals("Minas Gerais", resultado.get(1).nome());

        verify(estadoRepository, times(1)).findEstadoByIdAndName();
    }

    @Test
    void deveFalharAoListarEstados() {
       when(estadoRepository.findEstadoByIdAndName()).thenReturn(List.of());

        List<EstadoDTO> resultado = estadoService.listarEstados();

        assertEquals(0, resultado.size());

        verify(estadoRepository, times(1)).findEstadoByIdAndName();

    }
}
