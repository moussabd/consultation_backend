package sn.project.consultation.data.fixtures;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import sn.project.consultation.data.entities.*;
import sn.project.consultation.data.repositories.DossierMedicalRepository;
import sn.project.consultation.data.repositories.PatientRepository;
import sn.project.consultation.data.repositories.ProSanteRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
@Component
@Order(4)
public class DossierMedicalFixtures implements CommandLineRunner {

    @Autowired
    private DossierMedicalRepository dossierRepo;

    @Autowired
    private PatientRepository patientRepo;

    @Autowired
    private ProSanteRepository proSanteRepository;

    @Override
    public void run(String... args) {
        System.out.println("Dossier Medical Fixtures Started....");

        List<Patient> patients = patientRepo.findAll();
        List<ProSante> pros = proSanteRepository.findAll();
        Random random = new Random();

        List<DossierMedical> dossiers = new ArrayList<>();

        for (Patient p : patients) {
            DossierMedical dossier = new DossierMedical();
            dossier.setPatient(p);
            dossier.setResume("Dossier médical du patient " + p.getNom());
            dossier.setCouvertureSociale("CNAM");
            dossier.setPersonneUrgence("Contact " + p.getNom() + " Famille");
            dossier.setTelPersonneUrgence("77 123 45 67");
            dossier.setDate(LocalDate.now().minusDays(random.nextInt(200)));

            // 🔹 Antécédents
            Antecedents ant = new Antecedents();
            ant.setAntecedentsMedicaux(List.of("Hypertension", "Asthme"));
            ant.setAntecedentsChirurgicaux(List.of("Appendicectomie en 2018"));
            ant.setAntecedentsObstetricaux(List.of("Grossesse 2020"));
            ant.setAntecedentsPsychologiques(List.of("Anxiété légère"));
            ant.setMaladiesFamiliales(List.of("Diabète (père)", "Cancer du sein (mère)"));
            ant.setAllergies(List.of("Pénicilline"));
            dossier.setAntecedents(ant);

            // 🔹 Examen clinique
            ExamenClinique examen = new ExamenClinique();
            examen.setPoids(75.0);
            examen.setTaille(1.78);
            examen.setTensionArterielle("12/8");
            examen.setTemperature(37.2);
            examen.setFrequenceCardiaque(80);
            examen.setSaturationOxygene(98);
            examen.setBilanPhysique(List.of(
                    new ElementBilanPhysique("Poumons", "Absence de râles"),
                    new ElementBilanPhysique("Cœur", "Rythme régulier")
            ));
            examen.setObservations(List.of("Bonne hygiène générale", "Pas de détresse respiratoire"));
            dossier.setExamenClinique(examen);

            // 🔹 Examens complémentaires
            ExamensComplementaires exams = new ExamensComplementaires();
            exams.setAnalysesSanguines(List.of(new AnalyseBiologique("Hb", "13 g/dL"), new AnalyseBiologique("Glycémie", "1.1 g/L")));
            exams.setAnalysesUrines(List.of(new AnalyseBiologique("Protéines", "Absentes")));
            exams.setRadiographies(List.of("Radio thoracique normale"));
            exams.setEchographies(List.of("Échographie abdominale : foie normal"));
            exams.setScanners(List.of("Scanner cérébral sans anomalies"));
            exams.setIrm(List.of("IRM lombaire : hernie discale L4-L5"));
            exams.setTestsSpeciaux(List.of(new TestSpecial("Test VIH", "Négatif")));
            dossier.setExamensComplementaires(exams);

            // 🔹 Diagnostic
            DiagnosticMedical diag = new DiagnosticMedical();
            diag.setDiagnosticPrincipal("Pneumonie bactérienne");
            diag.setCodePrincipal("J18.9");
            diag.setSystemeCodification("CIM-10");
            diag.setDiagnosticsSecondaires(List.of("Hypertension", "Asthme"));
            dossier.setDiagnosticMedical(diag);

            // 🔹 Traitements
            TraitementPrescription traitement = new TraitementPrescription();
            MedicamentPrescrit med1 = new MedicamentPrescrit();
            med1.setNom("Amoxicilline");
            med1.setPosologie("1g 3x/jour");
            med1.setVoie("orale");
            med1.setDuree(7);
            med1.setCommentaire("Avec repas");

            MedicamentPrescrit med2 = new MedicamentPrescrit();
            med2.setNom("Paracétamol");
            med2.setPosologie("500mg si douleur");
            med2.setVoie("orale");
            med2.setDuree(5);
            med2.setCommentaire("Max 3g/jour");

            traitement.setMedicaments(List.of(med1, med2));

            SoinsParamedicaux soin1 = new SoinsParamedicaux();
            soin1.setTypeSoin("Pansement");
            soin1.setFrequence("Quotidien");
            soin1.setCommentaire("Chirurgie mineure");

            SoinsParamedicaux soin2 = new SoinsParamedicaux();
            soin2.setTypeSoin("Kinésithérapie");
            soin2.setFrequence("2x/semaine");
            soin2.setCommentaire("Rééducation respiratoire");

            traitement.setSoins(List.of(soin1, soin2));

            InterventionChirurgicale intervention = new InterventionChirurgicale();
            intervention.setNom("Appendicectomie");
            intervention.setDatePrevue(LocalDate.of(2018, 5, 20));
            intervention.setObjectif("Curatif");
            intervention.setCommentaire("Rien à signaler");

            traitement.setInterventions(List.of(intervention));
            dossier.setTraitements(traitement);

            // 🔹 Evolution & suivi
            EvolutionSuivi evol = new EvolutionSuivi();
            evol.setNotesEvolution(List.of("Amélioration après antibiotiques", "Contrôle prévu dans 7 jours"));
            ConsultationSuivi cs1 = new ConsultationSuivi();
            cs1.setDate(LocalDate.now().minusDays(10));
            cs1.setMedecin("Dr. Diop");
            cs1.setResume("Prescription d'antibiotiques");
            ConsultationSuivi cs2 = new ConsultationSuivi();
            cs2.setDate(LocalDate.now().minusDays(5));
            cs2.setMedecin("Dr. Ndiaye");
            cs2.setResume("Évolution favorable");
            evol.setConsultationsSuivi(List.of(cs1, cs2));
            evol.setCourbes(new ArrayList<>());
            evol.setDossierMedical(dossier);
            dossier.setEvolutionSuivi(evol);

            // 🔹 Correspondances
            Correspondances correspondances = new Correspondances();

            // CompteRenduHospitalisation
            CompteRenduHospitalisation crh = new CompteRenduHospitalisation();
            ProSante auteurCRH = pros.get(random.nextInt(pros.size()));
            ProSante destinataireCRH;
            do {
                destinataireCRH = pros.get(random.nextInt(pros.size()));
            } while (destinataireCRH.getId().equals(auteurCRH.getId()));


            crh.setAuteur(auteurCRH);
            crh.setDestinataire(destinataireCRH);
            crh.setPatient(p);
            crh.setDateAdmission(LocalDate.now().minusDays(7));
            crh.setDateSortie(LocalDate.now());
            crh.setDiagnosticAdmission(List.of("Fièvre", "Toux"));
            crh.setDiagnosticSortie(List.of("Pneumonie résolue"));
            crh.setExamensEffectues(List.of("Radiographie thoracique", "Analyse sanguine"));
            crh.setTraitements(List.of("Amoxicilline 1g 3x/jour"));
            crh.setEvolution(List.of("Amélioration clinique"));
            crh.setRecommandationsSortie(List.of("Reprise normale des activités"));
            correspondances.setCompteRenduHospitalisation(crh);

            // CompteRenduOperatoire
            CompteRenduOperatoire cro = new CompteRenduOperatoire();
            ProSante auteurCRO = pros.get(random.nextInt(pros.size()));
            ProSante destinataireCRO;
            do {
                destinataireCRO = pros.get(random.nextInt(pros.size()));
            } while (destinataireCRO.getId().equals(auteurCRO.getId()));
            cro.setAuteur(auteurCRO);
            cro.setDestinataire(destinataireCRO);
            cro.setPatient(p);
            cro.setNomIntervention("Appendicectomie");
            cro.setIndicationOperatoire("Appendicite aiguë");
            cro.setDescriptionActe("Ablation de l'appendice sans complication");
            cro.setComplications(List.of("Aucune"));
            cro.setConclusion("Bonne évolution post-opératoire");
            correspondances.setCompteRenduOperatoire(cro);

            // LettreConfrere
            LettreConfrere lc = new LettreConfrere();
            ProSante auteurLC = pros.get(random.nextInt(pros.size()));
            ProSante destinataireLC;
            do {
                destinataireLC = pros.get(random.nextInt(pros.size()));
            } while (destinataireLC.getId().equals(auteurLC.getId()));

            lc.setAuteur(auteurLC);
            lc.setDestinataire(destinataireLC);
            lc.setPatient(p);
            lc.setMotifConsultation("Suivi pneumonie");
            lc.setResultatsExamens(List.of("Radiographie normale", "Glycémie normale"));
            lc.setDiagnostic("Pneumonie résolue");
            lc.setTraitementPropose(List.of("Paracétamol si douleur"));
            lc.setRecommandationsSuivi(List.of("Contrôle dans 1 mois"));
            correspondances.setLettreConfrere(lc);

            dossier.setCorrespondances(correspondances);
            System.out.println(dossier.getCorrespondances().getCompteRenduHospitalisation().getAuteur().getNom());
            System.out.println(dossier.getCorrespondances().getCompteRenduHospitalisation().getDestinataire().getNom());
            System.out.println(dossier.getCorrespondances().getCompteRenduHospitalisation().getPatient().getNom());
            System.out.println(dossier.getCorrespondances().getCompteRenduOperatoire().getAuteur().getNom());
            System.out.println(dossier.getCorrespondances().getCompteRenduOperatoire().getDestinataire().getNom());
            System.out.println(dossier.getCorrespondances().getCompteRenduOperatoire().getPatient().getNom());
            System.out.println(dossier.getCorrespondances().getLettreConfrere().getAuteur().getNom());
            System.out.println(dossier.getCorrespondances().getLettreConfrere().getDestinataire().getNom());
            System.out.println(dossier.getCorrespondances().getLettreConfrere().getPatient().getNom());
            dossier.setDocuments(new ArrayList<>());
            dossier.setDocumentsAnnexes(new ArrayList<>());
            dossier.setHistoriques(new ArrayList<>());

            dossiers.add(dossier);
        }

        dossierRepo.saveAll(dossiers);
        System.out.println("Dossier Medical Fixtures Completed.");
    }


}

