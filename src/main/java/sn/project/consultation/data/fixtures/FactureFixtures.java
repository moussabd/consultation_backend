package sn.project.consultation.data.fixtures;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import sn.project.consultation.data.entities.*;
import sn.project.consultation.data.repositories.FactureRepository;
import sn.project.consultation.data.repositories.PaiementRepository;
import sn.project.consultation.data.repositories.PatientRepository;
import sn.project.consultation.data.repositories.ProSanteRepository;


import java.time.LocalDateTime;
import java.util.*;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import sn.project.consultation.data.entities.*;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
@Order(6)
public class FactureFixtures implements CommandLineRunner {

    private final FactureRepository factureRepository;
    private final PaiementRepository paiementRepository;
    private final PatientRepository patientRepository;
    private final ProSanteRepository proSanteRepository;

    @Override
    public void run(String... args) throws Exception {
        Random random = new Random();
        List<Patient> patients = patientRepository.findAll();
        List<ProSante> pros = proSanteRepository.findAll();

        int nbFactures = 10 + random.nextInt(11); // 10 à 20 factures
        for (int i = 1; i <= nbFactures; i++) {
            Facture facture = new Facture();
            facture.setNumero("FAC-" + (1000 + i));
            facture.setDateEmission(LocalDateTime.now().minusDays(random.nextInt(30)));

            // Montant fixe de la facture entre 1000 et 5000
            double montantFacture = 1000 + random.nextInt(4001);
            facture.setMontant(montantFacture);

            // Décider si la facture est payée ou non
            boolean estPayee = random.nextBoolean();

            if (estPayee) {
                Paiement paiement = new Paiement();
                paiement.setDatePaiement(LocalDateTime.now().minusDays(random.nextInt(30)));
                paiement.setMontant(montantFacture);
                paiement.setPatient(patients.get(random.nextInt(patients.size())));
                paiement.setProfessionnel(pros.get(random.nextInt(pros.size())));
                paiement.setMethode(randomMethode(random));
                paiement.setStatut("SUCCES");
                paiement.setFacture(facture);

                facture.setPaiement(paiement); // OneToOne
                facture.setEtatPaiement(EtatPaiement.PAYEE);
            } else {
                facture.setEtatPaiement(EtatPaiement.NON_PAYEE);
            }

            factureRepository.save(facture); // cascade persiste le paiement si présent
        }
    }

    private String randomMethode(Random random) {
        String[] methodes = {"Wave", "Orange Money"};
        return methodes[random.nextInt(methodes.length)];
    }
}



