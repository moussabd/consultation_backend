package sn.project.consultation.api.dto;

import lombok.Data;
import sn.project.consultation.data.entities.DocumentMedical;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class DocumentDTO {
    private String nom;
    private String urlStockage;

    public static DocumentDTO fromEntity(DocumentMedical document) {
        if (document == null) {
            return null;
        }

        DocumentDTO dto = new DocumentDTO();
        dto.setNom(document.getNom());
        dto.setUrlStockage(document.getUrlStockage());
        return dto;
    }

    public static DocumentMedical toEntity(DocumentDTO dto) {
        if (dto == null) {
            return null;
        }

        DocumentMedical document = new DocumentMedical();
        document.setNom(dto.getNom());
        document.setUrlStockage(dto.getUrlStockage());
        return document;
    }

    public static List<DocumentDTO> fromEntities(List<DocumentMedical> documents) {
        if (documents == null) {
            return null;
        }
        return documents.stream()
                .map(DocumentDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
