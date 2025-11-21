package sn.project.consultation.data.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sn.project.consultation.data.entities.DossierMedical;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DossierMedicalRepository extends JpaRepository<DossierMedical, Long> {

    List<DossierMedical> findByPatientId(Long patientId);
    List<DossierMedical> findByProSanteId(Long proId);

    @Query("""
    SELECT DISTINCT d FROM DossierMedical d
    LEFT JOIN FETCH d.patient
    LEFT JOIN FETCH d.documents
    LEFT JOIN FETCH d.evolutionSuivi es
""")
    List<DossierMedical> findAllWithDetails();

    @Query("SELECT d FROM DossierMedical d WHERE DATE(d.date) = :date")
    List<DossierMedical> findByDateCreation(@Param("date") LocalDate date);

    List<DossierMedical> findByPatientNomContainingIgnoreCase(String nom);


}