package sn.project.consultation.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CreatePatientRequest extends CreateUserRequest{
    private String lieuNaissance;
    private LocalDate dateNaissance;
    private String situationFamiliale;
    private Double latitude;
    private Double longitude;

}
