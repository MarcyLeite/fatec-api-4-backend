package com.fatec.api.backend.repository;

import org.springframework.stereotype.Repository;

import com.fatec.api.backend.model.Cidade;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface CidadeRepository extends JpaRepository<Cidade, Long> {

}
