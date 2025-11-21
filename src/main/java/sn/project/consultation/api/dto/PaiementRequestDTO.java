package sn.project.consultation.api.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class PaiementRequestDTO {
    private Double montant;
    private PatientDTO patient;
    private ProSanteDTO professionnel;
    private String methode;
    private String statut;
    private String datePaiement;
}
