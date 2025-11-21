package sn.project.consultation.api.dto;

import lombok.Getter;
import lombok.Setter;
import sn.project.consultation.data.entities.CourbeClinique;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class CourbeCliniqueDTO {
    private String type;
    private List<MesureCliniqueDTO> mesures;

    public static CourbeCliniqueDTO toDTO(CourbeClinique courbe) {
        if (courbe == null) return null;
        CourbeCliniqueDTO dto = new CourbeCliniqueDTO();
        dto.setType(courbe.getType());
        if (courbe.getMesures() != null) {
            dto.setMesures(courbe.getMesures()
                    .stream()
                    .map(MesureCliniqueDTO::toDTO)
                    .collect(Collectors.toList()));
        }
        return dto;
    }

    public static CourbeClinique toEntity(CourbeCliniqueDTO dto) {
        if (dto == null) return null;
        CourbeClinique courbe = new CourbeClinique();
        courbe.setType(dto.getType());
        if (dto.getMesures() != null) {
            courbe.setMesures(dto.getMesures()
                    .stream()
                    .map(MesureCliniqueDTO::toEntity)
                    .collect(Collectors.toList()));
        }
        return courbe;
    }
}
