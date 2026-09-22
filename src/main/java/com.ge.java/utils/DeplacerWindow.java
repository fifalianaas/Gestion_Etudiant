package com.ge.java.utils;

import javafx.scene.Node;
import javafx.stage.Stage;

public final class DeplacerWindow {

    private DeplacerWindow() {
    }

    public static void rendreDeplacable(Node header, Stage stage) {
        final double[] offsetX = {0};
        final double[] offsetY = {0};

        header.setOnMousePressed(e -> {
            offsetX[0] = e.getScreenX() - stage.getX();
            offsetY[0] = e.getScreenY() - stage.getY();
        });

        header.setOnMouseDragged(e -> {
            stage.setX(e.getScreenX() - offsetX[0]);
            stage.setY(e.getScreenY() - offsetY[0]);
        });
    }
}
