package sn.project.consultation.data.fixtures;

import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import sn.project.consultation.data.entities.Paiement;
import sn.project.consultation.data.entities.Patient;
import sn.project.consultation.data.entities.ProSante;
import sn.project.consultation.data.repositories.PaiementRepository;
import sn.project.consultation.data.repositories.PatientRepository;
import sn.project.consultation.data.repositories.ProSanteRepository;

import java.time.ZoneId;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import sn.project.consultation.data.entities.Paiement;
import sn.project.consultation.data.repositories.PaiementRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//@Component
//public class PaiementFixtures implements CommandLineRunner {
//
//    private final PaiementRepository paiementRepo;
//    private final Random random = new Random();
//
//    public PaiementFixtures(PaiementRepository paiementRepo) {
//        this.paiementRepo = paiementRepo;
//    }
//
//    @Override
//    public void run(String... args) throws Exception {
//        if (paiementRepo.count() > 0) return;
//
//        int nbPaiements = 3 + random.nextInt(18); // Entre 3 et 20
//        List<Paiement> paiements = new ArrayList<>();
//
//        for (int i = 1; i <= nbPaiements; i++) {
//            Paiement p = new Paiement();
//            p.setMontant((double) (10000 + random.nextInt(50001))); // Montant entre 10 000 et 60 000
//            p.setDatePaiement(LocalDateTime.now().minusDays(random.nextInt(30))); // Paiement dans les 30 derniers jours
//            p.setMethode(random.nextBoolean() ? "CARTE" : "ESPECE");
//            p.setStatut(random.nextBoolean() ? "SUCCES" : "EN_ATTENTE");
//            p.setFacture(null); // Aucun lien direct ici, sera lié depuis FactureFixtures si besoin
//            paiements.add(p);
//        }
//
//        paiementRepo.saveAll(paiements);
//        System.out.println("✅ " + nbPaiements + " paiements seedés");
//    }
//}
