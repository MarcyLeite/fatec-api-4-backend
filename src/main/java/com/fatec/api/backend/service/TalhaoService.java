package com.fatec.api.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fatec.api.backend.model.Talhao;
import com.fatec.api.backend.DTO.TalhaoDTO;
import com.fatec.api.backend.model.Fazenda;
import com.fatec.api.backend.repository.TalhaoRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

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
                TalhaoDTO talhaoDTO = new TalhaoDTO(talhao.getId(), talhao.getNome(), talhao.getCultura(), talhao.getArea());
                talhoes.add(talhaoDTO);
            }
        }

        return talhoes;
    }
}