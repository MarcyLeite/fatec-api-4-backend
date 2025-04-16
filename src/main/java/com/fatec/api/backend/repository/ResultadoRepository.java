package com.fatec.api.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fatec.api.backend.model.Resultado;

@Repository
public interface ResultadoRepository extends JpaRepository<Resultado, Long> {}
