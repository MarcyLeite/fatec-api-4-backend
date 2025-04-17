package com.fatec.api.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fatec.api.backend.model.Missao;
import com.fatec.api.backend.model.Talhao;
import com.fatec.api.backend.repository.MissaoRepository;


@Service
public class MissaoService {

    @Autowired
    private MissaoRepository missaoRepository;

    private Missao saveMissao(Missao missao) {
        return missaoRepository.save(missao);
    }

    public Missao CreateMissao(List<Talhao> talhoes) {
        return saveMissao(new Missao(talhoes));
    }

    public Missao getMissaobyId(Long missao_id) {
        Optional<Missao>missaoOptional = missaoRepository.findById(missao_id);
        if (missaoOptional.isEmpty()) {
            throw new IllegalArgumentException("Não existe resultados vinculados a esse QA");
        }
        return missaoOptional.get();
    }    

    public Missao finalizeMissao(Missao missao) {
        missao.setStatus(Missao.Status.finalizada);
        return missaoRepository.save(missao);
    }
}
