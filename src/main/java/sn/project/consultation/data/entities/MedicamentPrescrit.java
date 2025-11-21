package sn.project.consultation.data.entities;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class MedicamentPrescrit {
    private String nom;           // Ex: Amoxicilline
    private String posologie;     // Ex: 500mg 3x/jour
    private String voie;          // Ex: orale, intraveineuse
    private Integer duree;        // en jours
    private String commentaire;   // facultatif
}
