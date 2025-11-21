package sn.project.consultation.services;

import sn.project.consultation.api.dto.*;
import sn.project.consultation.data.entities.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface DossierMedicalService {

    // === CRUD GLOBAL ===
    DossierMedicalDTO creerDossier(Long patientId);
    List<DossierMedicalDTO> getDossiersByPatientId(Long id);
    List<DossierMedicalDTO> getDossiersByProSanteId(Long id);
    void genererDossierPourPatient(Long patientId, int nombreDossiers);
    // === DOCUMENTS MÉDICAUX ===
    void ajouterDocument(Long dossierId, FichierMedicalDTO dto);
    void ajouterFichierAnnexe(Long dossierId, FichierMedicalDTO dto);
    void enregistrerInfosPrincipales(Long dossierId, InfosUrgenceDTO infos) ;
    // === HISTORIQUE DE CONSULTATIONS ===
    void ajouterHistorique(Long dossierId, HistoriqueConsultationDTO dto);

    // === ANTÉCÉDENTS MÉDICAUX ===
    void enregistrerAntecedents(Long dossierId, Antecedents antecedents);

    // === EXAMEN CLINIQUE ===
    void enregistrerExamenClinique(Long dossierId, ExamenClinique examen);

    // === EXAMENS COMPLÉMENTAIRES ===
    void enregistrerExamensComplementaires(Long dossierId, ExamensComplementaires examens);

    // === DIAGNOSTIC MÉDICAL ===
    void enregistrerDiagnostic(Long dossierId, DiagnosticMedical diagnostic);

    // === TRAITEMENTS ET PRESCRIPTIONS ===
    void enregistrerTraitements(Long dossierId, TraitementPrescription traitements);

    // === ÉVOLUTION ET SUIVI ===
    void enregistrerEvolutionSuivi(Long dossierId, EvolutionSuivi suivi);
    void genererDossierPourPro(Long proId, int nombreDossiers);
    // === CORRESPONDANCES MÉDICALES ===
    void enregistrerCorrespondances(Long dossierId, Correspondances correspondances);
    List<DossierMedicalDTO> getDossiers();
    List<DossierMedicalDTO> searchDossiers(Long patientId, LocalDate filterDate, String patientName);
}
