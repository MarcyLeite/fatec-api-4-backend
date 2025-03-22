package com.fatec.api.backend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fatec.api.backend.model.Fazenda;
import com.fatec.api.backend.repository.FazendaRepository;

@Service
public class FazendaService {
    
    private FazendaRepository fazendaRepository;

    public FazendaService(FazendaRepository fazendaRepository) {
        this.fazendaRepository = fazendaRepository;
    }

    public Page<Fazenda> listarFazendasPaginadas(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return fazendaRepository.findAll(pageable);
    }
}
