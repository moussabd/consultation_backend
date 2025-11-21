package sn.project.consultation.services;


import sn.project.consultation.api.dto.DisponibiliteDTO;

import java.time.LocalDate;
import java.util.List;

public interface DisponibiliteService {
    List<DisponibiliteDTO> getDisponibilites(Long professionnelId, LocalDate date);
    void majDisponibilite(Long id, boolean disponible);
}
