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
public class ExamensComplementaires {

    // 🔬 Analyses biologiques
    @ElementCollection
    @Fetch(FetchMode.SUBSELECT)
    private List<AnalyseBiologique> analysesSanguines;

    @ElementCollection
    @Fetch(FetchMode.SUBSELECT)
    private List<AnalyseBiologique> analysesUrines;

    // 🖼️ Examens d’imagerie
    @ElementCollection
    @Fetch(FetchMode.SUBSELECT)
    private List<String> radiographies;

    @ElementCollection
    @Fetch(FetchMode.SUBSELECT)
    private List<String> echographies;

    @ElementCollection
    @Fetch(FetchMode.SUBSELECT)
    private List<String> scanners;

    @ElementCollection
    @Fetch(FetchMode.SUBSELECT)
    private List<String> irm;

    // ⚙️ Tests spécialisés
    @ElementCollection
    @Fetch(FetchMode.SUBSELECT)
    private List<TestSpecial> testsSpeciaux;
}
