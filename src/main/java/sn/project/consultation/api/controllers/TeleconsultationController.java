package sn.project.consultation.api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.project.consultation.api.dto.ApiResponseDTO;
import sn.project.consultation.api.dto.MessageDTO;
import sn.project.consultation.api.dto.PlanificationDTO;
import sn.project.consultation.api.dto.TeleconsultationDTO;
import sn.project.consultation.data.entities.Message;
import sn.project.consultation.data.entities.Teleconsultation;
import sn.project.consultation.services.impl.TeleconsultationService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/teleconsultations")
@Tag(name = "Téléconsultation", description = "Gestion des téléconsultations")
public class TeleconsultationController {

    @Autowired
    private TeleconsultationService teleconsultationService;

    /**
     * Planifie une téléconsultation
     */
    @Operation(summary = "Planifier une téléconsultation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Téléconsultation planifiée avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides pour la planification"),
            @ApiResponse(responseCode = "404", description = "Patient ou médecin introuvable"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @PostMapping("/planifier")
    public ResponseEntity<TeleconsultationDTO> planifier(@RequestBody PlanificationDTO dto) {
        Teleconsultation tc = teleconsultationService.planifierTeleconsultation(dto.getPatientId(), dto.getMedecinId(), dto.getDateHeure());
        return ResponseEntity.ok(TeleconsultationDTO.fromEntity(tc));
    }

    /**
     * Envoie un message
     */

    @Operation(summary = "Envoyer un message dans une téléconsultation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Message envoyé avec succès"),
            @ApiResponse(responseCode = "400", description = "Données de message invalides"),
            @ApiResponse(responseCode = "404", description = "Téléconsultation ou expéditeur introuvable"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @PostMapping("/{id}/messages")
    public ResponseEntity<Message> envoyerMessage(@PathVariable Long id, @RequestBody MessageDTO dto) {
        Message msg = teleconsultationService.envoyerMessage(id, dto.getExpediteurId(), dto.getContenu());
        return ResponseEntity.ok(msg);
    }

    /**
     * Récupère les messages d'une téléconsultation
     */

    @Operation(summary = "Récupérer les messages d'une téléconsultation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Messages récupérés avec succès"),
            @ApiResponse(responseCode = "404", description = "Téléconsultation introuvable"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @GetMapping("/{id}/messages")
    public ResponseEntity<List<Message>> getMessages(@PathVariable Long id) {
        List<Message> messages = teleconsultationService.getMessages(id);
        return ResponseEntity.ok(messages);
    }

    @PostMapping("/messages/log")
    public ResponseEntity<ApiResponseDTO> saveJitsiLog(@Valid @RequestBody MessageDTO dto) {
        try {
            Message saved = teleconsultationService.saveLogMessage(dto);
            URI location = URI.create(String.format("/api/messages/%d", saved.getId()));

            ApiResponseDTO response = new ApiResponseDTO(true, "Message log enregistré avec succès", saved.getId());
            return ResponseEntity.created(location).body(response);
        } catch (EntityNotFoundException ex) {
            ApiResponseDTO response = new ApiResponseDTO();
            response.setSuccess(false);
            response.setMessage("Téléconsultation ou expéditeur introuvable : "+ ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception ex) {
            ApiResponseDTO response = new ApiResponseDTO();
            response.setSuccess(false);
            response.setMessage("Erreur serveur : "+ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
