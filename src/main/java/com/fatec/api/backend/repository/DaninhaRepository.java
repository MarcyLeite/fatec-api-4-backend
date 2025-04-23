package com.fatec.api.backend.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fatec.api.backend.model.Daninha;
@Repository
public interface DaninhaRepository extends JpaRepository<Daninha, Long> {

    @Query(value = "SELECT json_agg(json_build_object('type', 'MultiPolygon', 'coordinates', ST_AsGeoJSON(d.dan_geom)::json->'coordinates')) "
                 + "FROM daninha d WHERE d.res_id = :resultado_id", nativeQuery = true)
    String findAggregatedGeoJsonByResultadoId(@Param("resultado_id") Long resultado_id);

    @Query(value = """
        SELECT json_build_object(
            'type', 'FeatureCollection',
            'features', json_agg(
                json_build_object(
                    'type', 'Feature',
                    'geometry', ST_AsGeoJSON(d.dan_geom)::json,
                    'properties', json_build_object()
                )
            )
        )
        FROM daninha d
        WHERE d.res_id = :resultado_id
          AND ST_Intersects(
                d.dan_geom,
                (SELECT t.rel_shape FROM talhao t WHERE t.id = :talhao_id)
            )
        """, nativeQuery = true)
    String findAggregatedGeoJsonByResultadoIdAndTalhaoId(
        @Param("resultado_id") Long resultadoId,
        @Param("talhao_id") Long talhaoId
    );
    
}