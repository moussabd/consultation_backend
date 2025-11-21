package sn.project.consultation.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.project.consultation.data.entities.FichierMedical;
import sn.project.consultation.data.entities.Message;

public interface FichierMedicalRepository extends JpaRepository<FichierMedical, Long> {
}
