package sn.project.consultation.data.entities;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Embeddable
@Getter
@Setter
public class ConsultationSuivi {
    private LocalDate date;
    private String medecin; // Nom ou identifiant du professionnel
    private String resume;  // Résumé de la consultation
}
