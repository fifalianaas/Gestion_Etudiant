package com.ge.java.utils;

import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

public class CacherPane {
    // Fonction pour cacher Pane formulaire
    public static void cacherFormulaire(Button actionBtn, Pane pane, Button buttonShow) {
        actionBtn.setOnAction(e -> {
            pane.setVisible(false);
            pane.setManaged(false);

            buttonShow.setManaged(true);
            buttonShow.setVisible(true);
        });
    }
}
