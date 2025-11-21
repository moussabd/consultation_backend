package sn.project.consultation.api.dto;


import lombok.Getter;
import lombok.Setter;
import sn.project.consultation.data.entities.TraitementPrescription;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class TraitementPrescriptionDTO {

    private List<MedicamentPrescritDTO> medicaments;
    private List<SoinsParamedicauxDTO> soinsParamedicaux;
    private List<InterventionChirurgicaleDTO> interventions;

    public static TraitementPrescriptionDTO toDTO(TraitementPrescription entity) {
        if (entity == null) return null;

        TraitementPrescriptionDTO dto = new TraitementPrescriptionDTO();

        dto.setMedicaments(
                entity.getMedicaments()
                        .stream()
                        .map(MedicamentPrescritDTO::toDTO)
                        .collect(Collectors.toList())
        );

        dto.setSoinsParamedicaux(
                entity.getSoins()
                        .stream()
                        .map(SoinsParamedicauxDTO::toDTO)
                        .collect(Collectors.toList())
        );

        dto.setInterventions(
                entity.getInterventions()
                        .stream()
                        .map(InterventionChirurgicaleDTO::toDTO)
                        .collect(Collectors.toList())
        );

        return dto;
    }

    public static TraitementPrescription toEntity(TraitementPrescriptionDTO dto) {
        if (dto == null) return null;

        TraitementPrescription entity = new TraitementPrescription();

        entity.setMedicaments(
                dto.getMedicaments()
                        .stream()
                        .map(MedicamentPrescritDTO::toEntity)
                        .collect(Collectors.toList())
        );

        entity.setSoins(
                dto.getSoinsParamedicaux()
                        .stream()
                        .map(SoinsParamedicauxDTO::toEntity)
                        .collect(Collectors.toList())
        );

        entity.setInterventions(
                dto.getInterventions()
                        .stream()
                        .map(InterventionChirurgicaleDTO::toEntity)
                        .collect(Collectors.toList())
        );

        return entity;
    }
}
