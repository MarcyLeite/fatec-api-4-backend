package com.fatec.api.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fatec.api.backend.model.Talhao;

@Repository
public interface TalhaoRepository extends JpaRepository<Talhao, Long> {

    @Query(value = "SELECT ST_AsGeoJSON(t.rel_shape) FROM talhao t WHERE t.id = :talhao_id", nativeQuery = true)
    String findGeoJsonById(@Param("talhao_id") Long talhaoId);

    @Query("SELECT t FROM Talhao t WHERE t.fazenda.id = :fazendaId")
    List<Talhao> findTalhaoFazId(@Param("fazendaId") Long fazendaId);
}