package com.fatec.api.backend.controller;

import com.fatec.api.backend.DTO.DashboardDTO;
import com.fatec.api.backend.DTO.AnalistaDTO;
import com.fatec.api.backend.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<DashboardDTO> getDashboardData(
            @RequestParam(required = false) Long analistaId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        
        DashboardDTO dashboard = dashboardService.getDashboardData(analistaId, inicio, fim);
        return ResponseEntity.ok(dashboard);
    }

    @GetMapping("/analistas")
    public ResponseEntity<List<AnalistaDTO>> getAnalistas() {
        List<AnalistaDTO> analistas = dashboardService.getAnalistas();
        return ResponseEntity.ok(analistas);
    }
}
