package sn.project.consultation.data.entities;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Coordonnees {
    private String adresse;
    private String numeroTelephone;
    private String email;
}
