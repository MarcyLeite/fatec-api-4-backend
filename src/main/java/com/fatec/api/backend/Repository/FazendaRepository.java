package com.fatec.api.backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fatec.api.backend.model.Fazenda;

@Repository
public interface FazendaRepository extends JpaRepository<Fazenda, Long> {

    
}