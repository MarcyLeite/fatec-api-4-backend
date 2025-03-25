package com.fatec.api.backend.service;

import java.util.List;
import java.util.stream.Collectors;
import com.fatec.api.backend.geojson.TalhaoGeoDTO;
import com.fatec.api.backend.model.Talhao;
import com.fatec.api.backend.repository.TalhaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TalhaoService {
    
    private final TalhaoRepository talhaoRepository;

    @Autowired
    public TalhaoService(TalhaoRepository talhaoRepository) {
        this.talhaoRepository = talhaoRepository;
    }

    public Page<TalhaoGeoDTO> listarTalhoesPaginados(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Talhao> talhoesPage = talhaoRepository.findAll(pageable);
        
        List<TalhaoGeoDTO> talhoesDTO = talhoesPage.getContent().stream()
        .map(this::convertToGeoDTO)
        .collect(Collectors.toList());
        
        return new PageImpl<>(talhoesDTO, pageable, talhoesPage.getTotalElements());
    }
    
    private TalhaoGeoDTO convertToGeoDTO(Talhao talhao) {
        return new TalhaoGeoDTO(
            talhao.getId(),
                talhao.getNome(),
                talhao.getCultura(),
                talhao.getArea(),
                talhao.getShape()
                );
    }
}

