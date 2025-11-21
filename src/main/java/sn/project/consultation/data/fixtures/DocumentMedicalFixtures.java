package sn.project.consultation.data.fixtures;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import sn.project.consultation.data.entities.DocumentMedical;
import sn.project.consultation.data.entities.DossierMedical;
import sn.project.consultation.data.repositories.DocumentMedicalRepository;
import sn.project.consultation.data.repositories.DossierMedicalRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Order(5)
public class DocumentMedicalFixtures implements CommandLineRunner {
    @Autowired
    private DossierMedicalRepository dossierRepo;
    @Autowired
    private DocumentMedicalRepository documentRepo;

    @Override
    public void run(String... args) {
        List<DossierMedical> dossiers = dossierRepo.findAll();
        List<DocumentMedical> docs = new ArrayList<>();

        for (DossierMedical dossier : dossiers) {
            for (int i = 0; i < 2; i++) {
                DocumentMedical doc = new DocumentMedical();
                doc.setNom("Document " + (i + 1) + " - " + dossier.getPatient().getNom());
                doc.setUrlStockage("https://example.com/docs/doc_" + UUID.randomUUID() + ".pdf");
                doc.setDossier(dossier);
                docs.add(doc);
            }
        }

        documentRepo.saveAll(docs);
    }
}
