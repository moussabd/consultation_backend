package sn.project.consultation.data.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@Setter
public class EvolutionSuivi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    @OrderColumn(name = "position_note")
    private List<String> notesEvolution;

    @OneToMany(mappedBy = "evolutionSuivi", cascade = CascadeType.ALL)
    @OrderColumn(name= "position_courbe")
    private List<CourbeClinique> courbes;

    @ElementCollection
    @OrderColumn(name = "position_consultation")
    private List<ConsultationSuivi> consultationsSuivi;

    @OneToOne
    private DossierMedical dossierMedical;

}
