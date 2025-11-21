package sn.project.consultation.api.dto;

import lombok.Getter;
import lombok.Setter;
import sn.project.consultation.data.entities.MedicamentPrescrit;

@Getter
@Setter
public class MedicamentPrescritDTO {

    private String nom;
    private String posologie;
    private String voie;
    private Integer duree;
    private String commentaire;

    public static MedicamentPrescritDTO toDTO(MedicamentPrescrit entity) {
        MedicamentPrescritDTO dto = new MedicamentPrescritDTO();
        dto.setNom(entity.getNom());
        dto.setPosologie(entity.getPosologie());
        dto.setVoie(entity.getVoie());
        dto.setDuree(entity.getDuree());
        dto.setCommentaire(entity.getCommentaire());
        return dto;
    }

    public static MedicamentPrescrit toEntity(MedicamentPrescritDTO dto) {
        MedicamentPrescrit entity = new MedicamentPrescrit();
        entity.setNom(dto.getNom());
        entity.setPosologie(dto.getPosologie());
        entity.setVoie(dto.getVoie());
        entity.setDuree(dto.getDuree());
        entity.setCommentaire(dto.getCommentaire());
        return entity;
    }
}
