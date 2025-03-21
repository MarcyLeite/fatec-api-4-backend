package com.fatec.api.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fatec.api.backend.model.Talhao;
import com.fatec.api.backend.repository.TalhaoRepository;

@Service
public class TalhaoService {

    private TalhaoRepository talhaoRepository;

    @Autowired
    public TalhaoService(TalhaoRepository talhaoRepository) {
        this.talhaoRepository = talhaoRepository;
    }

    public Page<Talhao> listarTalhoesPaginados(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return talhaoRepository.findAll(pageable);
    }
}