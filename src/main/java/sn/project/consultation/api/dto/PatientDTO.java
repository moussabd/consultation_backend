package sn.project.consultation.api.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import sn.project.consultation.data.entities.Coordonnees;
import sn.project.consultation.data.entities.Patient;
import sn.project.consultation.data.entities.ProSante;
import sn.project.consultation.data.enums.RoleUser;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientDTO {
    private Long id;
    private String nom;
    private String prenom;
    private String sexe;
    private String matricule;
    private String lieuNaissance;
    private LocalDate dateNaissance;
    private String situationFamiliale;

    private String adresse;
    private String email;
    private String telephone;

    private Double latitude;
    private Double longitude;

    private RoleUser role;

    // ===== Conversion Entity → DTO =====
    public static PatientDTO fromEntity(Patient patient) {
        if (patient == null) {
            return null;
        }

        Coordonnees coord = patient.getCoordonnees();

        return new PatientDTO(
                patient.getId(),
                patient.getNom(),
                patient.getPrenom(),
                patient.getSexe(),
                patient.getMatricule(),
                patient.getLieuNaissance(),
                patient.getDateNaissance(),
                patient.getSituationFamiliale(),
                coord != null ? coord.getAdresse() : null,
                coord != null ? coord.getEmail() : null,
                coord != null ? coord.getNumeroTelephone() : null,
                patient.getLatitude(),
                patient.getLongitude(),
                patient.getRole()
        );
    }

    // ===== Conversion DTO → Entity =====
    public static Patient toEntity(PatientDTO dto) {
        if (dto == null) {
            return null;
        }

        Patient patient = new Patient();
        patient.setId(dto.getId());
        patient.setNom(dto.getNom());
        patient.setPrenom(dto.getPrenom());
        patient.setSexe(dto.getSexe());
        patient.setMatricule(dto.getMatricule());
        patient.setLieuNaissance(dto.getLieuNaissance());
        patient.setDateNaissance(dto.getDateNaissance());
        patient.setSituationFamiliale(dto.getSituationFamiliale());
        patient.setLatitude(dto.getLatitude());
        patient.setLongitude(dto.getLongitude());
        patient.setRole(dto.getRole());

        Coordonnees coord = new Coordonnees();
        coord.setAdresse(dto.getAdresse());
        coord.setEmail(dto.getEmail());
        coord.setNumeroTelephone(dto.getTelephone());
        patient.setCoordonnees(coord);

        return patient;
    }
}
