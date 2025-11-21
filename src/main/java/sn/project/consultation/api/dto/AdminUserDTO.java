package sn.project.consultation.api.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sn.project.consultation.data.entities.Coordonnees;
import sn.project.consultation.data.enums.RoleUser;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserDTO {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private RoleUser role;
    private boolean enabled;
    private Coordonnees coordonnees;
    private LocalDateTime dateCreation;
}
