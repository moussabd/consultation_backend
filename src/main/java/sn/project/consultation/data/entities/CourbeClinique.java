package sn.project.consultation.data.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class CourbeClinique {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;

    @OneToMany(mappedBy = "courbeClinique", cascade = CascadeType.ALL)
    private List<MesureClinique> mesures;

    @ManyToOne
    private EvolutionSuivi evolutionSuivi;
}
