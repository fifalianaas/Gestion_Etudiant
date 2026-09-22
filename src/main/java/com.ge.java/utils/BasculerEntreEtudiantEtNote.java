package com.ge.java.utils;
import javafx.animation.TranslateTransition;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class BasculerEntreEtudiantEtNote {

    public static void switchPane(Pane container, Pane oldPane, Pane newPane, boolean versDroite) {

        double width = container.getWidth();

        if (width <= 0) {
            width = container.getPrefWidth();
        }

        // Position initiale du nouveau Pane
        newPane.setTranslateX(versDroite ? width : -width);

        if (!container.getChildren().contains(newPane)) {
            container.getChildren().add(newPane);
        }

        // Animation ancien Pane
        TranslateTransition oldTransition =
                new TranslateTransition(Duration.millis(700), oldPane);

        oldTransition.setToX(versDroite ? -width : width);

        // Animation nouveau Pane
        TranslateTransition newTransition =
                new TranslateTransition(Duration.millis(700), newPane);

        newTransition.setToX(0);

        oldTransition.play();
        newTransition.play();

        // Nettoyage après l'animation
        newTransition.setOnFinished(e -> {
            container.getChildren().remove(oldPane);
            oldPane.setTranslateX(0);
        });
    }
}
