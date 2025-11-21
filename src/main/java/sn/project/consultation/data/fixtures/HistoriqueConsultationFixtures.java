package sn.project.consultation.data.fixtures;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import sn.project.consultation.data.entities.DossierMedical;
import sn.project.consultation.data.entities.HistoriqueConsultation;
import sn.project.consultation.data.repositories.DossierMedicalRepository;
import sn.project.consultation.data.repositories.HistoriqueConsultationRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@Order(8)
public class HistoriqueConsultationFixtures implements CommandLineRunner {
    @Autowired
    private DossierMedicalRepository dossierRepo;
    @Autowired
    private HistoriqueConsultationRepository histoRepo;

    @Override
    public void run(String... args) {
        List<DossierMedical> dossiers = dossierRepo.findAll();
        List<HistoriqueConsultation> historiques = new ArrayList<>();

        for (DossierMedical dossier : dossiers) {
            for (int i = 0; i < 2; i++) {
                HistoriqueConsultation h = new HistoriqueConsultation();
                h.setDate(LocalDate.now().minusDays(i * 15));
                h.setNotes("Consultation numéro " + (i + 1));
                h.setTraitement("Traitement prescrit pour épisode " + (i + 1));
                h.setDossier(dossier);
                historiques.add(h);
            }
        }

        histoRepo.saveAll(historiques);
    }
}
