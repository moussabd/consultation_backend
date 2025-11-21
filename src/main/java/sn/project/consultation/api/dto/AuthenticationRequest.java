package sn.project.consultation.api.dto;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String email;
    private String motDePasse;
}
