package com.fatec.api.backend.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fatec.api.backend.model.Fazenda;
import com.fatec.api.backend.model.Usuario;
import com.fatec.api.backend.repository.FazendaRepository;
import com.fatec.api.backend.service.EstadoService;
import com.fatec.api.backend.service.FazendaService;
import com.fatec.api.backend.service.UsuarioService;

import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/fazenda")
@CrossOrigin(origins = "*")
public class FazendaController {
    
    private FazendaService fazendaService;

    private EstadoService estadoService;

    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private FazendaRepository fazendaRepository;

    private Usuario.Role[] permissionList = new Usuario.Role[]{ Usuario.Role.Administrador, Usuario.Role.Consultor };
    private Usuario.Role[] allPermissionList = new Usuario.Role[]{ Usuario.Role.Administrador, Usuario.Role.Consultor, Usuario.Role.Analista };

    public FazendaController(FazendaService fazendaService, EstadoService estadoService) {
        this.fazendaService = fazendaService;
        this.estadoService = estadoService;
    }

    @PostMapping()
    public ResponseEntity<?> createFazenda(@RequestBody Fazenda fazenda, @RequestParam String token) {
        if(!usuarioService.verifyAccess(permissionList, token)) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        Fazenda savedFazenda = fazendaService.cadastrarFazenda(fazenda);
        return ResponseEntity.ok(savedFazenda);
    }

    @GetMapping("listar/{page}/{quantity}")
    public ResponseEntity<?> listarFazendasPaginadas(
        @PathVariable("page") int page, 
        @PathVariable("quantity") int size,
        @RequestParam String token
    ) {
        if(!usuarioService.verifyAccess(allPermissionList, token)) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        Page<Fazenda> fazendas = fazendaService.listarFazendasPaginadas(page, size);
        return ResponseEntity.ok(fazendas);
    }

    @GetMapping("listar")
    public ResponseEntity<?> listarFazendas(@RequestParam String token) {
        if(!usuarioService.verifyAccess(allPermissionList, token)) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        List<Fazenda> fazendas = fazendaRepository.findAll();
        List<Map<String, Object>> fazendasSerializer = new ArrayList<>();

        for (Fazenda fazenda : fazendas) {
            Map<String, Object> i = new HashMap<>();
            i.put("id", fazenda.getId());
            i.put("nome", fazenda.getNome());
            fazendasSerializer.add(i);
        }
        return ResponseEntity.ok(fazendasSerializer);
    }

    @GetMapping("/editar")
    public ResponseEntity<?> listarEstados(@RequestParam String token) {
        if(!usuarioService.verifyAccess(allPermissionList, token)) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        return ResponseEntity.ok(estadoService.listarEstados());
    }

    @GetMapping("listar/analista")
    public ResponseEntity<?> listarFazendasEAnalistas(@RequestParam String token) {
        if(!usuarioService.verifyAccess(allPermissionList, token)) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        return ResponseEntity.ok(fazendaService.listarFazendaEAnalistas());
    }

    @PutMapping("editar/{id}")
    public ResponseEntity<?> editarFazenda(@PathVariable Long id, @RequestBody Fazenda dados, @RequestParam String token) {
        if(!usuarioService.verifyAccess(permissionList, token)) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        return ResponseEntity.ok(fazendaService.editarFazenda(id, dados));
    }

}
