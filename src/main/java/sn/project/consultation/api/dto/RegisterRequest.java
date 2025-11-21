package sn.project.consultation.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import sn.project.consultation.data.enums.RoleUser;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class RegisterRequest {
    private String nom;
    private String prenom;
    private String sexe;
    private String email;
    private String adresse;
    private String telephone;
    private String motDePasse;
    private RoleUser role;

    // Champs spécifiques Patient
    private String matricule;
    private String lieuNaissance;

    private String dateNaissance;
    private String situationFamiliale;
    private Double latitude;   // partagé avec ProSante
    private Double longitude;  // partagé avec ProSante

    // Champs spécifiques ProSante
    private String specialite;
    private String description;
    private Double tarif;
}
