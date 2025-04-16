package com.fatec.api.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fatec.api.backend.model.Missao;

@Repository
public interface MissaoRepository extends JpaRepository<Missao, Long> {
    @Query(value = "SELECT m.* FROM missao m "
    + "JOIN mtm_missoes_talhoes mt ON m.id = mt.mis_id "
    + "WHERE mt.tal_id = :talhaoId", nativeQuery = true)
List<Missao> findByTalhaoIdNative(@Param("talhaoId") Long talhaoId);
}