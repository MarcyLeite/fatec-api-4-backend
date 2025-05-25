package com.fatec.api.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fatec.api.backend.model.Resultado;

@Repository
public interface ResultadoRepository extends JpaRepository<Resultado, Long> {

    @Query("SELECT r FROM Resultado r WHERE r.missao.id = :missionId and r.source = :typeResult")
    Resultado GetByMissionIdAndType(@Param("missionId") Long missionId, @Param("typeResult") Resultado.Source typeResult);
}