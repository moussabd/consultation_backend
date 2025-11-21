package sn.project.consultation.data.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import sn.project.consultation.data.enums.TypeDocumentAnnexe;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class FichierMedical {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomFichier;          // ex: "consentement-chirurgie.pdf"
    private String typeMime;           // ex: "application/pdf", "image/jpeg"
    private String cheminAcces;        // ex: URL cloud ou chemin local
    private LocalDateTime dateAjout;

    @Enumerated(EnumType.STRING)
    private TypeDocumentAnnexe typeDocument;

    @ManyToOne
    private DossierMedical dossier;    // Lien vers le dossier médical parent
}
