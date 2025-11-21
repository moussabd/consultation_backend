package sn.project.consultation.data.entities;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Embeddable
@Getter
@Setter
public class InterventionChirurgicale {
    private String nom;            // Ex: Appendicectomie
    private LocalDate datePrevue; // Si connue
    private String objectif;       // Diagnostique, curatif, préventif
    private String commentaire;    // Risques ou précisions
}
