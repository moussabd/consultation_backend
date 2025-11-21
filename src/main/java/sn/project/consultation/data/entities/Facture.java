package sn.project.consultation.data.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Entity
@Getter
@Setter
public class Facture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numero;
    private LocalDateTime dateEmission;
    private String urlPdf;
    private Double montant;

    // ✅ Une seule facture <-> un seul paiement
    @OneToOne(mappedBy = "facture", cascade = CascadeType.ALL, orphanRemoval = true)
    private Paiement paiement;

    @Enumerated(EnumType.STRING)
    private EtatPaiement etatPaiement;
}

