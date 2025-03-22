package com.fatec.api.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fatec.api.backend.model.Fazenda;
import com.fatec.api.backend.repository.FazendaRepository;

@Service
public class FazendaService {
    
    private FazendaRepository fazendaRepository;

    @Autowired
    public FazendaService(FazendaRepository fazendaRepository) {
        this.fazendaRepository = fazendaRepository;
    }

    public Page<Fazenda> listarFazendasPaginadas(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return fazendaRepository.findAll(pageable);
    }

    public List<Fazenda> listarTodasFazendas() {
        return fazendaRepository.findAll();
    }
}
