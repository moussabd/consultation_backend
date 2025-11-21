package sn.project.consultation.api.dto;

import lombok.Getter;
import lombok.Setter;
import sn.project.consultation.data.entities.SoinsParamedicaux;

@Getter
@Setter
public class SoinsParamedicauxDTO {

    private String typeSoin;
    private String frequence;
    private String commentaire;

    public static SoinsParamedicauxDTO toDTO(SoinsParamedicaux entity) {
        SoinsParamedicauxDTO dto = new SoinsParamedicauxDTO();
        dto.setTypeSoin(entity.getTypeSoin());
        dto.setFrequence(entity.getFrequence());
        dto.setCommentaire(entity.getCommentaire());
        return dto;
    }

    public static SoinsParamedicaux toEntity(SoinsParamedicauxDTO dto) {
        SoinsParamedicaux entity = new SoinsParamedicaux();
        entity.setTypeSoin(dto.getTypeSoin());
        entity.setFrequence(dto.getFrequence());
        entity.setCommentaire(dto.getCommentaire());
        return entity;
    }
}
