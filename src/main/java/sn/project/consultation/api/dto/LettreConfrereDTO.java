package sn.project.consultation.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sn.project.consultation.data.entities.LettreConfrere;
import sn.project.consultation.data.entities.Patient;
import sn.project.consultation.data.entities.ProSante;
import sn.project.consultation.data.repositories.PatientRepository;
import sn.project.consultation.data.repositories.ProSanteRepository;

import java.util.List;

@Data
@Component
@NoArgsConstructor
@AllArgsConstructor
public class LettreConfrereDTO {
    private String motifConsultation;
    private List<String> resultatsExamens;
    private String diagnostic;
    private List<String> traitementsProposes;
    private List<String> recommandationsSuivi;
    private ProSanteDTO auteur;
    private ProSanteDTO destinataire;
    private PatientDTO patient;

    @Autowired
    private static ProSanteRepository medecinRepository;


    @Autowired
    private static PatientRepository patientRepository;


    public static LettreConfrereDTO fromEntity(LettreConfrere lettre) {
        if (lettre == null) return null;
        LettreConfrereDTO dto = new LettreConfrereDTO();
        dto.setMotifConsultation(lettre.getMotifConsultation());
        dto.setResultatsExamens(lettre.getResultatsExamens());
        dto.setDiagnostic(lettre.getDiagnostic());
        dto.setTraitementsProposes(lettre.getTraitementPropose());
        dto.setRecommandationsSuivi(lettre.getRecommandationsSuivi());
        dto.setAuteur(ProSanteDTO.fromEntity(lettre.getAuteur()));
        dto.setDestinataire(ProSanteDTO.fromEntity(lettre.getDestinataire()));
        dto.setPatient(PatientDTO.fromEntity(lettre.getPatient()));
        return dto;
    }

    public static LettreConfrere toEntity(LettreConfrereDTO dto) {
        if (dto == null) return null;
        LettreConfrere lettre = new LettreConfrere();
        lettre.setMotifConsultation(dto.getMotifConsultation());
        lettre.setResultatsExamens(dto.getResultatsExamens());
        lettre.setDiagnostic(dto.getDiagnostic());
        lettre.setTraitementPropose(dto.getTraitementsProposes());
        lettre.setRecommandationsSuivi(dto.getRecommandationsSuivi());
        lettre.setAuteur(ProSanteDTO.toEntity(dto.getAuteur()));
        lettre.setDestinataire(ProSanteDTO.toEntity(dto.getDestinataire()));
        lettre.setPatient(PatientDTO.toEntity(dto.getPatient()));
        return lettre;
    }
}

