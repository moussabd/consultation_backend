package sn.project.consultation.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.project.consultation.data.entities.Message;
import sn.project.consultation.data.entities.RendezVous;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByTeleconsultationIdOrderByDateEnvoiAsc(Long teleconsultationId);
}
