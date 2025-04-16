package com.fatec.api.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fatec.api.backend.model.Missao;

@Repository
public interface MissaoRepository extends JpaRepository<Missao, Long> {}
