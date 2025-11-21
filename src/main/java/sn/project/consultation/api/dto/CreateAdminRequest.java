package sn.project.consultation.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAdminRequest extends CreateUserRequest{
    private String niveauAcces;
    private Boolean estSuperAdmin;
    private String departement;
}
