package com.fatec.api.backend.controller;
import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fatec.api.backend.DTO.ResultadoDTO;
import com.fatec.api.backend.model.Usuario;
import com.fatec.api.backend.service.ResultService;
import com.fatec.api.backend.service.UsuarioService;

@RestController
@RequestMapping("/result")
@CrossOrigin(origins = "*")
public class ResultController {

    @Autowired
    private ResultService resultService;
    
    @Autowired
    private UsuarioService usuarioService;

    private Usuario.Role[] consultorPermissionList = new Usuario.Role[]{ Usuario.Role.Administrador, Usuario.Role.Consultor };
    private Usuario.Role[] analistPermissionList = new Usuario.Role[]{ Usuario.Role.Administrador, Usuario.Role.Analista };
    private Usuario.Role[] allPermissionList = new Usuario.Role[]{ Usuario.Role.Administrador, Usuario.Role.Consultor, Usuario.Role.Analista };


    @PostMapping(value="/ai", consumes = "multipart/form-data")
    public ResponseEntity<?> createResultai(
        @RequestPart("talhoes_ids") String talhoesIdsString,
        @RequestPart("file") MultipartFile geoJsonFile,
        @RequestParam String token
    ) throws ParseException, IOException, org.locationtech.jts.io.ParseException {

        if(!usuarioService.verifyAccess(consultorPermissionList, token)) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        List<Long> talhoesIds = Arrays.stream(talhoesIdsString.split(" "))
                                            .map(String::trim)
                                            .map(str -> Long.valueOf(str))
                                            .collect(Collectors.toList());
        try {
            String geoJsonContent = new String(geoJsonFile.getBytes());
            resultService.createResultAIAsync(geoJsonContent, talhoesIds);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Processamento iniciado. Logo terá seu resultado disponivel");
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "An error occurred while processing the request.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PostMapping(value="/qa", consumes = "multipart/form-data")
    public ResponseEntity<?> createResultqa(
        @RequestPart("missao_id") String missaoId,
        @RequestPart("file") MultipartFile geoJsonFile,
        @RequestParam String token
    ) throws ParseException, IOException, org.locationtech.jts.io.ParseException {

        if(!usuarioService.verifyAccess(analistPermissionList, token)) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        Long missao_id = Long.valueOf(missaoId);  
        try {
            String geoJsonContent = new String(geoJsonFile.getBytes());
            resultService.createResultQAAsyn(geoJsonContent, missao_id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Processamento iniciado. Logo terá seu resultado disponivel");
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "An error occurred while processing the request.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @GetMapping(value="{mission_id}/{type_result}/{talhao_id}")
    public ResponseEntity<?> getResultByMission(
        @PathVariable Long mission_id,
        @PathVariable String type_result,
        @PathVariable Long talhao_id,
        @RequestParam String token
    ) {
        if(!usuarioService.verifyAccess(allPermissionList, token)) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        ResultadoDTO resultados = resultService.getResultadoByMission(mission_id, type_result, talhao_id);
        return ResponseEntity.ok(resultados);
    }
    
}
