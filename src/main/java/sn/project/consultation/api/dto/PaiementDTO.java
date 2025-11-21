package sn.project.consultation.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sn.project.consultation.data.entities.Paiement;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaiementDTO {
    private Long id;
    private Double montant;
    private String methode;
    private String statut;
    private String reference;
    private LocalDateTime datePaiement;
    private PatientDTO patient;
    private ProSanteDTO professionnel;

    public static PaiementDTO fromEntity(Paiement paiement) {
        if (paiement == null) return null;
        return new PaiementDTO(
                paiement.getId(),
                paiement.getMontant(),
                paiement.getMethode(),
                paiement.getStatut(),
                paiement.getReference(),
                paiement.getDatePaiement(),
                PatientDTO.fromEntity(paiement.getPatient()),
                ProSanteDTO.fromEntity(paiement.getProfessionnel())
        );
    }

    public static Paiement toEntity(PaiementDTO dto) {
        if (dto == null) return null;
        Paiement paiement = new Paiement();
        paiement.setId(dto.getId());
        paiement.setMontant(dto.getMontant());
        paiement.setMethode(dto.getMethode());
        paiement.setReference(dto.getReference());
        paiement.setStatut(dto.getStatut());
        paiement.setDatePaiement(dto.getDatePaiement());
        paiement.setPatient(PatientDTO.toEntity(dto.getPatient()));
        paiement.setProfessionnel(ProSanteDTO.toEntity(dto.getProfessionnel()));
        return paiement;
    }
}
