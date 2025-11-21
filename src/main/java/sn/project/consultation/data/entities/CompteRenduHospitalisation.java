package sn.project.consultation.data.entities;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
public class CompteRenduHospitalisation extends CorrespondanceMedicale {
    private LocalDate dateAdmission;
    private LocalDate dateSortie;

    @ElementCollection
    private List<String> diagnosticAdmission;

    @ElementCollection
    private List<String> diagnosticSortie;

    @ElementCollection
    private List<String> examensEffectues;

    @ElementCollection
    private List<String> traitements;

    @ElementCollection
    private List<String> evolution;

    @ElementCollection
    private List<String> recommandationsSortie;
}