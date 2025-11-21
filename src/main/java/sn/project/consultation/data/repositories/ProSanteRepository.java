package sn.project.consultation.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sn.project.consultation.data.entities.ProSante;

import java.util.List;
import java.util.Optional;

public interface ProSanteRepository extends JpaRepository<ProSante, Long> {
    List<ProSante> findBySpecialiteContainingIgnoreCase(String specialite);
    @Query("SELECT p FROM ProSante p WHERE" + "(:nom IS NULL OR LOWER(p.nom) LIKE LOWER(CONCAT('%', :nom, '%'))" +
            "OR LOWER(p.prenom) LIKE LOWER(CONCAT('%',:nom,'%'))) AND (:specialite IS NULL OR LOWER(p.specialite) " +
            "LIKE LOWER(CONCAT('%', :specialite, '%')))")
    List<ProSante> findByNomPrenomOrSpecialite(@Param("nom") String nom,
                                               @Param("specialite") String specialite);
    List<ProSante> findBySpecialite(String specialite);
    List<ProSante> findByTarifLessThanEqual(Double tarif);
    ProSante findByNomIgnoreCaseAndPrenomIgnoreCase(String nom, String prenom);
}