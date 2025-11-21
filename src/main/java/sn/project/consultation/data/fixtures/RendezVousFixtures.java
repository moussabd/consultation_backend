package sn.project.consultation.data.fixtures;


import sn.project.consultation.data.entities.Patient;
import sn.project.consultation.data.entities.ProSante;
import sn.project.consultation.data.entities.RendezVous;
import sn.project.consultation.data.repositories.PatientRepository;
import sn.project.consultation.data.repositories.ProSanteRepository;
import sn.project.consultation.data.repositories.RendezVousRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.*;

@Component
@Order(3)
public class RendezVousFixtures implements CommandLineRunner {

    @Autowired
    private RendezVousRepository rendezVousRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private ProSanteRepository proSanteRepository;

    private static final String[] STATUTS = {"EN_ATTENTE", "VALIDÉ", "ANNULÉ"};

    @Override
    public void run(String... args) {
        List<Patient> patients = patientRepository.findAll();
        List<ProSante> pros = proSanteRepository.findAll();

        if (patients.isEmpty() || pros.isEmpty()) {
            System.out.println("⚠️  Pas de patients ou de professionnels en base. Fixtures annulées.");
            return;
        }

        List<RendezVous> rdvs = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < 30; i++) {
            RendezVous rdv = new RendezVous();
            rdv.setPatient(patients.get(random.nextInt(patients.size())));
            rdv.setProsante(pros.get(random.nextInt(pros.size())));

            // Génère une date aléatoire entre -10 et +10 jours
            int daysOffset = random.nextInt(21) - 10;
            LocalDateTime dateHeure = LocalDateTime.now().plusDays(daysOffset).withHour(9 + random.nextInt(8)).withMinute(0);
            rdv.setDateHeure(dateHeure);

            rdv.setStatut(STATUTS[random.nextInt(STATUTS.length)]);

            rdvs.add(rdv);
        }

        rendezVousRepository.saveAllAndFlush(rdvs);
    }
}
