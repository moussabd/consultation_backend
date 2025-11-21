package sn.project.consultation.data.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import sn.project.consultation.data.entities.Disponibilite;

import java.time.LocalDate;
import java.util.List;

public interface DisponibiliteRepository extends JpaRepository<Disponibilite, Long> {
    List<Disponibilite> findByProfessionnelIdAndDate(Long professionnelId, LocalDate date);
    List<Disponibilite> findByProfessionnelIdAndDisponibleTrue(Long professionnelId);
}
