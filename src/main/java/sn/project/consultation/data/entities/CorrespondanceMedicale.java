package sn.project.consultation.data.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@MappedSuperclass
@Getter
@Setter
public abstract class CorrespondanceMedicale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dateRedaction;
    @ManyToOne
    @JoinColumn(name = "auteur_id")
    private ProSante auteur; // médecin, service, etc.

    @ManyToOne
    @JoinColumn(name = "destinataire_id")
    private ProSante destinataire; // médecin traitant, autre spécialiste, etc.

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient; // identifiant du patient concerné
}

