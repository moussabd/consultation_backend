package sn.project.consultation.data.entities;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class SoinsParamedicaux {
    private String typeSoin;       // Ex: Pansement, Injection
    private String frequence;      // Ex: Quotidien, 2x/semaine
    private String commentaire;    // Facultatif
}
