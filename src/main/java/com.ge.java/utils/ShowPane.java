package com.ge.java.utils;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class ShowPane {
    // Fonction pour reafficher Pane formulaire
    public static void showFormulaire(Button actionBtn, Pane pane, Button buttonShow) {
        actionBtn.setOnAction(e -> {
            pane.setManaged(true);
            pane.setVisible(true);

            buttonShow.setVisible(false);
            buttonShow.setManaged(false);
        });
    }

    public static void showNote(
         Label btnToEtudiant,
         Label setting,
         VBox layoutEtudiant,
         VBox layoutNote

    ) {
        btnToEtudiant.setGraphic(null);
        setting.setGraphic(null);
        layoutEtudiant.setVisible(false);
        layoutEtudiant.setManaged(false);
        layoutNote.setManaged(true);
        layoutNote.setVisible(true);
    }

    public static void showEtudiant(
            Label btnToNote,
            Label setting,
            VBox layoutEtudiant,
            VBox layoutNote

    ) {
        btnToNote.setGraphic(null);
        setting.setGraphic(null);
        layoutEtudiant.setManaged(true);
        layoutEtudiant.setVisible(true);
        layoutNote.setVisible(false);
        layoutNote.setManaged(false);
    }
}
