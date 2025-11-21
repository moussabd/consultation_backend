package sn.project.consultation.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.project.consultation.data.entities.Admin;
import sn.project.consultation.data.enums.RoleUser;

import java.util.List;
import java.util.Optional;


public interface AdminRepository extends JpaRepository<Admin, Long> {

    // Trouver un admin par email
    Optional<Admin> findByCoordonneesEmail(String email);

    // Vérifier si un admin existe par email
    boolean existsByCoordonneesEmail(String email);

    // Trouver tous les admins par rôle
    List<Admin> findByRole(RoleUser role);

    // Compter les admins par rôle
    long countByRole(RoleUser role);

    // Compter les admins activés/désactivés
    long countByEnabled(boolean enabled);

    // Trouver les admins par département
    List<Admin> findByDepartement(String departement);

    // Trouver les super administrateurs
    List<Admin> findByEstSuperAdminTrue();

    // Recherche d'admins par nom, prénom ou email
    @Query("SELECT a FROM Admin a WHERE " +
            "LOWER(a.nom) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(a.prenom) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(a.coordonnees.email) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Admin> findBySearchTerm(@Param("searchTerm") String searchTerm);

    // Compter les admins créés dans une année spécifique
    @Query("SELECT COUNT(a) FROM Admin a WHERE YEAR(a.dateCreation) = :year")
    long countByYear(@Param("year") int year);
}