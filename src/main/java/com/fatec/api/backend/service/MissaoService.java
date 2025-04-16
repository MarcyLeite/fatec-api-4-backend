package com.fatec.api.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fatec.api.backend.model.Missao;
import com.fatec.api.backend.model.Talhao;
import com.fatec.api.backend.repository.MissaoRepository;


@Service
public class MissaoService {

    @Autowired
    private MissaoRepository missaoRepository;

    public Missao saveMissao(Missao missao) {
        return missaoRepository.save(missao);
    }

    public Missao CreateMissao(List<Talhao> talhoes) {
        return saveMissao(new Missao(talhoes));
    }
    
}
