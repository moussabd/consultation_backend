package sn.project.consultation.data.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Teleconsultation {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Patient patient;

    @ManyToOne
    private ProSante proSante;

    private LocalDateTime dateHeure;

    private String statut; // EN_ATTENTE, EN_COURS, TERMINEE, ANNULEE

    private String lienVideo; // Lien sécurisé pour la visioconférence

    private String notes; // Notes de la consultation

    // Getters et setters
}
