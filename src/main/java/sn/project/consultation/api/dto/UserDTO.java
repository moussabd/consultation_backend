package sn.project.consultation.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sn.project.consultation.data.entities.User;
import sn.project.consultation.data.enums.RoleUser;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private RoleUser role;

    public static UserDTO fromEntity(User user) {
        if (user == null) return null;
        return new UserDTO(
                user.getId(),
                user.getNom(),
                user.getPrenom(),
                user.getCoordonnees() != null ? user.getCoordonnees().getEmail() : null,
                user.getRole()
        );
    }
}
