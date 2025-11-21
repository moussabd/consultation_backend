package sn.project.consultation.data.entities;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
public class DossierMedical {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String resume;
    private LocalDate date;
    @ManyToOne
    private Patient patient;

    @ManyToOne
    private ProSante proSante;

    private String couvertureSociale;
    private String personneUrgence;
    private String telPersonneUrgence;

    @Embedded
    private Antecedents antecedents;

    @Embedded
    private ExamenClinique examenClinique;

    @Embedded
    private ExamensComplementaires examensComplementaires;

    @Embedded
    private DiagnosticMedical diagnosticMedical;

    @Embedded
    private TraitementPrescription traitements;

    @OneToOne(mappedBy = "dossierMedical", cascade = CascadeType.ALL)
    private EvolutionSuivi evolutionSuivi;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "correspondances_id")
    private Correspondances correspondances;


    // 📂 Documents médicaux
    @OneToMany(mappedBy = "dossier",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<FichierMedical> documents = new ArrayList<>();

    // 📂 Annexes
    @OneToMany(mappedBy = "dossier",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<FichierMedical> documentsAnnexes = new ArrayList<>();

    // 📜 Historique des consultations
    @OneToMany(mappedBy = "dossier",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<HistoriqueConsultation> historiques = new ArrayList<>();
}
