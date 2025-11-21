package sn.project.consultation.api.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import sn.project.consultation.data.entities.Message;
import sn.project.consultation.data.entities.Patient;
import sn.project.consultation.data.entities.ProSante;
import sn.project.consultation.data.entities.Teleconsultation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Getter
@Setter
public class MessageDTO {
    private Long teleconsultationId;
    private Long expediteurId;
    private String expediteurType; // "PATIENT", "PRO_SANTE", ou null
    private String contenu;
    private LocalDateTime dateEnvoi;
    private String source; // "JITSI", "APP", "SYSTEM", etc.

    public static MessageDTO fromEntity(Message entity) {
        if (entity == null) {
            return null;
        }

        MessageDTO dto = new MessageDTO();
        dto.setTeleconsultationId(entity.getTeleconsultation() != null ? entity.getTeleconsultation().getId() : null);
        dto.setExpediteurId(entity.getExpediteur() != null ? entity.getExpediteur().getId() : null);
        dto.setContenu(entity.getContenu());
        dto.setDateEnvoi(entity.getDateEnvoi());
        dto.setSource(entity.getSource());

        // Déduire le type d’expéditeur selon l’instance
        if (entity.getExpediteur() instanceof ProSante) {
            dto.setExpediteurType("PRO_SANTE");
        } else if (entity.getExpediteur() instanceof Patient) {
            dto.setExpediteurType("PATIENT");
        }

        return dto;
    }

    public static Message toEntity(MessageDTO dto) {
        if (dto == null) {
            return null;
        }

        Message entity = new Message();
        entity.setDateEnvoi(dto.getDateEnvoi());
        entity.setContenu(dto.getContenu());
        entity.setSource(dto.getSource());

        if (dto.getTeleconsultationId() != null) {
            Teleconsultation t = new Teleconsultation();
            t.setId(dto.getTeleconsultationId());
            entity.setTeleconsultation(t);
        }

        if ("PATIENT".equalsIgnoreCase(dto.getExpediteurType())) {
            Patient patient = new Patient();
            patient.setId(dto.getExpediteurId());
            entity.setExpediteur(patient);
        } else if ("PRO_SANTE".equalsIgnoreCase(dto.getExpediteurType())) {
            ProSante pro = new ProSante();
            pro.setId(dto.getExpediteurId());
            entity.setExpediteur(pro);
        }

        return entity;
    }

    public static List<MessageDTO> fromEntities(List<Message> entities) {
        if (entities == null) {
            return null;
        }

        return entities.stream()
                .map(MessageDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public static List<Message> toEntities(List<MessageDTO> dtos) {
        if (dtos == null) {
            return null;
        }

        return dtos.stream()
                .map(MessageDTO::toEntity)
                .collect(Collectors.toList());
    }
}
