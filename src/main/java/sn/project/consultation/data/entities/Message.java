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
public class Message {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Teleconsultation teleconsultation;

    @ManyToOne
    private User expediteur; // Patient ou Médecin

    private String contenu;

    private LocalDateTime dateEnvoi;

    private String source;

    // Getters et setters
}
