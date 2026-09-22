package com.ge.java.utils;

import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Bordure {
    public static void showBordure(Label label) {

        Image bordure = new Image(Bordure.class.getClassLoader().getResourceAsStream("image/bord.png"));

        ImageView image = new ImageView(bordure);
        image.setFitHeight(28);
        image.setFitWidth(80);
        image.setPreserveRatio(false);

        label.setGraphic(image);
        label.setContentDisplay(ContentDisplay.CENTER);
        label.setAlignment(Pos.CENTER);
        label.setMinWidth(80);
        label.setMaxWidth(80);
        label.setPrefWidth(80);


    }
}
