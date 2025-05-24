package com.fatec.api.backend.repository;

import com.fatec.api.backend.model.Relatorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RelatorioRepository extends JpaRepository<Relatorio, Long> {
    List<Relatorio> findByUsuarioId(Long usuarioId);
    List<Relatorio> findByStartReviewBetween(LocalDateTime inicio, LocalDateTime fim);
    List<Relatorio> findByUsuarioIdAndStartReviewBetween(Long usuarioId, LocalDateTime inicio, LocalDateTime fim);
}
