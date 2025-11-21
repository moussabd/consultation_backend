package sn.project.consultation.api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.project.consultation.api.dto.ProSanteDTO;
import sn.project.consultation.data.entities.Patient;
import sn.project.consultation.data.entities.ProSante;
import sn.project.consultation.data.repositories.ProSanteRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pros")
@Tag(name = "Professionnels de Santé", description = "Liste des professionnels de santé")
public class ProSanteController {

    @Autowired
    private ProSanteRepository proRepository;

    @Operation(summary = "Liste des professionnels de santé")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des professionnels récupérée avec succès"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @GetMapping("")
    public ResponseEntity<Map<String, Object>> getProsAsDto(
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) String specialite
    ) {
        List<ProSante> pros ;


            pros = proRepository.findByNomPrenomOrSpecialite(nom, specialite);

        List<ProSanteDTO> dtos = pros.stream().map(pro -> {
            ProSanteDTO dto = new ProSanteDTO();
            dto.setId(pro.getId());
            dto.setNom(pro.getNom());
            dto.setPrenom(pro.getPrenom());
            dto.setSpecialite(pro.getSpecialite());
            dto.setTarif(pro.getTarif());
            dto.setLatitude(pro.getLatitude());
            dto.setLongitude(pro.getLongitude());
            return dto;
        }).toList();

        Map<String, Object> response = new HashMap<>();
        response.put("data", dtos);
        return ResponseEntity.ok(response);
    }

}

