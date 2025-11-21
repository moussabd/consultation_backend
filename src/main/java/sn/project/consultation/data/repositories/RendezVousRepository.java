package sn.project.consultation.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sn.project.consultation.data.entities.ProSante;
import sn.project.consultation.data.entities.RendezVous;

import java.time.LocalDateTime;
import java.util.List;

public interface RendezVousRepository extends JpaRepository<RendezVous, Long> {
    List<RendezVous> findByPatientId(Long patientId);
    List<RendezVous> findByProsanteId(Long proSanteId);
    List<RendezVous> findByProsanteIdAndDateHeureAfterOrderByDateHeureAsc(Long professionnelId, LocalDateTime dateHeure);
    List<RendezVous> findByDateHeureBetween(LocalDateTime start, LocalDateTime end);
    List<RendezVous> findByDateHeureBefore(LocalDateTime dateTime);

    List<RendezVous> findByProsanteAndDateHeure(ProSante prosante, LocalDateTime dateHeure);

    @Query("SELECT r FROM RendezVous r WHERE r.prosante = :prosante AND DATE(r.dateHeure) = DATE(:dateHeure)")
    List<RendezVous> findByProsanteAndDate(@Param("prosante") ProSante prosante, @Param("dateHeure") LocalDateTime dateHeure);
}