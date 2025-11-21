package sn.project.consultation.data.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class RendezVous {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dateHeure;
    private String statut;

    @ManyToOne
    private Patient patient;

    @ManyToOne
    private ProSante prosante;

    // // Getters and setters (optional but recommended)
    // public Long getId() {
    //     return id;
    // }

    // public void setId(Long id) {
    //     this.id = id;
    // }

    // public LocalDateTime getDateHeure() {
    //     return dateHeure;
    // }

    // public void setDateHeure(LocalDateTime dateHeure) {
    //     this.dateHeure = dateHeure;
    // }

    // public String getStatut() {
    //     return statut;
    // }

    // public void setStatut(String statut) {
    //     this.statut = statut;
    // }

    // public Patient getPatient() {
    //     return patient;
    // }

    // public void setPatient(Patient patient) {
    //     this.patient = patient;
    // }

    // public ProSante getProsante() {
    //     return prosante;
    // }

    // public void setProsante(ProSante prosante) {
    //     this.prosante = prosante;
    // }
}
