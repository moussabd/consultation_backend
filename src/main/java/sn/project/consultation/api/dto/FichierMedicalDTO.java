package sn.project.consultation.api.dto;

import lombok.Getter;
import lombok.Setter;
import sn.project.consultation.data.entities.DossierMedical;
import sn.project.consultation.data.entities.FichierMedical;
import sn.project.consultation.data.enums.TypeDocumentAnnexe;

import java.time.LocalDateTime;

@Getter
@Setter
public class FichierMedicalDTO {
    private Long id;
    private String nomFichier;
    private String typeMime;
    private String cheminAcces;
    private LocalDateTime dateAjout;
    private TypeDocumentAnnexe typeDocument;

    public static FichierMedicalDTO toDTO(FichierMedical fichier) {
        FichierMedicalDTO dto = new FichierMedicalDTO();
        dto.setId(fichier.getId());
        dto.setNomFichier(fichier.getNomFichier());
        dto.setTypeMime(fichier.getTypeMime());
        dto.setCheminAcces(fichier.getCheminAcces());
        dto.setDateAjout(fichier.getDateAjout());
        dto.setTypeDocument(fichier.getTypeDocument());
        return dto;
    }

    public static FichierMedical toEntity(FichierMedicalDTO dto) {
        FichierMedical fichier = new FichierMedical();
        fichier.setNomFichier(dto.getNomFichier());
        fichier.setTypeMime(dto.getTypeMime());
        fichier.setCheminAcces(dto.getCheminAcces());
        fichier.setDateAjout(dto.getDateAjout() != null ? dto.getDateAjout() : java.time.LocalDateTime.now());
        fichier.setTypeDocument(dto.getTypeDocument());
        return fichier;
    }
}