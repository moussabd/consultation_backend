package sn.project.consultation.data.entities;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.List;

@Embeddable
@Getter
@Setter
@RequiredArgsConstructor
public class Antecedents {

    // Antécédents personnels
    @ElementCollection
    @Fetch(FetchMode.SUBSELECT)
    private List<String> antecedentsMedicaux; // Exemple : Diabète, hypertension...

    @ElementCollection
    @Fetch(FetchMode.SUBSELECT)
    private List<String> antecedentsChirurgicaux; // Exemple : Appendicectomie...

    @ElementCollection
    @Fetch(FetchMode.SUBSELECT)
    private List<String> antecedentsObstetricaux; // Surtout pour les patientes

    @ElementCollection
    @Fetch(FetchMode.SUBSELECT)
    private List<String> antecedentsPsychologiques;

    // Antécédents familiaux
    @ElementCollection
    @Fetch(FetchMode.SUBSELECT)
    private List<String> maladiesFamiliales; // Exemple : Cancer, diabète...

    // Allergies
    @ElementCollection
    @Fetch(FetchMode.SUBSELECT)
    private List<String> allergies; // Médicamenteuses, alimentaires, etc.
}

