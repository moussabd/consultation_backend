package sn.project.consultation.api.dto;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import sn.project.consultation.data.entities.Correspondances;

@Getter
@Setter

public class CorrespondancesDTO {
    private CompteRenduOperatoireDTO compteRenduOperatoire;
    private CompteRenduHospitalisationDTO compteRenduHospitalisation;
    private LettreConfrereDTO lettreConfrere;

    public static CorrespondancesDTO fromEntity(Correspondances correspondances) {
        if (correspondances == null) return null;
        CorrespondancesDTO correspondancesDTO = new CorrespondancesDTO();
        correspondancesDTO.setCompteRenduHospitalisation(CompteRenduHospitalisationDTO.fromEntity(correspondances.getCompteRenduHospitalisation()));
        correspondancesDTO.setCompteRenduOperatoire( CompteRenduOperatoireDTO.fromEntity(correspondances.getCompteRenduOperatoire()));
        correspondancesDTO.setLettreConfrere( LettreConfrereDTO.fromEntity(correspondances.getLettreConfrere()));
        return correspondancesDTO;
    }

    public static Correspondances toEntity(CorrespondancesDTO dto) {
        if (dto == null) return null;
        Correspondances correspondances = new Correspondances();
        correspondances.setCompteRenduHospitalisation(CompteRenduHospitalisationDTO.toEntity(dto.getCompteRenduHospitalisation()));
        correspondances.setCompteRenduOperatoire(CompteRenduOperatoireDTO.toEntity(dto.getCompteRenduOperatoire()));
        correspondances.setLettreConfrere(LettreConfrereDTO.toEntity(dto.getLettreConfrere()));
        return correspondances;
    }
}
