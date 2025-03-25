package com.fatec.api.backend.Repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;

import com.fatec.api.backend.DTO.EstadoDTO;
import com.fatec.api.backend.model.Estado;
import com.fatec.api.backend.repository.EstadoRepository;
import com.fatec.api.backend.service.EstadoService;

@ExtendWith(MockitoExtension.class)
public class EstadoRepositoryTest {
    
    @Mock
    private EstadoRepository estadoRepository;

    @InjectMocks
    private EstadoService estadoService;

    @BeforeEach
    public void setup() {
        when(estadoRepository.findEstadoByIdAndName()).thenReturn(List.of(
            new EstadoDTO(1L, "São Paulo"),
            new EstadoDTO(2L, "Minas Gerais")
        ));    
    }

    @Test
    void deveRealizarComSucessoAQuery() {
        Estado estado1 = new Estado();
        estado1.setId(1L);
        estado1.setNome("São Paulo");
        
        Estado estado2 = new Estado();
        estado2.setId(2L);
        estado2.setNome("Minas Gerais");

        List<EstadoDTO> estadosDTO = estadoService.listarEstados();

        assertEquals(2, estadosDTO.size());
        assertEquals(1L, estadosDTO.get(0).id());
        assertEquals("São Paulo", estadosDTO.get(0).nome());
        assertEquals(2L, estadosDTO.get(1).id());
        assertEquals("Minas Gerais", estadosDTO.get(1).nome());
    }

    @Test
    void deveFalharAoRealizarAQuery() {

        when(estadoRepository.findEstadoByIdAndName()).thenThrow(EmptyResultDataAccessException.class);

        assertThrows(EmptyResultDataAccessException.class, () -> estadoService.listarEstados());
    }
}
