package sn.project.consultation.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sn.project.consultation.data.entities.CompteRenduOperatoire;
import sn.project.consultation.data.entities.Patient;
import sn.project.consultation.data.entities.ProSante;
import sn.project.consultation.data.repositories.PatientRepository;
import sn.project.consultation.data.repositories.ProSanteRepository;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class CompteRenduOperatoireDTO {
    private String nomIntervention;
    private String indicationOperatoire;
    private String descriptionActe;
    private List<String> complications;
    private String conclusion;
    private ProSanteDTO auteur;
    private ProSanteDTO destinataire;
    private PatientDTO patient;

    @Autowired
    private static ProSanteRepository medecinRepository;


    @Autowired
    private static PatientRepository patientRepository;


    public static CompteRenduOperatoireDTO fromEntity(CompteRenduOperatoire cro) {
        if (cro == null) return null;
        CompteRenduOperatoireDTO dto = new CompteRenduOperatoireDTO();
        dto.setNomIntervention(cro.getNomIntervention());
        dto.setIndicationOperatoire(cro.getIndicationOperatoire());
        dto.setDescriptionActe(cro.getDescriptionActe());
        dto.setComplications(cro.getComplications());
        dto.setConclusion(cro.getConclusion());
        dto.setAuteur(ProSanteDTO.fromEntity(cro.getAuteur()));
        dto.setDestinataire(ProSanteDTO.fromEntity(cro.getDestinataire()));
        dto.setPatient(PatientDTO.fromEntity(cro.getPatient()));
        return dto;
    }

    public static CompteRenduOperatoire toEntity(CompteRenduOperatoireDTO dto) {
        if (dto == null) return null;
        CompteRenduOperatoire cro = new CompteRenduOperatoire();
        cro.setNomIntervention(dto.getNomIntervention());
        cro.setIndicationOperatoire(dto.getIndicationOperatoire());
        cro.setDescriptionActe(dto.getDescriptionActe());
        cro.setComplications(dto.getComplications());
        cro.setConclusion(dto.getConclusion());
        cro.setAuteur(ProSanteDTO.toEntity(dto.getAuteur()));
        cro.setDestinataire(ProSanteDTO.toEntity(dto.getDestinataire()));
        cro.setPatient(PatientDTO.toEntity(dto.getPatient()));
        return cro;
    }
}
