package sn.project.consultation.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.project.consultation.data.entities.DocumentMedical;
import sn.project.consultation.data.entities.DossierMedical;

import java.util.List;

public interface DocumentMedicalRepository extends JpaRepository<DocumentMedical, Long> {

    // ✅ Récupérer tous les documents liés à un dossier médical par son ID
    List<DocumentMedical> findByDossierId(Long dossierId);
}
