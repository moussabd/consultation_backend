package sn.project.consultation.services.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sn.project.consultation.api.dto.*;
import sn.project.consultation.data.entities.*;
import sn.project.consultation.data.repositories.*;
import sn.project.consultation.services.DossierMedicalService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


@Service
public class DossierMedicalServiceImpl implements DossierMedicalService {

    @Autowired private DossierMedicalRepository dossierRepo;
    @Autowired private PatientRepository patientRepo;
    @Autowired private ProSanteRepository proSanteRepo;
    @Autowired private DocumentMedicalRepository documentRepo;
    @Autowired private FichierMedicalRepository fichierRepo;
    @Autowired private HistoriqueConsultationRepository historiqueRepo;
    private Random random;

    // === CRUD GLOBAL ===

    public DossierMedicalDTO creerDossier(Long patientId) {
        Patient patient = patientRepo.findById(patientId).orElseThrow(() -> new RuntimeException("Patient non trouvé"));
        DossierMedical dossier = new DossierMedical();
        dossier.setPatient(patient);
        dossier = dossierRepo.save(dossier);
        return DossierMedicalDTO.fromEntity(dossier);
    }

    public List<DossierMedicalDTO> getDossiersByPatientId(Long id) {
        return DossierMedicalDTO.fromEntities(
                dossierRepo.findByPatientId(id));
    }

    public List<DossierMedicalDTO> getDossiersByProSanteId(Long id) {
        return DossierMedicalDTO.fromEntities(
                dossierRepo.findByProSanteId(id));
    }

    public List<DossierMedicalDTO> getDossiers() {
        return DossierMedicalDTO.fromEntities(
                dossierRepo.findAllWithDetails());

    }

    public List<DossierMedicalDTO> searchDossiers(Long patientId, LocalDate filterDate, String patientName) {
        List<DossierMedical> dossiers;

        if (patientId != null) {
            dossiers = dossierRepo.findByPatientId(patientId);
        } else if (filterDate != null) {
            dossiers = dossierRepo.findByDateCreation(filterDate);
        } else if (patientName != null && !patientName.isEmpty()) {
            dossiers = dossierRepo.findByPatientNomContainingIgnoreCase(patientName);
        } else {
            dossiers = dossierRepo.findAll();
        }

        return dossiers.stream()
                .map(DossierMedicalDTO::fromEntity)
                .collect(Collectors.toList());
    }


    private DossierMedical getDossier(Long id) {
        return dossierRepo.findById(id).orElseThrow(() -> new RuntimeException("Dossier introuvable"));
    }

    // === DOCUMENTS MÉDICAUX ===

    public void ajouterDocument(Long dossierId, FichierMedicalDTO dto) {
        DossierMedical dossier = getDossier(dossierId);
        FichierMedical doc = FichierMedicalDTO.toEntity(dto);
        dossier.getDocuments().add(doc);
        fichierRepo.save(doc);
    }

    public void ajouterFichierAnnexe(Long dossierId, FichierMedicalDTO dto) {
        DossierMedical dossier = getDossier(dossierId);
        FichierMedical fichier = FichierMedicalDTO.toEntity(dto);
        fichierRepo.save(fichier);
    }

    // === HISTORIQUE DE CONSULTATIONS ===

    public void ajouterHistorique(Long dossierId, HistoriqueConsultationDTO dto) {
        DossierMedical dossier = getDossier(dossierId);
        HistoriqueConsultation histo = HistoriqueConsultationDTO.toEntity(dto);
        dossier.getHistoriques().add(histo);
        historiqueRepo.save(histo);
    }

    // === ANTÉCÉDENTS MÉDICAUX ===

    public void enregistrerAntecedents(Long dossierId, Antecedents antecedents) {
        DossierMedical dossier = getDossier(dossierId);
        dossier.setAntecedents(antecedents);
        dossierRepo.save(dossier);
    }

    // === EXAMEN CLINIQUE ===

    public void enregistrerExamenClinique(Long dossierId, ExamenClinique examen) {
        DossierMedical dossier = getDossier(dossierId);
        dossier.setExamenClinique(examen);
        dossierRepo.save(dossier);
    }

    public void enregistrerInfosPrincipales(Long dossierId, InfosUrgenceDTO infos) {
        DossierMedical dossier = getDossier(dossierId);
        dossier.setCouvertureSociale(infos.getCouvertureSociale());
        dossier.setTelPersonneUrgence(infos.getTelPersonneUrgence());
        dossier.setPersonneUrgence(infos.getPersonneUrgence());
        dossierRepo.save(dossier);
    }

    // === EXAMENS COMPLÉMENTAIRES ===

    public void enregistrerExamensComplementaires(Long dossierId, ExamensComplementaires examens) {
        DossierMedical dossier = getDossier(dossierId);
        dossier.setExamensComplementaires(examens);
        dossierRepo.save(dossier);
    }

    // === DIAGNOSTIC MÉDICAL ===

    public void enregistrerDiagnostic(Long dossierId, DiagnosticMedical diagnostic) {
        DossierMedical dossier = getDossier(dossierId);
        dossier.setDiagnosticMedical(diagnostic);
        dossierRepo.save(dossier);
    }

    // === TRAITEMENTS ET PRESCRIPTIONS ===

    public void enregistrerTraitements(Long dossierId, TraitementPrescription traitements) {
        DossierMedical dossier = getDossier(dossierId);
        dossier.setTraitements(traitements);
        dossierRepo.save(dossier);
    }

    // === ÉVOLUTION ET SUIVI ===

    public void enregistrerEvolutionSuivi(Long dossierId, EvolutionSuivi suivi) {
        DossierMedical dossier = getDossier(dossierId);
        dossier.setEvolutionSuivi(suivi);
        dossierRepo.save(dossier);
    }

    // === CORRESPONDANCES MÉDICALES ===

    public void enregistrerCorrespondances(Long dossierId, Correspondances correspondances) {
        DossierMedical dossier = getDossier(dossierId);
        dossier.setCorrespondances(correspondances);
        dossierRepo.save(dossier);
    }

    @Transactional
    public void genererDossierPourPatient(Long patientId, int nombreDossiers) {
        Patient patient = patientRepo.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient introuvable"));

        List<DossierMedical> dossiers = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < nombreDossiers; i++) {
            DossierMedical dossier = new DossierMedical();
            dossier.setPatient(patient);
            dossier.setResume("Dossier médical auto-généré pour " + patient.getNom());
            dossier.setCouvertureSociale("CNAM");
            dossier.setPersonneUrgence("Contact " + patient.getNom() + " Famille");
            dossier.setTelPersonneUrgence("77 123 45 67");
            dossier.setDate(LocalDate.now().minusDays(random.nextInt(200)));
            Antecedents ant = new Antecedents();
            ant.setAntecedentsMedicaux(List.of("Hypertension", "Asthme"));
            ant.setAntecedentsChirurgicaux(List.of("Appendicectomie en 2018"));
            ant.setAntecedentsObstetricaux(List.of("Grossesse 2020"));
            ant.setAntecedentsPsychologiques(List.of("Anxiété légère"));
            ant.setMaladiesFamiliales(List.of("Diabète (père)", "Cancer du sein (mère)"));
            ant.setAllergies(List.of("Pénicilline"));
            dossier.setAntecedents(ant);
            // ⚡ Tu peux réutiliser la logique de tes Fixtures (antécédents, examens, etc.)

            dossiers.add(dossier);
        }

        dossierRepo.saveAll(dossiers);
    }

    @Transactional
    public void genererDossierPourPro(Long proId, int nombreDossiers) {
        ProSante pro = proSanteRepo.findById(proId)
                .orElseThrow(() -> new RuntimeException("Professionnel de santé introuvable"));

        List<Patient> patients = patientRepo.findAll();
        if (patients.isEmpty()) {
            throw new RuntimeException("Aucun patient trouvé pour générer les dossiers.");
        }

        Random random = new Random();
        List<DossierMedical> dossiers = new ArrayList<>();

        for (int i = 0; i < nombreDossiers; i++) {
            // 🔹 Sélection d’un patient aléatoire
            Patient patient = patients.get(random.nextInt(patients.size()));

            DossierMedical dossier = new DossierMedical();
            dossier.setPatient(patient);
            dossier.setResume("Dossier suivi par " + pro.getNom());
            dossier.setCouvertureSociale("IPM - Mutuelle");
            dossier.setPersonneUrgence("Contact " + patient.getNom() + " Famille");
            dossier.setTelPersonneUrgence("76 321 45 89");
            dossier.setDate(LocalDate.now().minusDays(random.nextInt(100)));

            // 🔹 Exemple d’examen clinique lié au pro
            ExamenClinique examen = new ExamenClinique();
            examen.setPoids((double) (70 + random.nextInt(15)));
            examen.setTaille(1.60 + (random.nextDouble() * 0.3));
            examen.setTensionArterielle("12/" + (7 + random.nextInt(3)));
            examen.setTemperature(36.5 + (random.nextDouble()));
            examen.setFrequenceCardiaque(70 + random.nextInt(15));
            examen.setSaturationOxygene(96 + random.nextInt(3));
            examen.setObservations(List.of("Consultation effectuée par " + pro.getNom()));
            dossier.setExamenClinique(examen);

            // 🔹 Diagnostic
            DiagnosticMedical diag = new DiagnosticMedical();
            diag.setDiagnosticPrincipal("Contrôle général");
            diag.setCodePrincipal("Z00.0");
            diag.setSystemeCodification("CIM-10");
            dossier.setDiagnosticMedical(diag);

            // 🔹 Exemple de correspondance (LettreConfrere)
            LettreConfrere lc = new LettreConfrere();
            lc.setAuteur(pro);
            lc.setDestinataire(pro); // ou un autre pro choisi
            lc.setPatient(patient);
            lc.setMotifConsultation("Suivi régulier");
            lc.setDiagnostic("RAS");
            lc.setTraitementPropose(List.of("Repos", "Hydratation"));
            lc.setRecommandationsSuivi(List.of("Revenir dans 6 mois"));

            Correspondances correspondances = new Correspondances();
            correspondances.setLettreConfrere(lc);
            dossier.setCorrespondances(correspondances);

            dossiers.add(dossier);
        }

        dossierRepo.saveAll(dossiers);
    }




}

