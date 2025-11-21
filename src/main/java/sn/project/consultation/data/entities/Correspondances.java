package sn.project.consultation.data.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Correspondances {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "compte_rendu_hospitalisation_id")
    private CompteRenduHospitalisation compteRenduHospitalisation;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "compte_rendu_operatoire_id")
    private CompteRenduOperatoire compteRenduOperatoire;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "lettre_confrere_id")
    private LettreConfrere lettreConfrere;
}
