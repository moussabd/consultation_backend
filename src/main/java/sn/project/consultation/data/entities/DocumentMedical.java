package sn.project.consultation.data.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class DocumentMedical {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String urlStockage;

    @ManyToOne(optional = false)
    @JoinColumn(name = "dossier_id", nullable = false)
    private DossierMedical dossier;

}
