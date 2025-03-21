package com.fatec.api.backend.service;

import com.fatec.api.backend.DTO.TalhaoDTO;
import com.fatec.api.backend.model.Talhao;
import com.fatec.api.backend.model.Fazenda;
import com.fatec.api.backend.repository.TalhaoRepository;

import java.io.IOException;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.geojson.GeoJsonReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class TalhaoService {
    
    @Autowired
    private TalhaoRepository talhaoRepository;
    
    public Talhao createTalhao(TalhaoDTO talhaoDTO, MultipartFile geoJsonFile, Fazenda fazenda) throws IOException {
        Talhao talhao = new Talhao();
        
        talhao.setNome(talhaoDTO.getNome());
        talhao.setCultura(talhaoDTO.getCultura());
        talhao.setFazenda(fazenda);
        
        Geometry geometry = processGeoJson(geoJsonFile);
        talhao.setShape(geometry);
        talhao.setArea(calculateArea(geometry));
        
        return talhaoRepository.save(talhao);
    }
    
    private Geometry processGeoJson(MultipartFile file) throws IOException {
        try {
            String geoJsonContent = new String(file.getBytes());
            GeoJsonReader reader = new GeoJsonReader();
            return reader.read(geoJsonContent);
        } catch (org.locationtech.jts.io.ParseException e) {
            throw new IOException("Erro ao processar GeoJSON", e);
        }
    }
    
    private Float calculateArea(Geometry geometry) {
        double areaEmMetrosQuadrados = geometry.getArea();
        return (float) (areaEmMetrosQuadrados / 10000.0);
    }
}