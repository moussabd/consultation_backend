package sn.project.consultation.api.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.project.consultation.api.dto.*;
import sn.project.consultation.data.entities.RendezVous;
import sn.project.consultation.services.RendezVousService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/rendezvous")
@Tag(name = "Rendez-vous", description = "Planification et gestion des rendez-vous")
public class RendezVousController {
    @Autowired
    private RendezVousService service;

    @Operation(summary = "Créer un nouveau rendez-vous")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rendez-vous créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur Serveur"),
    })
    @PostMapping
    public ResponseEntity<RendezVousRequestDTO> creer(@RequestBody RendezVousRequestDTO dto) {
        RendezVousRequestDTO result = service.creerRendezVous(dto);
        if (result.getId() != null) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Annuler un rendez-vous")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Rendez-vous annulé avec succès"),
            @ApiResponse(responseCode = "404", description = "Rendez-vous non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> annuler(@PathVariable Long id) {
        try {
            service.annulerRendezVous(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Lister les rendez-vous d'un patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des rendez-vous récupérée avec succès"),
            @ApiResponse(responseCode = "204", description = "Rendez-vous non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @GetMapping("/patient/{id}")
    public ResponseEntity<List<RendezVousDTO>> lister(@PathVariable Long id) {
        List<RendezVousDTO> liste = service.listerRendezVousParPatient(id);
        if (liste.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(liste);
        }
    }

    @GetMapping("/pro/{id}")
    public ResponseEntity<List<RendezVousDTO>> listerPro(@PathVariable Long id) {
        List<RendezVousDTO> liste = service.listerRendezVousParPro(id);
        if (liste.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(liste);
        }
    }

    // ✅ Modification d’un rendez-vous
    @Operation(summary = "Modifier un rendez-vous")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rendez-vous modifié avec succès"),
            @ApiResponse(responseCode = "404", description = "Rendez-vous non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @PutMapping("/{id}")
    public ResponseEntity<RendezVousDTO> modifier(@PathVariable Long id, @RequestBody RendezVousDTO dto) {
        try {
            RendezVous updated = service.modifierRendezVous(id, dto);
            return ResponseEntity.ok(RendezVousDTO.fromEntity(updated));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Rechercher des professionnels selon des critères")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Professionnels trouvés avec succès"),
            @ApiResponse(responseCode = "204", description = "Aucun professionnel trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @PostMapping("/recherche")
    public ResponseEntity<List<ProSanteDTO>> rechercher(@RequestBody RechercheProDTO criteres) {
        List<ProSanteDTO> resultats = service.rechercherProfessionnels(criteres);
        if (resultats.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(resultats);
        }
    }

    @Operation(summary = "Tester l'envoi des rappels")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rappels envoyés avec succès"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @PostMapping("/test-rappel")
    public ResponseEntity<String> testerRappel() {
        service.envoyerRappels();
        return ResponseEntity.ok("Rappels envoyés !");
    }

    @Operation(summary = "Optimiser une tournée médicale")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tournée optimisée retournée avec succès"),
            @ApiResponse(responseCode = "404", description = "Professionnel non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @GetMapping("/optimiser-tournee/{id}")
    public ResponseEntity<TourneeOptimiseeDTO> optimiserTournee(@PathVariable Long id) {
        TourneeOptimiseeDTO tournee = service.optimiserTournee(id);
        return ResponseEntity.ok(tournee);
    }

    @Operation(summary = "Statistiques hebdomadaires d'un professionnel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Statistiques récupérées avec succès"),
            @ApiResponse(responseCode = "404", description = "Professionnel non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @GetMapping("/statistiques/{proId}")
    public ResponseEntity<Map<String, Object>> statistiques(@PathVariable Long proId) {
        return ResponseEntity.ok(service.statistiquesHebdo(proId));
    }

    // ✅ Créneaux disponibles pour un professionnel à une date donnée
    @Operation(summary = "Créneaux disponibles d'un professionnel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des créneaux disponibles renvoyée"),
            @ApiResponse(responseCode = "400", description = "Paramètres de requête invalides"),
            @ApiResponse(responseCode = "404", description = "Professionnel non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @GetMapping("/creneaux-disponibles")
    public ResponseEntity<List<LocalDateTime>> getCreneauxDisponibles(
            @RequestParam Long proId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(service.getCreneauxDisponibles(proId, date));
    }

    // ✅ Carte des patients pour un professionnel
    @Operation(summary = "Carte des patients")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des positions des patients retournée avec succès"),
            @ApiResponse(responseCode = "404", description = "Professionnel non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @GetMapping("/carte-patients/{proId}")
    public ResponseEntity<List<Map<String, Object>>> getCartePatients(@PathVariable Long proId) {
        return ResponseEntity.ok(service.getCartePatients(proId));
    }
}