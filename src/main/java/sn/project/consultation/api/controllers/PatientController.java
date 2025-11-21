package sn.project.consultation.api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.project.consultation.data.entities.Patient;
import sn.project.consultation.data.repositories.PatientRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sn.project.consultation.api.dto.PatientDTO;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/patients")
@Tag(name = "Patients", description = "Gestion des patients")
public class PatientController {

    @Autowired
    private PatientRepository patientRepository;

    @Operation(summary = "Lister tous les patients")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des patients récupérée avec succès"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @GetMapping("")
    public ResponseEntity<Map<String, Object>> getAllPatientsAsDTO() {
        List<Patient> patients = patientRepository.findAll();
        List<PatientDTO> dtoList = patients.stream()
                .map(PatientDTO::fromEntity)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("data", dtoList);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Lister un patient via son id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des patients récupérée avec succès"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getPatientById(@PathVariable Long id) {
        Patient patient = patientRepository.findById(id).orElseThrow();
        Map<String, Object> response = new HashMap<>();
        response.put("data", PatientDTO.fromEntity(patient));
        return ResponseEntity.ok(response);
    }
}

