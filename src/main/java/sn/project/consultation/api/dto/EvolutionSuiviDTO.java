package sn.project.consultation.api.dto;

import lombok.Getter;
import lombok.Setter;
import sn.project.consultation.data.entities.EvolutionSuivi;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
public class EvolutionSuiviDTO {
    private List<String> notesEvolution;
    private List<CourbeCliniqueDTO> courbes;
    private List<ConsultationSuiviDTO> consultationsSuivi;

    public static EvolutionSuiviDTO toDTO(EvolutionSuivi entity) {
        if (entity == null) return null;
        EvolutionSuiviDTO dto = new EvolutionSuiviDTO();
        dto.setNotesEvolution(entity.getNotesEvolution());
        if (entity.getCourbes() != null) {
            dto.setCourbes(entity.getCourbes()
                    .stream()
                    .map(CourbeCliniqueDTO::toDTO)
                    .collect(Collectors.toList()));
        }
        if (entity.getConsultationsSuivi() != null) {
            dto.setConsultationsSuivi(entity.getConsultationsSuivi()
                    .stream()
                    .map(ConsultationSuiviDTO::toDTO)
                    .collect(Collectors.toList()));
        }
        return dto;
    }

    public static EvolutionSuivi toEntity(EvolutionSuiviDTO dto) {
        if (dto == null) return null;
        EvolutionSuivi entity = new EvolutionSuivi();
        entity.setNotesEvolution(dto.getNotesEvolution());
        if (dto.getCourbes() != null) {
            entity.setCourbes(dto.getCourbes()
                    .stream()
                    .map(CourbeCliniqueDTO::toEntity)
                    .collect(Collectors.toList()));
        }
        if (dto.getConsultationsSuivi() != null) {
            entity.setConsultationsSuivi(dto.getConsultationsSuivi()
                    .stream()
                    .map(ConsultationSuiviDTO::toEntity)
                    .collect(Collectors.toList()));
        }
        return entity;
    }
}