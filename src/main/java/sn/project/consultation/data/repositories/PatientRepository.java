package sn.project.consultation.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sn.project.consultation.data.entities.Patient;
import sn.project.consultation.data.enums.RoleUser;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
  Optional<Patient> findByCoordonneesEmail(String email);
  boolean existsByCoordonneesEmail(String email);
  List<Patient> findByRole(RoleUser role);
  long countByRole(RoleUser role);
  long countByEnabled(boolean enabled);

  @Query("SELECT p FROM Patient p WHERE " +
          "LOWER(p.nom) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
          "LOWER(p.prenom) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
          "LOWER(p.coordonnees.email) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
  List<Patient> findBySearchTerm(@Param("searchTerm") String searchTerm);

  @Query("SELECT COUNT(p) FROM Patient p WHERE p.role = :role AND YEAR(p.dateCreation) = :year")
  long countByRoleAndYear(@Param("role") RoleUser role, @Param("year") int year);

  // Méthodes spécifiques aux patients
  List<Patient> findByLieuNaissance(String lieuNaissance);
  List<Patient> findBySituationFamiliale(String situationFamiliale);
  Optional<Patient> findByMatricule(String matricule);
  Patient findByNomIgnoreCaseAndPrenomIgnoreCase(String nom, String prenom);
}