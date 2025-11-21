package sn.project.consultation.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateProSanteRequest extends CreateUserRequest{
    private String specialite;
    private String description;
    private Double tarif;
    private Double latitude;
    private Double longitude;
}
