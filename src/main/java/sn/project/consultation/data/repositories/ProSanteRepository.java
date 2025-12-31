package sn.project.consultation.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sn.project.consultation.data.entities.ProSante;

import java.util.List;

public interface ProSanteRepository extends JpaRepository<ProSante, Long> {

    // Recherche par spécialité (insensible à la casse)
    List<ProSante> findBySpecialiteContainingIgnoreCase(String specialite);

    // Recherche combinée par nom, prénom ou spécialité (insensible à la casse)
    @Query("SELECT p FROM ProSante p " +
           "WHERE (:nom IS NULL OR p.nom ILIKE CONCAT('%', :nom, '%') " +
           "OR p.prenom ILIKE CONCAT('%', :nom, '%')) " +
           "AND (:specialite IS NULL OR p.specialite ILIKE CONCAT('%', :specialite, '%'))")
    List<ProSante> findByNomPrenomOrSpecialite(@Param("nom") String nom,
                                               @Param("specialite") String specialite);

    // Recherche exacte par spécialité
    List<ProSante> findBySpecialite(String specialite);

    // Filtrage par tarif maximal
    List<ProSante> findByTarifLessThanEqual(Double tarif);

    // Recherche exacte par nom et prénom (insensible à la casse)
    ProSante findByNomIgnoreCaseAndPrenomIgnoreCase(String nom, String prenom);
}
