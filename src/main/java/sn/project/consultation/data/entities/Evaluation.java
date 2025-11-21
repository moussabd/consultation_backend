package sn.project.consultation.data.entities;

import jakarta.persistence.*;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
public class Evaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int note;
    private String commentaire;

    @ManyToOne
    private Patient patient;

    @ManyToOne
    private ProSante prosante;
}
