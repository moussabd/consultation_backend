package sn.project.consultation.api.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
public class PlanificationDTO {
    private Long patientId;
    private Long medecinId;
    private LocalDateTime dateHeure;
    private String lienVideo;
    private String statut;
}