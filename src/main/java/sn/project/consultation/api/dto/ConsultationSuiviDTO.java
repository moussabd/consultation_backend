package sn.project.consultation.api.dto;

import lombok.Getter;
import lombok.Setter;
import sn.project.consultation.data.entities.ConsultationSuivi;

import java.time.LocalDate;

@Getter
@Setter
public class ConsultationSuiviDTO {
    private LocalDate date;
    private String medecin;
    private String resume;

    public static ConsultationSuiviDTO toDTO(ConsultationSuivi c) {
        if (c == null) return null;
        ConsultationSuiviDTO dto = new ConsultationSuiviDTO();
        dto.setDate(c.getDate());
        dto.setMedecin(c.getMedecin());
        dto.setResume(c.getResume());
        return dto;
    }

    public static ConsultationSuivi toEntity(ConsultationSuiviDTO dto) {
        if (dto == null) return null;
        ConsultationSuivi c = new ConsultationSuivi();
        c.setDate(dto.getDate());
        c.setMedecin(dto.getMedecin());
        c.setResume(dto.getResume());
        return c;
    }
}
