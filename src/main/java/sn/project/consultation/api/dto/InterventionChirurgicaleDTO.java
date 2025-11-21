package sn.project.consultation.api.dto;


import lombok.Getter;
import lombok.Setter;
import sn.project.consultation.data.entities.InterventionChirurgicale;

import java.time.LocalDate;

@Getter
@Setter
public class InterventionChirurgicaleDTO {

    private String nom;
    private LocalDate datePrevue;
    private String objectif;
    private String commentaire;

    public static InterventionChirurgicaleDTO toDTO(InterventionChirurgicale entity) {
        InterventionChirurgicaleDTO dto = new InterventionChirurgicaleDTO();
        dto.setNom(entity.getNom());
        dto.setDatePrevue(entity.getDatePrevue());
        dto.setObjectif(entity.getObjectif());
        dto.setCommentaire(entity.getCommentaire());
        return dto;
    }

    public static InterventionChirurgicale toEntity(InterventionChirurgicaleDTO dto) {
        InterventionChirurgicale entity = new InterventionChirurgicale();
        entity.setNom(dto.getNom());
        entity.setDatePrevue(dto.getDatePrevue());
        entity.setObjectif(dto.getObjectif());
        entity.setCommentaire(dto.getCommentaire());
        return entity;
    }
}
