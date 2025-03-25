package com.fatec.api.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fatec.api.backend.DTO.EstadoDTO;
import com.fatec.api.backend.repository.EstadoRepository;

@Service
public class EstadoService {
    
    private EstadoRepository estadoRepository;

    public EstadoService(EstadoRepository estadoRepository) {
        this.estadoRepository = estadoRepository;
    }

    public List<EstadoDTO> listarEstados() {
        return estadoRepository.findEstadoByIdAndName();
    }
}
