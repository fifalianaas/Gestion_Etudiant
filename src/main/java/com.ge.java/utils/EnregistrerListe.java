package com.ge.java.utils;

import com.ge.java.model.Etudiant;
import com.ge.java.model.GestionEtudiant;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static com.ge.java.autre.Autre.preview;
import static com.ge.java.model.GestionEtudiant.EtudiantDao.getAllForListSaved;
import static com.ge.java.model.GestionEtudiant.nouvelleListes;


public class EnregistrerListe {

    public static HBox hBox;

    public static List<HBox> hBoxList  = new ArrayList<>();
    public static String lien;

    public static void saveListe(Window owner) throws IOException {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Exporter la liste des étudiants");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Fichier texte (*.txt)", "*.txt")
        );
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Tous les fichiers (*.*)", "*.*")
        );
        chooser.setInitialFileName("etudiants.txt");

        java.io.File fichier = chooser.showSaveDialog(owner);

        if (fichier == null) {
            return; // Annuler
        }

        nouvelleListes.clear();
        getAllForListSaved();

        Path chemin = fichier.toPath();
        if (!chemin.toString().toLowerCase().endsWith(".txt")) {
            chemin = Path.of(chemin + ".txt");
        }

        Files.write(
                chemin,
                nouvelleListes,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
        );

        new Alert(Alert.AlertType.INFORMATION,
                "Export terminé !\\n\\nFichier : " + chemin.getFileName() +
                "\\nÉtudiants exportés : " + nouvelleListes.size()
        ).showAndWait();
    }


    /**
     * Ouvre un sélecteur de fichier puis importe la liste choisie dans SQLite.
     * L'interface graphique n'est pas modifiée : cette méthode est appelée
     * directement par le bouton Importer.
     */
    public static void choisirEtImporter(Window owner) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Importer la liste des étudiants");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Fichier texte (*.txt)", "*.txt")
        );
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Tous les fichiers (*.*)", "*.*")
        );

        java.io.File fichier = chooser.showOpenDialog(owner);

        if (fichier == null) {
            return; // Annuler
        }

        lien = fichier.toString();

        try {
            lireLesDonnees(lien,preview);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /** Importe un fichier TXT UTF-8 contenant 10 colonnes séparées par |. */
    public static void ImportListe(Path fichier) throws IOException {
        List<String> lignes = Files.readAllLines(fichier, StandardCharsets.UTF_8);

        int importes = 0;
        int ignores = 0;
        int erreurs = 0;

        for (String ligne : lignes) {
            ligne = ligne.replace("\uFEFF", "").trim();

            if (ligne.isBlank() || ligne.startsWith("[")) {
                continue;
            }

            String[] data = ligne.split("\\|", -1);
            if (data.length != 10) {
                ignores++;
                continue;
            }

            String matricule = data[1].trim();
            String nom = data[2].trim();
            String prenom = data[3].trim();
            String dateNaissance = data[4].trim();
            String email = data[5].trim();
            String telephone = data[6].trim();
            String parcours = data[7].trim();
            String anneeUniversitaire = data[8].trim();
            String status = data[9].trim();

            if (matricule.isBlank() || nom.isBlank()) {
                ignores++;
                continue;
            }

            // Le matricule est UNIQUE dans SQLite : on vérifie avant l'INSERT.
            if (GestionEtudiant.EtudiantDao.existeMatricule(matricule)) {
                ignores++;
                continue;
            }

            Etudiant etudiant = new Etudiant(
                    0, matricule, nom, prenom, dateNaissance, email,
                    telephone, parcours, anneeUniversitaire, status
            );

            if (GestionEtudiant.EtudiantDao.ajouter(etudiant)) {
                importes++;
            } else {
                erreurs++;
            }
        }

        Alert resultat = new Alert(Alert.AlertType.INFORMATION);
        resultat.setHeaderText("Import terminé");
        resultat.setContentText(
                "Étudiants importés : " + importes +
                "\nDoublons/lignes ignorés : " + ignores +
                "\nErreurs : " + erreurs
        );
        resultat.showAndWait();
    }


    public static void lireLesDonnees(String fichier, VBox vBox) throws IOException {

        List<String> lignes = Files.readAllLines(Path.of(fichier));

        for (String ligne : lignes) {

            if (ligne.startsWith("[") || ligne.isBlank()) {
                continue;
            }

            String[] data = ligne.split("\\|");

            String id = (data[0]);
            String matricule = data[1];
            String nom = data[2];
            String prenom = data[3];
            String dateNaissance = data[4];
            String email = data[5];
            String telephone = data[6];
            String parcours = data[7];
            String AnneeUniversitaire = data[8];
            String status = data[9];


            hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_LEFT);
            HBox.setHgrow(hBox, Priority.ALWAYS);

            Label lbl = new Label(
                    "EXP : [ " +id + " | " + matricule + " | " + nom + " | " + prenom + " | " +
                    dateNaissance + " | " + email + " | " + telephone + " | " +
                    parcours + " | "+
                    AnneeUniversitaire+" | "+status+ " ]"
            );
            lbl.setId("lblNouvelleListe");

            hBox.getChildren().add(lbl);
            hBoxList.add(hBox);

        }
        vBox.getChildren().addAll(hBoxList);


    }





}