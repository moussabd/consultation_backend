package sn.project.consultation.api.dto;



import lombok.*;
import sn.project.consultation.data.entities.Facture;
import sn.project.consultation.data.entities.Paiement;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import sn.project.consultation.data.entities.Facture;
import sn.project.consultation.data.entities.Paiement;
import sn.project.consultation.data.entities.EtatPaiement;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FactureDTO {

    private Long id;
    private String numero;
    private LocalDateTime dateEmission;
    private String urlPdf;

    private Double montant;        // Montant de la facture
    private PaiementDTO paiement;  // Paiement unique associé

    private Double montantTotal;   // Montant déjà payé
    private Double montantASolder; // Reste à payer

    private String etatPaiement;   // "NON_PAYEE", "PAYEE_COMPLETEMENT", etc.

    // ✅ Conversion entité → DTO
    public static FactureDTO fromEntity(Facture facture) {
        if (facture == null) return null;

        FactureDTO dto = new FactureDTO();
        dto.setId(facture.getId());
        dto.setNumero(facture.getNumero());
        dto.setDateEmission(facture.getDateEmission());
        dto.setUrlPdf(facture.getUrlPdf());
        dto.setMontant(facture.getMontant() != null ? facture.getMontant() : 0.0);
        dto.setEtatPaiement(facture.getEtatPaiement() != null ? facture.getEtatPaiement().name() : "NON_DEFINI");

        Paiement paiement = facture.getPaiement();
        if (paiement != null) {
            PaiementDTO paiementDTO = PaiementDTO.fromEntity(paiement);
            dto.setPaiement(paiementDTO);
            dto.setMontantTotal(paiement.getMontant());
            dto.setMontantASolder(dto.getMontant() - paiement.getMontant());
        } else {
            dto.setMontantTotal(0.0);
            dto.setMontantASolder(dto.getMontant());
        }

        return dto;
    }

    // ✅ Conversion DTO → entité
    public static Facture toEntity(FactureDTO dto) {
        if (dto == null) return null;

        Facture facture = new Facture();
        facture.setId(dto.getId());
        facture.setNumero(dto.getNumero());
        facture.setDateEmission(dto.getDateEmission());
        facture.setUrlPdf(dto.getUrlPdf());
        facture.setMontant(dto.getMontant());
        facture.setEtatPaiement(
                dto.getEtatPaiement() != null
                        ? Enum.valueOf(EtatPaiement.class, dto.getEtatPaiement())
                        : null
        );

        if (dto.getPaiement() != null) {
            Paiement paiement = PaiementDTO.toEntity(dto.getPaiement());
            paiement.setFacture(facture);
            facture.setPaiement(paiement);
        }

        return facture;
    }
}

