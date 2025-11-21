package sn.project.consultation.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.project.consultation.data.entities.Evaluation;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {}