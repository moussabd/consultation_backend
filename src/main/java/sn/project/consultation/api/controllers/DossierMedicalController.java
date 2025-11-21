package sn.project.consultation.api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import sn.project.consultation.api.dto.*;
import sn.project.consultation.data.entities.*;
import sn.project.consultation.data.enums.RoleUser;
import sn.project.consultation.data.repositories.DossierMedicalRepository;
import sn.project.consultation.services.DossierMedicalService;
import sn.project.consultation.services.PDFGeneratorService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dossier")
@Tag(name="Dossier Médical", description = "Gestion des dossiers médicaux")
public class DossierMedicalController {
    // ✅ Récupération du dossier médical complet d’un patient
    @Autowired
    private DossierMedicalService dossierService;
    @Autowired
    private DossierMedicalRepository dossierRepo;
    @Autowired
    private PDFGeneratorService pdfService;

    // 📌 Créer un dossier médical pour un patient
    @Operation(summary = "Créer un dossier médical pour un patient")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dossier créé avec succès"),
            @ApiResponse(responseCode = "404", description = "Patient non trouvé")
    })
    @PostMapping("/creer/{patientId}")
    public ResponseEntity<DossierMedicalDTO> creerDossier(@PathVariable Long patientId) {
        return ResponseEntity.ok(dossierService.creerDossier(patientId));
    }

    // 📌 Récupérer un dossier complet
    @Operation(summary = "Récupérer tous les dossiers médicaux")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dossiers trouvés"),
            @ApiResponse(responseCode = "404", description = "Aucun dossier trouvé")
    })
    @GetMapping("/all")
    public ResponseEntity<List<DossierMedicalDTO>> getDossiers() {
        return ResponseEntity.ok(dossierService.getDossiers());
    }

    // 📌 Récupérer un dossier complet
    @Operation(summary = "Récupérer le dossier médical complet d’un patient")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dossier trouvé"),
            @ApiResponse(responseCode = "404", description = "Aucun dossier trouvé")
    })
    @GetMapping("/patient/{id}")
    public ResponseEntity<List<DossierMedicalDTO>> getDossier(@PathVariable Long id) {
        return ResponseEntity.ok(dossierService.getDossiersByPatientId(id));
    }

    @Operation(summary = "Récupérer le dossier médical via un id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dossier trouvé"),
            @ApiResponse(responseCode = "404", description = "Aucun dossier trouvé")
    })
    @GetMapping("/{id}")
    public ResponseEntity<DossierMedicalDTO> getDossierById(@PathVariable Long id) {
        DossierMedical dossierMedical = dossierRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Dossier introuvable"));
        DossierMedicalDTO dossierDTO = DossierMedicalDTO.fromEntity(dossierMedical);
        return ResponseEntity.ok(dossierDTO);
    }



    @GetMapping("/mes-dossiers")
    public ResponseEntity<List<DossierMedicalDTO>> getMesDossiers(Authentication authentication) {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        System.out.println(user.getUser().getRole());
        if (user.getUser().getRole() == RoleUser.PATIENT) {
            Long patientId = user.getUser().getId();
            List<DossierMedicalDTO> dossiers = dossierService.getDossiersByPatientId(patientId);

            if (dossiers.isEmpty()) {
                dossierService.genererDossierPourPatient(patientId, 3);
                dossiers = dossierService.getDossiersByPatientId(user.getUser().getId());
            }
            return ResponseEntity.ok(dossiers);
        }

        if (user.getUser().getRole() == RoleUser.PRO_SANTE) {
            Long proId = user.getUser().getId();
            List<DossierMedicalDTO> dossiers = dossierService.getDossiersByProSanteId(proId);

            // 🔹 Exemple : si tu veux aussi générer pour les pros (optionnel)
            if (dossiers.isEmpty()) {
                dossierService.genererDossierPourPro(proId, 2); // à implémenter si besoin
                dossiers = dossierService.getDossiersByProSanteId(proId);
            }

            return ResponseEntity.ok(dossiers);
        }



        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @Operation(summary = "Rechercher des dossiers médicaux avec filtres")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Résultats de la recherche renvoyés"),
            @ApiResponse(responseCode = "404", description = "Aucun dossier trouvé")
    })
    @GetMapping("/search")
    public ResponseEntity<List<DossierMedicalDTO>> searchDossiers(
            @RequestParam(required = false) Long patientId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate filterDate,
            @RequestParam(required = false) String patientName
    ) {
        List<DossierMedicalDTO> result = dossierService.searchDossiers(patientId, filterDate, patientName);
        if (result.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
        return ResponseEntity.ok(result);
    }


    // 📌 Ajouter un document médical
    @Operation(summary = "Ajouter un document médical (ex : ordonnance, compte-rendu...)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Document ajouté"),
            @ApiResponse(responseCode = "404", description = "Dossier introuvable")
    })
    @PostMapping("/{id}/document")
    public ResponseEntity<Void> ajouterDocument(@PathVariable Long id, @RequestBody FichierMedicalDTO doc) {
        dossierService.ajouterDocument(id, doc);
        return ResponseEntity.ok().build();
    }

    // 📌 Ajouter un fichier annexe (PDF, image médicale...)
    @Operation(summary = "Ajouter un fichier médical annexe (PDF, image...)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Fichier ajouté"),
            @ApiResponse(responseCode = "404", description = "Dossier introuvable")
    })
    @PostMapping("/{id}/fichier-annexe")
    public ResponseEntity<Void> ajouterFichier(@PathVariable Long id, @RequestBody FichierMedicalDTO dto) {
        dossierService.ajouterFichierAnnexe(id, dto);
        return ResponseEntity.ok().build();
    }

    // 📌 Ajouter un historique de consultation
    @Operation(summary = "Ajouter une consultation dans l'historique du dossier")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Historique ajouté"),
            @ApiResponse(responseCode = "404", description = "Dossier introuvable")
    })
    @PostMapping("/{id}/historique")
    public ResponseEntity<Void> ajouterHistorique(@PathVariable Long id, @RequestBody HistoriqueConsultationDTO dto) {
        dossierService.ajouterHistorique(id, dto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Mettre à jour les antécédents médicaux d’un patient")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Antécédents mis à jour avec succès"),
            @ApiResponse(responseCode = "404", description = "Dossier introuvable")
    })
    @PutMapping("/{id}/antecedents")
    public ResponseEntity<Void> updateAntecedents(@PathVariable Long id, @RequestBody AntecedentsDTO dto) {
        dossierService.enregistrerAntecedents(id, AntecedentsDTO.toEntity(dto));
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Mettre à jour l'examen clinique d’un patient")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Examen clinique mis à jour"),
            @ApiResponse(responseCode = "404", description = "Dossier introuvable")
    })
    @PutMapping("/{id}/examen-clinique")
    public ResponseEntity<Void> updateExamenClinique(@PathVariable Long id, @RequestBody ExamenCliniqueDTO dto) {
        ExamenClinique exam=ExamenCliniqueDTO.toEntity(dto);
        dossierService.enregistrerExamenClinique(id, exam);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Mettre à jour les infos d'urgence relatives d’un patient")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Examen clinique mis à jour"),
            @ApiResponse(responseCode = "404", description = "Dossier introuvable")
    })
    @PutMapping("/{id}/infos-urgence")
    public ResponseEntity<Void> updateInfosUrgence(@PathVariable Long id, @RequestBody InfosUrgenceDTO infosUrgenceDTO) {
        dossierService.enregistrerInfosPrincipales(id, infosUrgenceDTO);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Mettre à jour les examens complémentaires")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Examens complémentaires mis à jour"),
            @ApiResponse(responseCode = "404", description = "Dossier introuvable")
    })
    @PutMapping("/{id}/examens-complementaires")
    public ResponseEntity<Void> updateExamensComplementaires(@PathVariable Long id, @RequestBody ExamensComplementairesDTO dto) {
        dossierService.enregistrerExamensComplementaires(id, ExamensComplementairesDTO.toEntity(dto));
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Mettre à jour le diagnostic médical")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Diagnostic mis à jour"),
            @ApiResponse(responseCode = "404", description = "Dossier introuvable")
    })
    @PutMapping("/{id}/diagnostic")
    public ResponseEntity<Void> updateDiagnostic(@PathVariable Long id, @RequestBody DiagnosticMedicalDTO dto) {
        dossierService.enregistrerDiagnostic(id, DiagnosticMedicalDTO.toEntity(dto));
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Mettre à jour les traitements et prescriptions")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Traitement mis à jour"),
            @ApiResponse(responseCode = "404", description = "Dossier introuvable")
    })
    @PutMapping("/{id}/traitements")
    public ResponseEntity<Void> updateTraitements(@PathVariable Long id, @RequestBody TraitementPrescriptionDTO dto) {
        dossierService.enregistrerTraitements(id, TraitementPrescriptionDTO.toEntity(dto));
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Mettre à jour l’évolution et le suivi clinique")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Évolution mise à jour"),
            @ApiResponse(responseCode = "404", description = "Dossier introuvable")
    })
    @PutMapping("/{id}/evolution")
    public ResponseEntity<Void> updateEvolution(@PathVariable Long id, @RequestBody EvolutionSuivi dto) {
        dossierService.enregistrerEvolutionSuivi(id, dto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Mettre à jour les correspondances médicales")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Correspondances mises à jour"),
            @ApiResponse(responseCode = "404", description = "Dossier introuvable")
    })
    @PutMapping("/{id}/correspondances")
    public ResponseEntity<Void> updateCorrespondances(@PathVariable Long id, @RequestBody CorrespondancesDTO dto) {
        dossierService.enregistrerCorrespondances(id, CorrespondancesDTO.toEntity(dto));
        return ResponseEntity.ok().build();
    }


    @GetMapping("/fiche/{dossierId}")
    public ResponseEntity<byte[]> genererFiche(@PathVariable Long dossierId) {
        DossierMedical dossierMedical = dossierRepo.findById(dossierId)
                .orElseThrow(() -> new RuntimeException("Dossier introuvable"));

        byte[] pdf = pdfService.generateFicheMedicalePDF(dossierMedical);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=fiche-" + dossierMedical.getId() + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);

    }


}