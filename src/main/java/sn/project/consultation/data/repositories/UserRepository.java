package sn.project.consultation.data.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sn.project.consultation.data.entities.RendezVous;
import sn.project.consultation.data.entities.User;
import sn.project.consultation.data.enums.RoleUser;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.coordonnees.email = :email")
    User findByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.coordonnees.numeroTelephone = :tel")
    Optional<User> findByTelephone(@Param("tel") String numeroTelephone);

    @Query("SELECT u FROM User u WHERE LOWER(u.coordonnees.adresse) LIKE LOWER(CONCAT('%', :adresse, '%'))")
    List<User> searchByAdresse(@Param("adresse") String adresse);

    @Query("SELECT COUNT(u) FROM User u WHERE u.role = :role AND YEAR(u.dateCreation) = :year")
    long countByRoleAndYear(@Param("role") RoleUser role, @Param("year") int year);

    // Compter par rôle
    long countByRole(RoleUser role);

    // Compter les utilisateurs activés
    long countByEnabledTrue();

    // Recherche avancée avec pagination
    @Query("SELECT u FROM User u WHERE " +
            "LOWER(u.nom) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(u.prenom) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(u.coordonnees.email) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<User> findBySearchTerm(@Param("search") String search, Pageable pageable);

    // Vérifier si l'email existe déjà
    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.coordonnees.email = :email")
    boolean existsByEmail(@Param("email") String email);
}
