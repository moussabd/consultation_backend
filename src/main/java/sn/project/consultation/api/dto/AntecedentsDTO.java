package sn.project.consultation.api.dto;


import lombok.Getter;
import lombok.Setter;
import sn.project.consultation.data.entities.Antecedents;

import java.util.List;

@Getter
@Setter
public class AntecedentsDTO {

    private List<String> antecedentsMedicaux;
    private List<String> antecedentsChirurgicaux;
    private List<String> antecedentsObstetricaux;
    private List<String> antecedentsPsychologiques;
    private List<String> maladiesFamiliales;
    private List<String> allergies;

    public static AntecedentsDTO fromEntity(Antecedents antecedents) {
        if (antecedents == null) return null;

        AntecedentsDTO dto = new AntecedentsDTO();
        dto.setAntecedentsMedicaux(antecedents.getAntecedentsMedicaux());
        dto.setAntecedentsChirurgicaux(antecedents.getAntecedentsChirurgicaux());
        dto.setAntecedentsObstetricaux(antecedents.getAntecedentsObstetricaux());
        dto.setAntecedentsPsychologiques(antecedents.getAntecedentsPsychologiques());
        dto.setAllergies(antecedents.getAllergies());

        return dto;
    }

    public static Antecedents toEntity(AntecedentsDTO dto) {
        if (dto == null) return null;

        Antecedents entity = new Antecedents();
        entity.setAntecedentsMedicaux(dto.getAntecedentsMedicaux());
        entity.setAntecedentsChirurgicaux(dto.getAntecedentsChirurgicaux());
        entity.setAntecedentsObstetricaux(dto.getAntecedentsObstetricaux());
        entity.setAntecedentsPsychologiques(dto.getAntecedentsPsychologiques());
        entity.setMaladiesFamiliales(dto.getMaladiesFamiliales());
        entity.setAllergies(dto.getAllergies());

        return entity;
    }

}
