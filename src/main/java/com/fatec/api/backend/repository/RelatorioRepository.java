package com.fatec.api.backend.repository;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fatec.api.backend.DTO.AcuraciaDTO;
import com.fatec.api.backend.model.Relatorio;

@Repository
public interface RelatorioRepository extends JpaRepository<Relatorio, Long> {
    @Query(value = """
        WITH
        qa AS (
            SELECT ST_Union(d.dan_geom) AS geom
            FROM daninha d
            JOIN resultado r ON d.res_id = r.id
            WHERE r.mis_id = :missaoId AND r.res_source = 1
        ),
        ai AS (
            SELECT ST_Union(d.dan_geom) AS geom
            FROM daninha d
            JOIN resultado r ON d.res_id = r.id
            WHERE r.mis_id = :missaoId AND r.res_source = 0
        ),
        inter AS (
            SELECT ST_Intersection(qa.geom, ai.geom) AS geom
            FROM qa, ai
        )
        SELECT
            (ST_Area(inter.geom) / NULLIF(ST_Area(qa.geom), 0)) * 100 AS concordancia,
            ((ST_Area(qa.geom) - ST_Area(inter.geom)) / NULLIF(ST_Area(qa.geom), 0)) * 100 AS falsoNegativo,
            ((ST_Area(ai.geom) - ST_Area(inter.geom)) / NULLIF(ST_Area(ai.geom), 0)) * 100 AS falsoPositivo
        FROM qa, ai, inter
        """, nativeQuery = true)
    Object[] calcularMetricasRaw(@Param("missaoId") Long missaoId);

    default AcuraciaDTO calcularMetricas(Long missaoID) {
        Object[] row = (Object[]) calcularMetricasRaw(missaoID)[0];
        return new AcuraciaDTO(
            row[0] == null ? null : BigDecimal.valueOf(((Number) row[0]).doubleValue()).setScale(4, RoundingMode.HALF_UP).doubleValue(),
            row[1] == null ? null : BigDecimal.valueOf(((Number) row[1]).doubleValue()).setScale(4, RoundingMode.HALF_UP).doubleValue(),
            row[2] == null ? null : BigDecimal.valueOf(((Number) row[2]).doubleValue()).setScale(4, RoundingMode.HALF_UP).doubleValue()
        );
    }
    
    @Query("SELECT r FROM Relatorio r WHERE r.missao.id = :missaoId")
    Relatorio findByMissaoId(Long missaoId);
}