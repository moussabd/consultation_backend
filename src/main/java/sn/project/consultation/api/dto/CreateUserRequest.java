package sn.project.consultation.api.dto;

import lombok.Data;
import sn.project.consultation.data.enums.RoleUser;

@Data
public class CreateUserRequest {
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String adresse;
    private String sexe;
    private RoleUser role;
}