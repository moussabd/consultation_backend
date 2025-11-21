package sn.project.consultation.data.entities;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Embeddable
@Getter
@Setter
public class DiagnosticMedical {
    // Diagnostic principal
    private String diagnosticPrincipal;     // Exemple : Pneumonie bactérienne
    private String codePrincipal;           // Exemple : J18.9 ou SNOMED CT ID
    private String systemeCodification;     // Exemple : CIM-10, SNOMED CT

    // Diagnostics secondaires ou associés
    @ElementCollection
    private List<String> diagnosticsSecondaires; // Liste libre de diagnostics secondaires
}
