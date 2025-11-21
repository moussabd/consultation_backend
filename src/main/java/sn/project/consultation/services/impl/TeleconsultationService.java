package sn.project.consultation.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sn.project.consultation.api.dto.MessageDTO;
import sn.project.consultation.data.entities.*;
import sn.project.consultation.data.repositories.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class TeleconsultationService {

    @Autowired private TeleconsultationRepository teleconsultationRepo;
    @Autowired private UserRepository userRepo;
    @Autowired private PatientRepository patientRepo;
    @Autowired private ProSanteRepository proRepo;
    @Autowired private MessageRepository messageRepo;

    // ✅ Planifie une téléconsultation si date libre
    public Teleconsultation planifierTeleconsultation(Long patientId, Long medecinId, LocalDateTime dateHeure) {
        if (dateHeure.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La date de consultation doit être ultérieure à l’heure actuelle.");
        }

        // 💡 Empêcher les doublons
        if (teleconsultationRepo.existsByProSanteIdAndDateHeure(medecinId, dateHeure)) {
            throw new IllegalStateException("Ce créneau est déjà pris pour ce médecin.");
        }

        Teleconsultation tc = new Teleconsultation();
        tc.setPatient(patientRepo.findById(patientId).orElseThrow());
        tc.setProSante(proRepo.findById(medecinId).orElseThrow());
        tc.setDateHeure(dateHeure);
        tc.setStatut("EN_ATTENTE");
        tc.setLienVideo(genererLienVideoSecurise());

        return teleconsultationRepo.save(tc);
    }

    // ✅ Envoi d’un message dans une téléconsultation
    public Message envoyerMessage(Long teleconsultationId, Long expediteurId, String contenu) {
        Teleconsultation tc = teleconsultationRepo.findById(teleconsultationId).orElseThrow();
        User expediteur = userRepo.findById(expediteurId).orElseThrow();

        // 🔐 Vérifier que l’expéditeur est bien impliqué
        if (!tc.getPatient().getId().equals(expediteurId) &&
                !tc.getProSante().getId().equals(expediteurId)) {
            throw new SecurityException("Vous n’êtes pas autorisé à participer à cette consultation.");
        }

        Message msg = new Message();
        msg.setTeleconsultation(tc);
        msg.setExpediteur(expediteur);
        msg.setContenu(contenu);
        msg.setDateEnvoi(LocalDateTime.now());

        return messageRepo.save(msg);
    }

    // ✅ Récupère les messages d’une consultation
    public List<Message> getMessages(Long teleconsultationId) {
        return messageRepo.findByTeleconsultationIdOrderByDateEnvoiAsc(teleconsultationId);
    }

    // ✅ Génère un lien de visio sécurisé et personnalisable
    private String genererLienVideoSecurise() {
        String base = "https://meet.jit.si/";
        String identifiant = "teleconsult-" + UUID.randomUUID().toString().substring(0, 8);
        return base + identifiant;
    }

    public Message saveLogMessage(MessageDTO dto) {
        Message message = MessageDTO.toEntity(dto);
        Teleconsultation teleconsultation = teleconsultationRepo.findById(dto.getTeleconsultationId())
                .orElseThrow(() -> new RuntimeException("Téléconsultation non trouvée"));

        message.setTeleconsultation(teleconsultation);

        if ("PATIENT".equalsIgnoreCase(dto.getExpediteurType())) {
            Patient p = patientRepo.findById(dto.getExpediteurId())
                    .orElseThrow(() -> new RuntimeException("Patient introuvable"));
            message.setExpediteur(p);
        } else if ("PRO_SANTE".equalsIgnoreCase(dto.getExpediteurType())) {
            ProSante pro = proRepo.findById(dto.getExpediteurId())
                    .orElseThrow(() -> new RuntimeException("Professionnel de Santé Introuvable"));
            message.setExpediteur(pro);
        }

        return messageRepo.save(message);
    }
    // (Optionnel) ✅ Annulation de téléconsultation
    public void annulerTeleconsultation(Long consultationId, Long demandeurId) {
        Teleconsultation tc = teleconsultationRepo.findById(consultationId).orElseThrow();
        if (!tc.getPatient().getId().equals(demandeurId) &&
                !tc.getProSante().getId().equals(demandeurId)) {
            throw new SecurityException("Vous ne pouvez pas annuler cette téléconsultation.");
        }
        tc.setStatut("ANNULEE");
        teleconsultationRepo.save(tc);
        // Optionnel : envoyer une notification ici
    }
}
