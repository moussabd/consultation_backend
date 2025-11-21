package sn.project.consultation.services;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.stereotype.Service;
import sn.project.consultation.data.entities.*;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Service
public class PDFGeneratorService {

    private static final Font TITLE_FONT = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD, BaseColor.BLUE);
    private static final Font SECTION_FONT = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.DARK_GRAY);
    private static final Font NORMAL_FONT = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);

    public byte[] genererFacturePDF(Paiement paiement, String numeroFacture) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, baos);

            document.open();

            // 🔷 En-tête avec logo et titre
            ajouterEnTete(document, numeroFacture);

            // 👤 Infos patient et pro
            ajouterInfosClient(document, paiement);

            // 📋 Détails de la facture
            ajouterTableFacture(document, paiement);

            // 🧾 Résumé final
            ajouterTotal(document, paiement);

            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération de la facture PDF", e);
        }
    }

    public byte[] generateFicheMedicalePDF(DossierMedical dossier)  {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                Document document = new Document(PageSize.A4);
                PdfWriter.getInstance(document, baos);
                document.open();

                ajouterEnTeteFicheMedicale(document, dossier.getId().toString());
                ajouterInfosPatient(document, dossier);
                ajouterExamenClinique(document, dossier);
                if (dossier.getExamensComplementaires() !=null ) {
                    ajouterExamensComplementaires(document, dossier.getExamensComplementaires());
                }
                ajouterDiagnostics(document, dossier);

                if (dossier.getTraitements() !=null) {
                    ajouterTraitements(document, dossier.getTraitements());
                }

                ajouterConclusion(document, dossier);
                document.close();
                return baos.toByteArray();
            } catch(Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Erreur lors de la génération de la fiche médicale", e);
            }
    }

    private void ajouterEnTeteFicheMedicale(Document doc, String numero) throws Exception {
        Paragraph titre = new Paragraph("FICHE MEDICALE", new Font(Font.FontFamily.HELVETICA, 22, Font.BOLD,
                BaseColor.BLACK));
        titre.setAlignment(Element.ALIGN_CENTER);
        doc.add(titre);

        Paragraph ref = new Paragraph("N Dossier : " + numero, new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.GRAY));
        ref.setAlignment(Element.ALIGN_RIGHT);
        doc.add(ref);

        doc.add(Chunk.NEWLINE);
    }

    private void ajouterEnTete(Document doc, String numero) throws Exception {
        Paragraph titre = new Paragraph("FACTURE", new Font(Font.FontFamily.HELVETICA, 22, Font.BOLD, BaseColor.BLACK));
        titre.setAlignment(Element.ALIGN_CENTER);
        doc.add(titre);

        Paragraph ref = new Paragraph("Numéro : " + numero, new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.GRAY));
        ref.setAlignment(Element.ALIGN_RIGHT);
        doc.add(ref);

        doc.add(Chunk.NEWLINE);
    }

    private void ajouterExamensComplementaires(Document doc, ExamensComplementaires examens) throws Exception {
        Paragraph titre = new Paragraph("Examens Complémentaires", SECTION_FONT);
        titre.setSpacingAfter(5);
        doc.add(titre);

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new int[]{3, 7});
        table.setSpacingAfter(15);

        Font tableHeader = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLACK);

        if (examens.getAnalysesSanguines() != null && !examens.getAnalysesSanguines().isEmpty()) {
            table.addCell(new PdfPCell(new Phrase("Analyses sanguines", tableHeader)));
            table.addCell(new PdfPCell(new Phrase(
                    examens.getAnalysesSanguines().stream()
                            .map(a -> a.getNom() + " : " + a.getValeur())
                            .reduce((a,b) -> a + ", " + b)
                            .orElse("N/A")
            )));
        }

        // Analyses urines
        if (examens.getAnalysesUrines() != null && !examens.getAnalysesUrines().isEmpty()) {
            table.addCell(new PdfPCell(new Phrase("Analyses urines", tableHeader)));
            table.addCell(new PdfPCell(new Phrase(
                    examens.getAnalysesUrines().stream()
                            .map(a -> a.getNom() + " : " + a.getValeur())
                            .reduce((a,b) -> a + ", " + b)
                            .orElse("N/A")
            )));
        }

        // Radiographies
        if (examens.getRadiographies() != null && !examens.getRadiographies().isEmpty()) {
            table.addCell(new PdfPCell(new Phrase("Radiographies", tableHeader)));
            table.addCell(new PdfPCell(new Phrase(String.join(", ", examens.getRadiographies()))));
        }

        // Échographies
        if (examens.getEchographies() != null && !examens.getEchographies().isEmpty()) {
            table.addCell(new PdfPCell(new Phrase("Échographies", tableHeader)));
            table.addCell(new PdfPCell(new Phrase(String.join(", ", examens.getEchographies()))));
        }

        // Scanners
        if (examens.getScanners() != null && !examens.getScanners().isEmpty()) {
            table.addCell(new PdfPCell(new Phrase("Scanners", tableHeader)));
            table.addCell(new PdfPCell(new Phrase(String.join(", ", examens.getScanners()))));
        }

        // IRM
        if (examens.getIrm() != null && !examens.getIrm().isEmpty()) {
            table.addCell(new PdfPCell(new Phrase("IRM", tableHeader)));
            table.addCell(new PdfPCell(new Phrase(String.join(", ", examens.getIrm()))));
        }

        // Tests spécialisés
        if (examens.getTestsSpeciaux() != null && !examens.getTestsSpeciaux().isEmpty()) {
            table.addCell(new PdfPCell(new Phrase("Tests spécialisés", tableHeader)));
            table.addCell(new PdfPCell(new Phrase(
                    examens.getTestsSpeciaux().stream()
                            .map(t -> t.getNom() + " : " + t.getResultat())
                            .reduce((a,b) -> a + ", " + b)
                            .orElse("N/A")
            )));
        }


        doc.add(table);
        doc.add(Chunk.NEWLINE);
    }

    private void ajouterInfosPatient(Document doc, DossierMedical dossier) throws Exception {
        Paragraph titre = new Paragraph("Informations Patient", SECTION_FONT);
        titre.setSpacingAfter(5);
        doc.add(titre);

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingAfter(15);
        table.setWidths(new int[]{3,7});

        table.addCell(new PdfPCell(new Phrase("Nom", SECTION_FONT)));
        table.addCell(new PdfPCell(new Phrase(dossier.getPatient().getNom(), NORMAL_FONT)));

        table.addCell(new PdfPCell(new Phrase("Sexe", SECTION_FONT)));
        table.addCell(new PdfPCell(new Phrase(dossier.getPatient().getSexe(), NORMAL_FONT)));

        table.addCell(new PdfPCell(new Phrase("Date de naissance", SECTION_FONT)));
        table.addCell(new PdfPCell(new Phrase(dossier.getPatient().getDateNaissance().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), NORMAL_FONT)));

        table.addCell(new PdfPCell(new Phrase("Médecin référent", SECTION_FONT)));
        table.addCell(new PdfPCell(new Phrase("Dr. " + dossier.getCorrespondances().getCompteRenduHospitalisation().getAuteur().getPrenom() + " (" + dossier.getCorrespondances().getCompteRenduHospitalisation().getAuteur().getNom() + ")", NORMAL_FONT)));

        doc.add(table);

    }

    private void ajouterExamenClinique(Document doc, DossierMedical dossier) throws Exception {
        Paragraph titre = new Paragraph("Examen Clinique", SECTION_FONT);
        titre.setSpacingAfter(5);
        doc.add(titre);
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new int[]{3, 2});
        table.setSpacingAfter(15);

        Font headFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        table.addCell(new PdfPCell(new Phrase("Paramètre", headFont)));
        table.addCell(new PdfPCell(new Phrase("Valeur", headFont)));

        table.addCell("Taille");
        table.addCell(dossier.getExamenClinique().getTaille()+ " cm");

        table.addCell("Poids");
        table.addCell(dossier.getExamenClinique().getPoids()+ " kg");

        table.addCell("Tension Artérielle");
        table.addCell(dossier.getExamenClinique().getTensionArterielle());

        table.addCell("Température");
        table.addCell(dossier.getExamenClinique().getTemperature() + " °C");

        table.addCell("Fréquence Cardiaque");
        table.addCell(dossier.getExamenClinique().getFrequenceCardiaque() + " bpm");

        table.addCell("Saturation O2");
        table.addCell(dossier.getExamenClinique().getSaturationOxygene() + " %");

        doc.add(table);

        if (dossier.getExamenClinique().getObservations() !=null && dossier.getExamenClinique().getObservations().isEmpty()) {
            Paragraph obs = new Paragraph("Observations : "+ String.join(", ", dossier.getExamenClinique().getObservations()));
            obs.setSpacingBefore(10);
            doc.add(obs);
        }
        doc.add(Chunk.NEWLINE);
    }

    private void ajouterDiagnostics(Document doc, DossierMedical dossier) throws Exception {
        Paragraph titreDiag = new Paragraph("🧾 Diagnostic Médical", SECTION_FONT);
        titreDiag.setSpacingAfter(5);
        doc.add(titreDiag);

        doc.add(new Paragraph("Diagnostic principal : " + dossier.getDiagnosticMedical().getDiagnosticPrincipal(), NORMAL_FONT));
        if (!dossier.getDiagnosticMedical().getDiagnosticsSecondaires().isEmpty()) {
            doc.add(new Paragraph("Diagnostics secondaires : " + String.join(", ", dossier.getDiagnosticMedical().getDiagnosticsSecondaires()), NORMAL_FONT));
        }


    }

    private void ajouterTraitements(Document doc, TraitementPrescription traitement) throws Exception {
        Paragraph titreTrait = new Paragraph("💊 Traitements / Prescriptions", SECTION_FONT);
        titreTrait.setSpacingBefore(10);
        titreTrait.setSpacingAfter(5);
        doc.add(titreTrait);

        // 🧪 Médicaments prescrits
        if (traitement.getMedicaments() != null && !traitement.getMedicaments().isEmpty()) {
            Paragraph sousTitre = new Paragraph("Médicaments prescrits", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLACK));
            sousTitre.setSpacingAfter(3);
            doc.add(sousTitre);

            PdfPTable tableMed = new PdfPTable(5);
            tableMed.setWidthPercentage(100);
            tableMed.setWidths(new int[]{3, 2, 2, 2, 3});

            tableMed.addCell("Nom");
            tableMed.addCell("Posologie");
            tableMed.addCell("Voie");
            tableMed.addCell("Durée (jours)");
            tableMed.addCell("Commentaire");

            for (MedicamentPrescrit med : traitement.getMedicaments()) {
                tableMed.addCell(med.getNom());
                tableMed.addCell(med.getPosologie());
                tableMed.addCell(med.getVoie());
                tableMed.addCell(med.getDuree() != null ? med.getDuree().toString() : "N/A");
                tableMed.addCell(med.getCommentaire() != null ? med.getCommentaire() : "-");
            }

            tableMed.setSpacingAfter(10);
            doc.add(tableMed);
        }

        // 🩺 Soins paramédicaux
        if (traitement.getSoins() != null && !traitement.getSoins().isEmpty()) {
            Paragraph sousTitre = new Paragraph("Soins paramédicaux", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLACK));
            sousTitre.setSpacingAfter(3);
            doc.add(sousTitre);

            PdfPTable tableSoins = new PdfPTable(3);
            tableSoins.setWidthPercentage(100);
            tableSoins.setWidths(new int[]{4, 2, 4});

            tableSoins.addCell("Type de soin");
            tableSoins.addCell("Fréquence");
            tableSoins.addCell("Commentaire");

            for (SoinsParamedicaux soin : traitement.getSoins()) {
                tableSoins.addCell(soin.getTypeSoin());
                tableSoins.addCell(soin.getFrequence());
                tableSoins.addCell(soin.getCommentaire() != null ? soin.getCommentaire() : "-");
            }

            tableSoins.setSpacingAfter(10);
            doc.add(tableSoins);
        }

        // 🏥 Interventions chirurgicales prévues
        if (traitement.getInterventions() != null && !traitement.getInterventions().isEmpty()) {
            Paragraph sousTitre = new Paragraph("Interventions chirurgicales prévues", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLACK));
            sousTitre.setSpacingAfter(3);
            doc.add(sousTitre);

            PdfPTable tableInter = new PdfPTable(4);
            tableInter.setWidthPercentage(100);
            tableInter.setWidths(new int[]{3, 2, 3, 3});

            tableInter.addCell("Nom");
            tableInter.addCell("Date prévue");
            tableInter.addCell("Objectif");
            tableInter.addCell("Commentaire");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            for (InterventionChirurgicale inter : traitement.getInterventions()) {
                tableInter.addCell(inter.getNom());
                tableInter.addCell(inter.getDatePrevue() != null ? inter.getDatePrevue().format(formatter) : "N/A");
                tableInter.addCell(inter.getObjectif());
                tableInter.addCell(inter.getCommentaire() != null ? inter.getCommentaire() : "-");
            }

            tableInter.setSpacingAfter(10);
            doc.add(tableInter);
        }
    }

    private void ajouterConclusion(Document doc, DossierMedical dossier) throws Exception {
        Paragraph titre = new Paragraph("🖊 Conclusion", SECTION_FONT);
        titre.setSpacingAfter(5);
        doc.add(titre);

        doc.add(Chunk.NEWLINE);
        doc.add(new Paragraph("Fait à Dakar, le " + java.time.LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), NORMAL_FONT));
        doc.add(new Paragraph("Médecin référent : Dr. " + dossier.getCorrespondances().getCompteRenduHospitalisation().getAuteur().getPrenom() + " (" + dossier.getCorrespondances().getCompteRenduHospitalisation().getAuteur().getNom() + ")", NORMAL_FONT));
        doc.add(new Paragraph("Signature : __________________________", NORMAL_FONT));
    }

    private void ajouterInfosClient(Document doc, Paiement paiement) throws Exception {
        Paragraph infos = new Paragraph();
        infos.add(new Phrase("Patient : " + paiement.getPatient().getNom() + "\n"));
        infos.add(new Phrase("Email : " + paiement.getPatient().getCoordonnees().getEmail() + "\n"));
        infos.add(new Phrase("Téléphone : " + paiement.getPatient().getCoordonnees().getNumeroTelephone() + "\n\n"));

        infos.add(new Phrase("Professionnel : Dr. " + paiement.getProfessionnel().getNom() + "\n"));
        infos.add(new Phrase("Spécialité : " + paiement.getProfessionnel().getSpecialite() + "\n"));
        infos.setSpacingAfter(20);
        doc.add(infos);
    }

    private void ajouterTableFacture(Document doc, Paiement paiement) throws Exception {
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setWidths(new int[]{6, 2, 2});

        Font headFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);

        PdfPCell cell;

        cell = new PdfPCell(new Phrase("Description", headFont));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Quantité", headFont));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Montant (FCFA)", headFont));
        table.addCell(cell);

        // ✅ Exemple de ligne
        table.addCell("Consultation à domicile");
        table.addCell("1");
        table.addCell(String.format("%,.0f", paiement.getMontant()));

        doc.add(table);
    }

    private void ajouterTotal(Document doc, Paiement paiement) throws Exception {
        doc.add(Chunk.NEWLINE);
        Paragraph total = new Paragraph("Total payé : " + String.format("%,.0f FCFA", paiement.getMontant()),
                new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD));
        total.setAlignment(Element.ALIGN_RIGHT);
        doc.add(total);

        doc.add(new Paragraph("Date : " + paiement.getDatePaiement().format(DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm")),
                new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC)));
    }
}

