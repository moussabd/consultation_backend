package sn.project.consultation.api.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sn.project.consultation.data.entities.CompteRenduHospitalisation;
import sn.project.consultation.data.entities.Patient;
import sn.project.consultation.data.entities.ProSante;
import sn.project.consultation.data.repositories.PatientRepository;
import sn.project.consultation.data.repositories.ProSanteRepository;

import java.time.LocalDate;
import java.util.List;

@Component
@Getter
@Setter
public class CompteRenduHospitalisationDTO {
    private LocalDate dateAdmission;
    private LocalDate dateSortie;
    private List<String> diagnosticAdmission;
    private List<String> diagnosticSortie;
    private List<String> examensEffectues;
    private List<String> traitements;
    private List<String> evolutions;
    private List<String> recommandationsSortie;
    private ProSanteDTO auteur;
    private ProSanteDTO destinataire;
    private PatientDTO patient;


    public static CompteRenduHospitalisationDTO fromEntity(CompteRenduHospitalisation crh) {
        if (crh == null) return null;
        CompteRenduHospitalisationDTO dto = new CompteRenduHospitalisationDTO();
        dto.setDateAdmission(crh.getDateAdmission());
        dto.setDateSortie(crh.getDateSortie());
        dto.setDiagnosticAdmission(crh.getDiagnosticAdmission());
        dto.setDiagnosticSortie(crh.getDiagnosticSortie());
        dto.setExamensEffectues(crh.getExamensEffectues());
        dto.setTraitements(crh.getTraitements());
        dto.setEvolutions(crh.getEvolution());
        dto.setRecommandationsSortie(crh.getRecommandationsSortie());
        dto.setAuteur(ProSanteDTO.fromEntity(crh.getAuteur()));
        dto.setDestinataire(ProSanteDTO.fromEntity(crh.getDestinataire()));
        dto.setPatient(PatientDTO.fromEntity(crh.getPatient()));
        return dto;
    }

    public static CompteRenduHospitalisation toEntity(CompteRenduHospitalisationDTO dto) {
        if (dto == null) return null;
        CompteRenduHospitalisation crh = new CompteRenduHospitalisation();
        crh.setDateAdmission(dto.getDateAdmission());
        crh.setDateSortie(dto.getDateSortie());
        crh.setDiagnosticAdmission(dto.getDiagnosticAdmission());
        crh.setDiagnosticSortie(dto.getDiagnosticSortie());
        crh.setExamensEffectues(dto.getExamensEffectues());
        crh.setTraitements(dto.getTraitements());
        crh.setEvolution(dto.getEvolutions());
        crh.setRecommandationsSortie(dto.getRecommandationsSortie());
        crh.setAuteur(ProSanteDTO.toEntity(dto.getAuteur()));
        crh.setDestinataire(ProSanteDTO.toEntity(dto.getDestinataire()));
        crh.setPatient(PatientDTO.toEntity(dto.getPatient()));
        // Les liens avec les entités Patient / ProSante seront gérés dans le service ou via repository
        return crh;
    }


}
