package sn.project.consultation.api.dto;


import lombok.*;
import sn.project.consultation.data.entities.Patient;
import sn.project.consultation.data.entities.ProSante;
import sn.project.consultation.data.enums.RoleUser;

@Data
@Getter
@Setter
@NoArgsConstructor
public class ProSanteDTO {
    private Long id;
    private String nom;
    private String prenom;
    private String specialite;
    private String role;

    private Double tarif;
    private Double latitude;
    private Double longitude;
    private Double distanceKm;

    public static ProSanteDTO fromEntity(ProSante proSante) {
        if (proSante == null) {
            return null;
        }

        ProSanteDTO dto = new ProSanteDTO();
        dto.setId(proSante.getId());
                dto.setNom(proSante.getNom());  // Assure-toi que la classe User (superclasse) a bien la méthode getNom()
                dto.setPrenom(proSante.getPrenom());
        dto.setSpecialite(proSante.getSpecialite());
        dto.setTarif(proSante.getTarif());
                dto.setLatitude(proSante.getLatitude());
        dto.setLongitude(proSante.getLongitude());
        dto.setRole(proSante.getRole().toString());
                // distanceKm sera probablement calculée ailleurs

        return dto;
    }

    public static ProSante toEntity(ProSanteDTO dto) {
        if (dto == null) {
            return null;
        }

        ProSante proSante = new ProSante();
        proSante.setId(dto.getId());

        // Séparation nom / prénom
        String[] parts = dto.getNom() != null ? dto.getNom().split(" ", 2) : new String[]{"", ""};
        proSante.setNom(parts[0]);
        proSante.setPrenom(parts.length > 1 ? parts[1] : "");

        proSante.setSpecialite(dto.getSpecialite());
        proSante.setTarif(dto.getTarif());
        proSante.setLatitude(dto.getLatitude());
        proSante.setLongitude(dto.getLongitude());

        // Conversion String -> Enum RoleUser
        try {
            proSante.setRole(RoleUser.valueOf(dto.getRole()));
        } catch (IllegalArgumentException | NullPointerException e) {
            proSante.setRole(null); // ou une valeur par défaut
        }

        return proSante;
    }
}
