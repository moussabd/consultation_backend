package sn.project.consultation.api.dto;

import lombok.Getter;
import lombok.Setter;
import sn.project.consultation.data.entities.MesureClinique;

import java.time.LocalDate;

@Getter
@Setter
public class MesureCliniqueDTO {
    private LocalDate date;
    private Double valeur;

    public static MesureCliniqueDTO toDTO(MesureClinique mesure) {
        if (mesure == null) return null;
        MesureCliniqueDTO dto = new MesureCliniqueDTO();
        dto.setDate(mesure.getDate());
        dto.setValeur(mesure.getValeur());
        return dto;
    }

    public static MesureClinique toEntity(MesureCliniqueDTO dto) {
        if (dto == null) return null;
        MesureClinique mesure = new MesureClinique();
        mesure.setDate(dto.getDate());
        mesure.setValeur(dto.getValeur());
        return mesure;
    }
}

