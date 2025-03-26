package com.fatec.api.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fatec.api.backend.model.Talhao;
import com.fatec.api.backend.DTO.TalhaoDTO;
import com.fatec.api.backend.model.Fazenda;
import com.fatec.api.backend.repository.TalhaoRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.stream.Collectors;

@Service
public class TalhaoService {
    
    private final TalhaoRepository talhaoRepository;
    private final GeoJsonProcessor geoJsonProcessor;
    private final TalhaoFactory talhaoFactory;

    public TalhaoService(TalhaoRepository talhaoRepository, GeoJsonProcessor geoJsonProcessor, TalhaoFactory talhaoFactory) {
        this.talhaoRepository = talhaoRepository;
        this.geoJsonProcessor = geoJsonProcessor;
        this.talhaoFactory = talhaoFactory;
    }

    public List<TalhaoDTO> createTalhoes(String geoJsonContent, Fazenda fazenda) throws IOException, ParseException, org.locationtech.jts.io.ParseException {
        String cleanedGeoJson = geoJsonProcessor.cleanGeoJson(geoJsonContent);
        JsonNode features = geoJsonProcessor.extractFeatures(cleanedGeoJson);

        List<TalhaoDTO> talhoes = new ArrayList<>();
        if (features != null && features.isArray()) {
            for (JsonNode feature : features) {
                Talhao talhao = talhaoFactory.createTalhao(feature, fazenda);
                talhaoRepository.save(talhao);
                TalhaoDTO talhaoDTO = this.convertToGeoDTO(talhao);
                talhoes.add(talhaoDTO);
            }
        }

        return talhoes;
    }

    public Page<TalhaoDTO> listarTalhoesPaginados(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Talhao> talhoesPage = talhaoRepository.findAll(pageable);
        
        List<TalhaoDTO> talhoesDTO = talhoesPage.getContent().stream()
                .map(this::convertToGeoDTO)
                .collect(Collectors.toList());
                
        return new PageImpl<>(talhoesDTO, pageable, talhoesPage.getTotalElements());
    }

    private TalhaoDTO convertToGeoDTO(Talhao talhao) {
        ArrayNode geoJson = geoJsonProcessor.extractCoordinates(talhao.getShape());
        return new TalhaoDTO(talhao.getId(), talhao.getNome(), talhao.getCultura(), talhao.getArea(), geoJson);
    }

    public TalhaoDTO getTalhao(Long id) {
        Talhao talhao = talhaoRepository.getReferenceById(id);
        ArrayNode geoJson = geoJsonProcessor.extractCoordinates(talhao.getShape());
        return new TalhaoDTO(talhao.getId(), talhao.getNome(), talhao.getCultura(), talhao.getArea(), geoJson);
    }
}

