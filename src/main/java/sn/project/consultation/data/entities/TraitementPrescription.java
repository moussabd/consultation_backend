package sn.project.consultation.data.entities;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.List;

@Embeddable
@Getter
@Setter
public class TraitementPrescription {

    // Médicaments prescrits
    @ElementCollection
    @Fetch(FetchMode.SUBSELECT)
    private List<MedicamentPrescrit> medicaments;

    // Soins infirmiers ou paramédicaux
    @ElementCollection
    @Fetch(FetchMode.SUBSELECT)
    private List<SoinsParamedicaux> soins;

    // Interventions ou procédures chirurgicales prévues
    @ElementCollection
    @Fetch(FetchMode.SUBSELECT)
    private List<InterventionChirurgicale> interventions;
}
