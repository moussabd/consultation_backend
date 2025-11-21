package sn.project.consultation.data.fixtures;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import sn.project.consultation.data.entities.Admin;
import sn.project.consultation.data.entities.Coordonnees;
import sn.project.consultation.data.enums.RoleUser;
import sn.project.consultation.data.repositories.AdminRepository;

@Component
@Order(3) // Exécution après PatientFixtures et ProSanteFixtures
public class AdminFixtures implements CommandLineRunner {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Vérifier si des admins existent déjà
        if (adminRepository.count() > 0) {
            System.out.println("Des administrateurs existent déjà, skip des fixtures");
            return;
        }

        System.out.println("Création des fixtures administrateurs...");

        // Admin principal
        Admin adminPrincipal = new Admin();
        adminPrincipal.setNom("Admin");
        adminPrincipal.setPrenom("System");
        adminPrincipal.setSexe("Masculin");
        adminPrincipal.setRole(RoleUser.ADMIN);
        adminPrincipal.setEnabled(true);

        // Mot de passe encodé
        adminPrincipal.setMotDePasse(passwordEncoder.encode("admin123"));

        Coordonnees coordonneesPrincipal = new Coordonnees();
        coordonneesPrincipal.setEmail("admin@santeado.com");
        coordonneesPrincipal.setNumeroTelephone("771234567");
        coordonneesPrincipal.setAdresse("Siège Social, Dakar Plateau");
        adminPrincipal.setCoordonnees(coordonneesPrincipal);

        adminPrincipal.setNiveauAcces("COMPLET");
        adminPrincipal.setEstSuperAdmin(true);
        adminPrincipal.setDepartement("Direction Générale");

        adminRepository.save(adminPrincipal);

        // Admin technique
        Admin adminTechnique = new Admin();
        adminTechnique.setNom("Technique");
        adminTechnique.setPrenom("IT");
        adminTechnique.setSexe("Masculin");
        adminTechnique.setRole(RoleUser.ADMIN);
        adminTechnique.setEnabled(true);

        adminTechnique.setMotDePasse(passwordEncoder.encode("tech123"));

        Coordonnees coordonneesTechnique = new Coordonnees();
        coordonneesTechnique.setEmail("tech@santeado.com");
        coordonneesTechnique.setNumeroTelephone("772345678");
        coordonneesTechnique.setAdresse("Service IT, Dakar");
        adminTechnique.setCoordonnees(coordonneesTechnique);

        adminTechnique.setNiveauAcces("COMPLET");
        adminTechnique.setEstSuperAdmin(false);
        adminTechnique.setDepartement("Informatique");

        adminRepository.save(adminTechnique);

        // Admin médical
        Admin adminMedical = new Admin();
        adminMedical.setNom("Medical");
        adminMedical.setPrenom("Coord");
        adminMedical.setSexe("Féminin");
        adminMedical.setRole(RoleUser.ADMIN);
        adminMedical.setEnabled(true);

        adminMedical.setMotDePasse(passwordEncoder.encode("med123"));

        Coordonnees coordonneesMedical = new Coordonnees();
        coordonneesMedical.setEmail("medical@santeado.com");
        coordonneesMedical.setNumeroTelephone("773456789");
        coordonneesMedical.setAdresse("Service Médical, Dakar");
        adminMedical.setCoordonnees(coordonneesMedical);

        adminMedical.setNiveauAcces("LIMITE");
        adminMedical.setEstSuperAdmin(false);
        adminMedical.setDepartement("Médical");

        adminRepository.saveAndFlush(adminMedical);

        System.out.println("Fixtures administrateurs créées avec succès");
        System.out.println("Admin principal: admin@santeado.com / admin123");
        System.out.println("Admin technique: tech@santeado.com / tech123");
        System.out.println("Admin médical: medical@santeado.com / med123");
    }
}