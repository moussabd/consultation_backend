package sn.project.consultation.data.entities;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Embeddable
@Getter
@Setter
public class ExamenClinique {
    private Double poids;
    private Double taille;
    private String tensionArterielle;
    private Double temperature;
    private Integer frequenceCardiaque;
    private Integer saturationOxygene;

    @ElementCollection
    private List<ElementBilanPhysique> bilanPhysique;

    @ElementCollection
    private List<String> observations;
}

