package com.fatec.api.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fatec.api.backend.DTO.EstadoDTO;
import com.fatec.api.backend.model.Estado;

@Repository
public interface EstadoRepository extends JpaRepository<Estado, Long> {
    
    @Query("SELECT new com.fatec.api.backend.DTO.EstadoDTO(e.id, e.nome) FROM Estado e")
    List<EstadoDTO> findEstadoByIdAndName();
}
