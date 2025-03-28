package com.fatec.api.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fatec.api.backend.model.Estado;
import com.fatec.api.backend.repository.EstadoRepository;

@RestController
@RequestMapping("/estado")
@CrossOrigin(origins= "*")
public class EstadoController {

    @Autowired
    private EstadoRepository estadoRepository;

	@GetMapping("/all")
	public ResponseEntity<List<Estado>> getCidade() {
		return ResponseEntity.ok(estadoRepository.findAll());
	}
}
