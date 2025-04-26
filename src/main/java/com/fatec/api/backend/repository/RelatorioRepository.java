package com.fatec.api.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fatec.api.backend.model.Relatorio;

@Repository
public interface RelatorioRepository extends JpaRepository<Relatorio, Long> {

}