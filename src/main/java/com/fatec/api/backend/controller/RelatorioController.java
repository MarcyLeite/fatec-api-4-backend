package com.fatec.api.backend.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fatec.api.backend.service.RelatorioService;
import com.fatec.api.backend.service.UsuarioService;
import com.fatec.api.backend.model.Relatorio;
import com.fatec.api.backend.model.Usuario;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/relatorio")
@CrossOrigin(origins = "*")
public class RelatorioController {

    @Autowired
    private RelatorioService relatorioService;

    @Autowired
    UsuarioService usuarioService;

    private Usuario.Role[] permissionList = new Usuario.Role[]{ Usuario.Role.Administrador, Usuario.Role.Analista };


    @PostMapping("/salvar")
    public ResponseEntity<String> criarRelatorio(@RequestBody Map<String, Object> request, @RequestParam String token) {
        if(!usuarioService.verifyAccess(permissionList, token)) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        try {
            if (!request.containsKey("dataInicioRelatorio") || request.get("dataInicioRelatorio") == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de início do relatório é obrigatória.");
            }

            if (!request.containsKey("status") || request.get("status") == null || request.get("status").toString().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("O status é obrigatório.");
            }

            if (!request.containsKey("userId") || request.get("userId") == null || request.get("userId").toString().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("O ID do usuário é obrigatório.");
            }

            if (!request.containsKey("missaoId") || request.get("missaoId") == null || request.get("missaoId").toString().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("O ID da missão é obrigatório.");
            }

            LocalDateTime dataInicioRelatorio = LocalDateTime.parse(request.get("dataInicioRelatorio").toString());
            
            String statusStr = request.get("status").toString().trim();
            Relatorio.Status status = Relatorio.Status.valueOf(statusStr);
            Long userId = Long.valueOf(request.get("userId").toString());
            Long missaoId = Long.valueOf(request.get("missaoId").toString());

            LocalDateTime dataFinalRelatorio = LocalDateTime.now();

            relatorioService.processarRelatorio(
                dataInicioRelatorio,
                dataFinalRelatorio,
                status,
                userId,
                missaoId
            );

            return ResponseEntity.status(HttpStatus.CREATED).body("Relatório criado com sucesso!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Status inválido fornecido.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao criar o relatório: " + e.getMessage());
        }
    }
}