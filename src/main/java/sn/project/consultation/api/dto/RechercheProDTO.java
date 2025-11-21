package sn.project.consultation.api.dto;


import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class RechercheProDTO {
    private Double latitude;
    private Double longitude;
    private String specialite;
    private Double tarifMax;
    private Double rayonKm;
}