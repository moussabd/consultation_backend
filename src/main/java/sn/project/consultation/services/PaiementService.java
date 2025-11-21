package sn.project.consultation.services;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sn.project.consultation.api.dto.PaiementRequestDTO;
import sn.project.consultation.data.entities.*;
import sn.project.consultation.data.repositories.FactureRepository;
import sn.project.consultation.data.repositories.PaiementRepository;
import sn.project.consultation.data.repositories.PatientRepository;
import sn.project.consultation.data.repositories.ProSanteRepository;
import sn.project.consultation.services.impl.SmsService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
@Slf4j
@Service
public class PaiementService {

    @Autowired private PaiementRepository paiementRepo;
    @Autowired private FactureRepository factureRepo;
    @Autowired private PatientRepository patientRepo;
    @Autowired private ProSanteRepository proRepo;
    @Autowired private CloudStorageService cloudStorage;
    @Autowired private EmailService emailService;
    @Autowired private SmsService smsService;

    /**
     * 💳 Paiement unique et direct (simulation d’un succès immédiat)
     */
    @Transactional
    public Facture effectuerPaiement(Long factureId, PaiementRequestDTO dto) {
        log.info("💳 Paiement lancé pour facture ID={} avec montant {} FCFA", factureId, dto.getMontant());

        // 1️⃣ Vérification de la facture
        Facture facture = factureRepo.findById(factureId)
                .orElseThrow(() -> new IllegalArgumentException("Facture introuvable avec ID " + factureId));

        if (facture.getEtatPaiement() == EtatPaiement.PAYEE) {
            throw new IllegalStateException("Cette facture est déjà entièrement payée.");
        }

        // 2️⃣ Vérification du patient et du professionnel
        Patient patient = patientRepo.findById(dto.getPatient().getId())
                .orElseThrow(() -> new IllegalArgumentException("Patient introuvable avec ID " + dto.getPatient().getId()));

        ProSante pro = proRepo.findById(dto.getProfessionnel().getId())
                .orElseThrow(() -> new IllegalArgumentException("Professionnel introuvable avec ID " + dto.getProfessionnel().getId()));

        double montantFacture = facture.getMontant();
        double montantAPayer = dto.getMontant();

        // 3️⃣ Validation du montant
        if (montantAPayer <= 0) {
            throw new IllegalArgumentException("Le montant doit être supérieur à 0");
        }
        if (montantAPayer != montantFacture) {
            throw new IllegalArgumentException("Le paiement doit correspondre exactement au montant de la facture (" + montantFacture + " FCFA)");
        }

        // 4️⃣ Vérifier s’il y a déjà un paiement associé
        if (facture.getPaiement() != null) {
            throw new IllegalStateException("Un paiement existe déjà pour cette facture.");
        }

        // 5️⃣ Création du paiement
        Paiement paiement = new Paiement();
        paiement.setMontant(montantAPayer);
        paiement.setPatient(patient);
        paiement.setProfessionnel(pro);
        paiement.setDatePaiement(LocalDateTime.now());
        paiement.setMethode(dto.getMethode());
        paiement.setStatut("SUCCES");
        paiement.setReference(UUID.randomUUID().toString());
        paiement.setFacture(facture);

        paiementRepo.save(paiement);

        // 6️⃣ Mise à jour de la facture
        facture.setPaiement(paiement);
        facture.setEtatPaiement(EtatPaiement.PAYEE);
        factureRepo.save(facture);

        log.info("✅ Paiement de {} FCFA enregistré pour la facture {} — État: PAYÉE COMPLETEMENT", montantAPayer, facture.getNumero());

        envoyerRecuMultiCanal(patient, facture);

        return facture;
    }

    /**
     * 🕓 Paiement initié (PayTech) — non encore confirmé
     */
    @Transactional
    public Paiement initierPaiementPourFacture(Long factureId, PaiementRequestDTO dto) {
        Facture facture = factureRepo.findById(factureId)
                .orElseThrow(() -> new IllegalArgumentException("Facture introuvable"));

        if (facture.getEtatPaiement() == EtatPaiement.PAYEE) {
            throw new IllegalStateException("Cette facture est déjà réglée.");
        }

        Patient patient = patientRepo.findById(dto.getPatient().getId())
                .orElseThrow(() -> new IllegalArgumentException("Patient introuvable"));

        ProSante pro = proRepo.findById(dto.getProfessionnel().getId())
                .orElseThrow(() -> new IllegalArgumentException("Professionnel introuvable"));

        Paiement paiement = new Paiement();
        paiement.setFacture(facture);
        paiement.setMontant(facture.getMontant());
        paiement.setMethode(dto.getMethode());
        paiement.setStatut("EN_ATTENTE");
        paiement.setDatePaiement(LocalDateTime.now());
        paiement.setPatient(patient);
        paiement.setProfessionnel(pro);
        paiement.setReference(UUID.randomUUID().toString());

        paiementRepo.save(paiement);

        facture.setPaiement(paiement);
        facture.setEtatPaiement(EtatPaiement.EN_ATTENTE);
        factureRepo.save(facture);

        log.info("⏳ Paiement en attente créé pour facture {} ({} FCFA)", facture.getNumero(), facture.getMontant());

        return paiement;
    }

    /**
     * 🔁 Traitement du callback PayTech
     */
    @Transactional
    public void traiterCallbackPaytech(Map<String, Object> payload) {
        log.info("📩 Callback PayTech reçu : {}", payload);

        try {
            String reference = String.valueOf(payload.get("reference"));
            String statut = String.valueOf(payload.get("status"));

            Paiement paiement = paiementRepo.findByReference(reference)
                    .orElseThrow(() -> new IllegalArgumentException("Paiement introuvable pour référence " + reference));

            boolean success = "success".equalsIgnoreCase(statut) || "completed".equalsIgnoreCase(statut);

            paiement.setStatut(success ? "SUCCES" : "ECHEC");
            paiement.setDatePaiement(LocalDateTime.now());
            paiementRepo.save(paiement);

            Facture facture = paiement.getFacture();
            facture.setEtatPaiement(success ? EtatPaiement.PAYEE : EtatPaiement.NON_PAYEE);
            factureRepo.save(facture);

            if (success) {
                envoyerRecuMultiCanal(paiement.getPatient(), facture);
            }

            log.info("✅ Callback traité : facture={} statutPaiement={}", facture.getNumero(), paiement.getStatut());

        } catch (Exception e) {
            log.error("❌ Erreur lors du traitement du callback PayTech : {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 💌 Envoi du reçu de paiement par email et SMS
     */
    private void envoyerRecuMultiCanal(Patient patient, Facture facture) {
        String message = "🎉 Paiement confirmé ! Reçu n°" + facture.getNumero() + " envoyé à votre email.";

        String url = facture.getUrlPdf();
        if (url == null) return;

        String cheminRelatif = url.replace("http://localhost:10001/files/", "");
        String cheminLocal = cloudStorage.getCheminComplet(cheminRelatif);

        try {
            emailService.envoyerEmail(
                    patient.getCoordonnees().getEmail(),
                    "Votre reçu de paiement",
                    message,
                    cheminLocal
            );
            log.info("📧 Reçu envoyé à {}", patient.getCoordonnees().getEmail());
        } catch (Exception e) {
            log.error("❌ Erreur lors de l'envoi de l'email : {}", e.getMessage());
        }

        try {
            if (patient.getCoordonnees().getNumeroTelephone() != null) {
//                smsService.envoyerSms(patient.getCoordonnees().getNumeroTelephone(), message);
                log.info("📱 SMS envoyé à {}", patient.getCoordonnees().getNumeroTelephone());
            }
        } catch (Exception e) {
            log.error("❌ Erreur lors de l'envoi du SMS : {}", e.getMessage());
        }
    }
}


