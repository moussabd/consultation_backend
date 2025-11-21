package sn.project.consultation.services;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;

@Service
public class CloudStorageService {

    private static final String BASE_DIR = "uploads/";
    private static final String BASE_URL = "http://localhost:10001/files/";

    public String upload(byte[] contenu, String cheminRelatif) {
        try {
            File fichier = new File(BASE_DIR + cheminRelatif);
            File dossierParent = fichier.getParentFile();
            if (!dossierParent.exists()) {
                dossierParent.mkdirs();
            }
            try (FileOutputStream fos = new FileOutputStream(fichier)) {
                fos.write(contenu);
            }
            return BASE_URL + cheminRelatif.replace("\\", "/");
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'upload du fichier", e);
        }
    }

    public byte[] lireFichier(String cheminAbsolu) {
        try {
            return Files.readAllBytes(new File(cheminAbsolu).toPath());
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la lecture du fichier", e);
        }
    }

    public String getCheminComplet(String cheminRelatif) {
        return new File(BASE_DIR, cheminRelatif).getPath().replace("\\", "/");
    }
}
