package sn.project.consultation.api.dto;


import lombok.*;
import sn.project.consultation.data.entities.RendezVous;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RendezVousDTO {
    private Long id;
    private PatientDTO patient;
    private ProSanteDTO proSante;
    private LocalDateTime dateHeure;
    private String statut;

    public static RendezVousDTO fromEntity(RendezVous entity) {
        if (entity == null) {
            return null;
        }

        RendezVousDTO dto = new RendezVousDTO();
        dto.setId(entity.getId());
        dto.setPatient(PatientDTO.fromEntity(entity.getPatient()));
        dto.setProSante(ProSanteDTO.fromEntity(entity.getProsante()));
        dto.setDateHeure(entity.getDateHeure());
        dto.setStatut(entity.getStatut());

        return dto;
    }

    public static RendezVous toEntity(RendezVousDTO dto) {
        if (dto == null) {
            return null;
        }

        RendezVous entity = new RendezVous();
        entity.setId(dto.getId());
        entity.setPatient(PatientDTO.toEntity(dto.getPatient()));
        entity.setProsante(ProSanteDTO.toEntity(dto.getProSante()));
        entity.setDateHeure(dto.getDateHeure());
        entity.setStatut(dto.getStatut());

        return entity;
    }

    public static List<RendezVousDTO> fromEntities(List<RendezVous> entities) {
        if (entities == null) {
            return null;
        }

        return entities.stream()
                .map(RendezVousDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
