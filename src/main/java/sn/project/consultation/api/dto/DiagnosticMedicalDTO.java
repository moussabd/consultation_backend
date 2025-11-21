package sn.project.consultation.api.dto;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import sn.project.consultation.data.entities.DiagnosticMedical;

import java.util.List;

@Data
@Getter
@Setter
public class DiagnosticMedicalDTO {

    private String diagnosticPrincipal;
    private String codePrincipal;
    private String systemeCodification;
    private List<String> diagnosticsSecondaires;

    public static DiagnosticMedicalDTO toDto(DiagnosticMedical entity) {
        if (entity == null) {
            return null;
        }

        DiagnosticMedicalDTO dto = new DiagnosticMedicalDTO();
        dto.setDiagnosticPrincipal(entity.getDiagnosticPrincipal());
        dto.setCodePrincipal(entity.getCodePrincipal());
        dto.setSystemeCodification(entity.getSystemeCodification());
        dto.setDiagnosticsSecondaires(entity.getDiagnosticsSecondaires());
        return dto;
    }

    // Convertir DTO -> Entity
    public static DiagnosticMedical toEntity(DiagnosticMedicalDTO dto) {
        if (dto == null) {
            return null;
        }

        DiagnosticMedical entity = new DiagnosticMedical();
        entity.setDiagnosticPrincipal(dto.getDiagnosticPrincipal());
        entity.setCodePrincipal(dto.getCodePrincipal());
        entity.setSystemeCodification(dto.getSystemeCodification());
        entity.setDiagnosticsSecondaires(dto.getDiagnosticsSecondaires());
        return entity;
    }
}
