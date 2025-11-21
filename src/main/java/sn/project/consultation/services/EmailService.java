package sn.project.consultation.services;

import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
@Slf4j
@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    /**
     * Envoi d'un email simple (texte brut)
     */
    public void envoyerEmail(String to, String sujet, String contenu) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(sujet);
            message.setText(contenu);
            message.setFrom(from);

            mailSender.send(message);
            log.info("✅ Email simple envoyé à {}", to);
        } catch (Exception e) {
            log.error("❌ Erreur lors de l'envoi d'un email simple à {} : {}", to, e.getMessage());
            throw new RuntimeException("Impossible d'envoyer l'email", e);
        }
    }

    /**
     * Envoi d'un email avec pièce jointe en mémoire (ex: PDF en byte[])
     */
    public void envoyerEmail(String to, String sujet, String contenu, byte[] pdf, String nomFichier) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(to);
            helper.setSubject(sujet);
            helper.setText(contenu, true); // true = support HTML
            helper.setFrom(from);

            helper.addAttachment(nomFichier, new ByteArrayResource(pdf));

            mailSender.send(mimeMessage);
            log.info("✅ Email avec pièce jointe envoyé à {}", to);
        } catch (Exception e) {
            log.error("❌ Erreur lors de l'envoi d'un email avec pièce jointe à {} : {}", to, e.getMessage());
            throw new RuntimeException("Erreur lors de l'envoi de l'email avec pièce jointe", e);
        }
    }

    /**
     * Envoi d'un email avec fichier joint depuis un chemin local
     */
    public void envoyerEmail(String to, String sujet, String contenu, String cheminFichier) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(to);
            helper.setSubject(sujet);
            helper.setText(contenu, true);
            helper.setFrom(from);

            FileSystemResource file = new FileSystemResource(new File(cheminFichier));
            if (!file.exists()) {
                throw new RuntimeException("Fichier non trouvé : " + cheminFichier);
            }

            helper.addAttachment(file.getFilename(), file);

            mailSender.send(mimeMessage);
            log.info("✅ Email avec fichier joint envoyé à {}", to);
        } catch (Exception e) {
            log.error("❌ Erreur lors de l'envoi d'un email avec fichier joint à {} : {}", to, e.getMessage());
            throw new RuntimeException("Erreur lors de l'envoi de l'email avec fichier joint", e);
        }
    }
}


