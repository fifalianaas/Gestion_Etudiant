package com.ge.java.utils;

import com.ge.java.model.Etudiant;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class remplirChamps {
    // ============================================================
    // Afficher l' etudiant selectioner
    // ============================================================
    public static void remplirChamps(
            TextField matri, TextField nom, TextField prenom, DatePicker dateNaiss,
            TextField email, TextField phone, ComboBox<String> parcour, ComboBox<String> anneeUniv,
            ComboBox<String> status, Etudiant etudiant, Button buttonAjouter
    ) {
        matri.setText(etudiant.getMatricule());
        nom.setText(etudiant.getNom());
        prenom.setText(etudiant.getPrenom());

        if (etudiant.getDateNaissance() == null || etudiant.getDateNaissance().isBlank()) {
            dateNaiss.setValue(null);
        } else {
            try {
                dateNaiss.setValue(java.time.LocalDate.parse(etudiant.getDateNaissance()));
            } catch (java.time.format.DateTimeParseException ex) {
                dateNaiss.setValue(null);
                dateNaiss.getEditor().setText(etudiant.getDateNaissance());
            }
        }

        email.setText(etudiant.getEmail());
        phone.setText(etudiant.getTelephone());
        parcour.getSelectionModel().select(String.valueOf(etudiant.getParcours()));
        anneeUniv.getSelectionModel().select(String.valueOf(etudiant.getAnneeUniversitaire()));
        status.getSelectionModel().select(String.valueOf(etudiant.getStatus()));



        // Cacher Button ajout en Update
        buttonAjouter.setVisible(false);
        buttonAjouter.setManaged(false);
    }
}
