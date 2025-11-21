package sn.project.consultation.services;

import sn.project.consultation.api.dto.*;
import sn.project.consultation.data.entities.RendezVous;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface RendezVousService {
    RendezVousRequestDTO creerRendezVous(RendezVousRequestDTO dto);
    RendezVous modifierRendezVous(Long id, RendezVousDTO dto);
    void annulerRendezVous(Long id);
    List<RendezVousDTO> listerRendezVousParPatient(Long patientId);
    List<RendezVousDTO> listerRendezVousParPro(Long proSanteId);
    List<ProSanteDTO> rechercherProfessionnels(RechercheProDTO criteres);
    TourneeOptimiseeDTO optimiserTournee(Long professionnelId);
    void envoyerRappels();
    List<LocalDateTime> getCreneauxDisponibles(Long proId, LocalDate date);
    Map<String, Object> statistiquesHebdo(Long professionnelId);
    List<Map<String, Object>> getCartePatients(Long proId);
}
