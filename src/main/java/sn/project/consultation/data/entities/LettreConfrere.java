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
public class LettreConfrere extends CorrespondanceMedicale {
    private String motifConsultation;

    @ElementCollection
    private List<String> resultatsExamens;
    private String diagnostic;

    @ElementCollection
    private List<String> traitementPropose;

    @ElementCollection
    private List<String> recommandationsSuivi;
}