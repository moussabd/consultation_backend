package sn.project.consultation.data.entities;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class CompteRenduOperatoire extends CorrespondanceMedicale {
    private String nomIntervention;
    private String indicationOperatoire;
    private String descriptionActe;
    @ElementCollection
    private List<String> complications; // Peut être null si aucune
    private String conclusion;
}
