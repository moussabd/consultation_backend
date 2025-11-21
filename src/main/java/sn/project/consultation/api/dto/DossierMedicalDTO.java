package sn.project.consultation.api.dto;

import lombok.Data;

import java.util.List;


import sn.project.consultation.data.entities.DossierMedical;
import sn.project.consultation.data.entities.FichierMedical;

import java.util.stream.Collectors;

@Data
public class DossierMedicalDTO {
    private Long id;
    private String resume;
    private PatientDTO patient;
    private String couvertureSociale;
    private String personneUrgence;
    private String telPersonneUrgence;
    private AntecedentsDTO antecedents;
    private ExamenCliniqueDTO examenClinique;
    private ExamensComplementairesDTO examensComplementaires;
    private DiagnosticMedicalDTO diagnosticMedical;
    private TraitementPrescriptionDTO traitements;
    private EvolutionSuiviDTO evolutionSuivi;
    private CorrespondancesDTO correspondances;
    private List<FichierMedicalDTO> documents;
    private List<FichierMedicalDTO> documentsAnnexes;
    private List<HistoriqueConsultationDTO> historiques;

    public static DossierMedicalDTO fromEntity(DossierMedical dossier) {
        if (dossier == null) return null;
        DossierMedicalDTO dto = new DossierMedicalDTO();
        dto.setId(dossier.getId());
        dto.setResume(dossier.getResume());
        dto.setPatient(PatientDTO.fromEntity(dossier.getPatient()));
        dto.setCouvertureSociale(dossier.getCouvertureSociale());
        dto.setPersonneUrgence(dossier.getPersonneUrgence());
        dto.setTelPersonneUrgence(dossier.getTelPersonneUrgence());
        dto.setAntecedents(AntecedentsDTO.fromEntity(dossier.getAntecedents()));
        dto.setExamenClinique(ExamenCliniqueDTO.toDTO(dossier.getExamenClinique()));
        dto.setExamensComplementaires(ExamensComplementairesDTO.toDTO(dossier.getExamensComplementaires()));
        dto.setDiagnosticMedical(DiagnosticMedicalDTO.toDto(dossier.getDiagnosticMedical()));
        dto.setTraitements(TraitementPrescriptionDTO.toDTO(dossier.getTraitements()));
        dto.setEvolutionSuivi(EvolutionSuiviDTO.toDTO(dossier.getEvolutionSuivi()));
        dto.setCorrespondances(CorrespondancesDTO.fromEntity(dossier.getCorrespondances()));

        if (dossier.getDocuments() != null) {
            dto.setDocuments(dossier.getDocuments()
                    .stream()
                    .map(FichierMedicalDTO::toDTO)
                    .collect(Collectors.toList()));
        }
        if (dossier.getDocumentsAnnexes() != null) {
            dto.setDocumentsAnnexes(dossier.getDocumentsAnnexes()
                    .stream()
                    .map(FichierMedicalDTO::toDTO)
                    .collect(Collectors.toList()));
        }
        if (dossier.getHistoriques() != null) {
            dto.setHistoriques(dossier.getHistoriques()
                    .stream()
                    .map(HistoriqueConsultationDTO::fromEntity)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    public static DossierMedical toEntity(DossierMedicalDTO dto) {
        if (dto == null) return null;

        DossierMedical dossier = new DossierMedical();
        dossier.setId(dto.getId());
        dossier.setResume(dto.getResume());
        dossier.setPatient(PatientDTO.toEntity(dto.getPatient()));
        dossier.setCouvertureSociale(dto.getCouvertureSociale());
        dossier.setPersonneUrgence(dto.getPersonneUrgence());
        dossier.setTelPersonneUrgence(dto.getTelPersonneUrgence());
        dossier.setAntecedents(AntecedentsDTO.toEntity(dto.getAntecedents()));
        dossier.setExamenClinique(ExamenCliniqueDTO.toEntity(dto.getExamenClinique()));
        dossier.setExamensComplementaires(ExamensComplementairesDTO.toEntity(dto.getExamensComplementaires()));
        dossier.setDiagnosticMedical(DiagnosticMedicalDTO.toEntity(dto.getDiagnosticMedical()));
        dossier.setTraitements(TraitementPrescriptionDTO.toEntity(dto.getTraitements()));
        dossier.setEvolutionSuivi(EvolutionSuiviDTO.toEntity(dto.getEvolutionSuivi()));
        dossier.setCorrespondances(CorrespondancesDTO.toEntity(dto.getCorrespondances()));

        if (dto.getDocuments() != null) {
            dossier.setDocuments(dto.getDocuments()
                    .stream()
                    .map(FichierMedicalDTO::toEntity)
                    .collect(Collectors.toList()));
        }
        if (dto.getDocumentsAnnexes() != null) {
            dossier.setDocumentsAnnexes(dto.getDocumentsAnnexes()
                    .stream()
                    .map(FichierMedicalDTO::toEntity)
                    .collect(Collectors.toList()));
        }
        if (dto.getHistoriques() != null) {
            dossier.setHistoriques(dto.getHistoriques()
                    .stream()
                    .map(HistoriqueConsultationDTO::toEntity)
                    .collect(Collectors.toList()));
        }

        return dossier;
    }

    public static List<DossierMedicalDTO> fromEntities(List<DossierMedical> dossiers) {
        if (dossiers == null) {
            return null;
        }
        return dossiers.stream()
                .map(DossierMedicalDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public static List<DossierMedical> toEntities(List<DossierMedicalDTO> dtos) {
        if (dtos == null) {
            return null;
        }
        return dtos.stream()
                .map(DossierMedicalDTO::toEntity)
                .collect(Collectors.toList());
    }



}
