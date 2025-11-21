package sn.project.consultation.api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.project.consultation.api.dto.FactureDTO;
import sn.project.consultation.api.dto.PaiementRequestDTO;
import sn.project.consultation.api.dto.PatientDTO;
import sn.project.consultation.api.dto.ProSanteDTO;
import sn.project.consultation.data.entities.Facture;
import sn.project.consultation.data.entities.Paiement;
import sn.project.consultation.services.FactureService;
import sn.project.consultation.services.PaiementService;
import sn.project.consultation.data.repositories.PaiementRepository;
import sn.project.consultation.data.repositories.FactureRepository;
import sn.project.consultation.services.impl.PaytechService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/paiements")
@Tag(name = "Paiements", description = "Paiement, Facture")
public class PaiementController {

    @Autowired private PaiementService paiementService;
    @Autowired private FactureService factureService;
    @Autowired private PaiementRepository paiementRepo;
    @Autowired private FactureRepository factureRepo;
    @Autowired private PaytechService paytechService;

    @PostMapping("/payer/{factureId}")
    public ResponseEntity<Facture> effectuerPaiement(
            @PathVariable Long factureId,
            @Valid @RequestBody PaiementRequestDTO dto) {
        Facture facture = paiementService.effectuerPaiement(factureId, dto);
        return ResponseEntity.ok(facture);
    }

    @PostMapping("/initier/{factureId}")
    @Operation(summary = "Initier un paiement partiel via Wave ou Orange Money pour une facture")
    public ResponseEntity<Map<String, String>> initierPaiement(
            @PathVariable Long factureId,
            @Valid @RequestBody PaiementRequestDTO dto) {
        Map<String, String> response = new HashMap<>();
        try {
            Paiement paiement = paiementService.initierPaiementPourFacture(factureId, dto);
            System.out.println(paiement.getMontant()+" "+paiement.getId());
            String urlPaiement = paytechService.initierPaiement(
                    paiement.getMontant(),
                    paiement.getMethode(),
                    paiement.getId(),
                    paiement.getPatient().getId()
            );

            response.put("paiementUrl", urlPaiement);
            response.put("paiementId", paiement.getId().toString());
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            log.error("Erreur lors de l'initiation du paiement : {}", e.getMessage(), e);
            response.put("error", "Impossible de créer le paiement");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

//    @GetMapping("/montant/{patientId}")
//    public ResponseEntity<Double> getMontantAPayer(@PathVariable Long patientId) {
//        return ResponseEntity.ok(paiementService.getMontantAPayer(patientId));
//    }

    @GetMapping("/details/{paiementId}")
    public ResponseEntity<Paiement> getDetailPaiement(@PathVariable Long paiementId) {
        return ResponseEntity.ok(paiementRepo.findById(paiementId).orElseThrow());
    }

    @GetMapping("/factures/{patientId}")
    public ResponseEntity<List<FactureDTO>> listerFactures(@PathVariable Long patientId) {
        return ResponseEntity.ok(factureService.getFacturesByPatient(patientId));
    }

    @DeleteMapping("/factures/{id}")
    public ResponseEntity<Void> supprimerFacture(@PathVariable Long id) {
        factureRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/callback")
    @Operation(summary = "Callback PayTech — mise à jour automatique du paiement après confirmation")
    public ResponseEntity<String> handlePaytechCallback(@RequestBody Map<String, Object> payload) {
        try {
            paiementService.traiterCallbackPaytech(payload);
            return ResponseEntity.ok("Callback traité avec succès ✅");
        } catch (Exception e) {
            log.error("❌ Erreur callback PayTech : {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erreur callback : " + e.getMessage());
        }
    }
}

