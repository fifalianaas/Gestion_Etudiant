package com.ge.java.style;

import javafx.scene.Scene;

import java.net.URL;

public final class Style {

    private Style() {
    }

    public static void appliquer(Scene scene) {
        URL css = Style.class.getResource("/style/style.css");

        if (css == null) {
            System.err.println("Aucun fichier CSS trouvé : /style/style.css");
            return;
        }

        String cssUrl = css.toExternalForm();
        if (!scene.getStylesheets().contains(cssUrl)) {
            scene.getStylesheets().add(cssUrl);
        }
    }
}
