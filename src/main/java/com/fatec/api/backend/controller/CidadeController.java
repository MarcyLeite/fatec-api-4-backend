package com.fatec.api.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fatec.api.backend.model.Cidade;
import com.fatec.api.backend.repository.CidadeRepository;

@RestController
@RequestMapping("/cidade")
@CrossOrigin(origins= "*")
public class CidadeController {

    @Autowired
    private CidadeRepository cidadeRepository;

	@GetMapping("/all")
	public ResponseEntity<List<Cidade>> getCidade() {
		return ResponseEntity.ok(cidadeRepository.findAll());
	}
}
