package com.fatec.api.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fatec.api.backend.DTO.AcuraciaDTO;
import com.fatec.api.backend.model.Relatorio;

@Repository
public interface RelatorioRepository extends JpaRepository<Relatorio, Long> {
@Query(value = """
        WITH
        qa AS  (SELECT ST_Union(d.dan_geom) AS geom
                FROM daninha d WHERE d.res_id = :qaId),
        ai AS  (SELECT ST_Union(d.dan_geom) AS geom
                FROM daninha d WHERE d.res_id = :aiId),
        inter AS (SELECT ST_Intersection(qa.geom, ai.geom) AS geom
                  FROM qa, ai)
        SELECT
            (ST_Area(inter.geom) / NULLIF(ST_Area(qa.geom), 0)) * 100      AS concordancia,
            ((ST_Area(qa.geom) - ST_Area(inter.geom)) / NULLIF(ST_Area(qa.geom), 0)) * 100  AS falsoNegativo,
            ((ST_Area(ai.geom) - ST_Area(inter.geom)) / NULLIF(ST_Area(ai.geom), 0)) * 100  AS falsoPositivo
        FROM qa, ai, inter
        """,
        nativeQuery = true)
    Object[] calcularMetricasRaw(Long qaId, Long aiId);

    default AcuraciaDTO calcularMetricas(Long qaId, Long aiId) {
        Object[] row = calcularMetricasRaw(qaId, aiId);
        if (row == null) return null;
        return new AcuraciaDTO(
            row[0] == null ? null : ((Number) row[0]).doubleValue(),
            row[1] == null ? null : ((Number) row[1]).doubleValue(),
            row[2] == null ? null : ((Number) row[2]).doubleValue()
        );
    }
}