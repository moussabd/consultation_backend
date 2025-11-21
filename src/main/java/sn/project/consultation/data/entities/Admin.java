package sn.project.consultation.data.entities;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Admin extends User {

    private String niveauAcces; // COMPLET, LIMITE, etc.
    private boolean estSuperAdmin;

    // Champs supplémentaires si nécessaire
    private String departement;
    private String permissionsSpeciales;
}