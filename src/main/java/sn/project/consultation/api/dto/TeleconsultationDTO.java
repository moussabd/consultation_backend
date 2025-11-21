package sn.project.consultation.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import sn.project.consultation.data.entities.Teleconsultation;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeleconsultationDTO {
    private Long id;
    private PatientDTO patient;
    private ProSanteDTO proSante;
    private LocalDateTime dateHeure;
    private String statut;     // EN_ATTENTE, EN_COURS, TERMINEE, ANNULEE
    private String lienVideo;  // Lien sécurisé pour la visioconférence
    private String notes;      // Notes de la consultation

    public static TeleconsultationDTO fromEntity(Teleconsultation entity) {
        if (entity == null) {
            return null;
        }

        TeleconsultationDTO dto = new TeleconsultationDTO();
        dto.setId(entity.getId());
        dto.setPatient(PatientDTO.fromEntity(entity.getPatient()));
        dto.setProSante(ProSanteDTO.fromEntity(entity.getProSante()));
        dto.setDateHeure(entity.getDateHeure());
        dto.setStatut(entity.getStatut());
        dto.setLienVideo(entity.getLienVideo());
        dto.setNotes(entity.getNotes());

        return dto;
    }

    public static Teleconsultation toEntity(TeleconsultationDTO dto) {
        if (dto == null) {
            return null;
        }

        Teleconsultation entity = new Teleconsultation();
        entity.setId(dto.getId());
        entity.setPatient(PatientDTO.toEntity(dto.getPatient()));
        entity.setProSante(ProSanteDTO.toEntity(dto.getProSante()));
        entity.setDateHeure(dto.getDateHeure());
        entity.setStatut(dto.getStatut());
        entity.setLienVideo(dto.getLienVideo());
        entity.setNotes(dto.getNotes());

        return entity;
    }

    public static List<TeleconsultationDTO> fromEntities(List<Teleconsultation> entities) {
        if (entities == null) {
            return null;
        }

        return entities.stream()
                .map(TeleconsultationDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
