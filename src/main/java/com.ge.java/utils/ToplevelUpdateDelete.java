package com.ge.java.utils;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/** Petite fenêtre sans décoration pour les actions sur un étudiant. */
public final class ToplevelUpdateDelete {

    private static Stage currentStage;

    private ToplevelUpdateDelete() {
    }

    public static void creerToplevelUpdateDelete(Image icon, Runnable onUpdate, Runnable onDelete,
                                                 String texte) {
        fermer();

        Stage topLevel = new Stage();
        currentStage = topLevel;
        topLevel.initStyle(StageStyle.TRANSPARENT);
        if (icon != null) {
            topLevel.getIcons().add(icon);
        }

        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #00000050;");

        VBox frame = new VBox(15);
        frame.setAlignment(Pos.TOP_CENTER);
        frame.setMaxWidth(300);
        frame.setPrefHeight(140);
        frame.setStyle("-fx-background-color: gray; -fx-background-radius: 15;");


        frame.setOnMouseExited(event -> {
            fermer();
        });


        HBox head = new HBox();
        head.setPrefWidth(300);
        head.setMinHeight(35);
        head.setAlignment(Pos.CENTER_RIGHT);
        head.setStyle("-fx-background-color: #202020;");

        Label title = new Label(texte);
        title.setStyle("-fx-text-fill: white;");
        title.setPrefWidth(250);

        Label exitButton = new Label("  ✕  ");
        exitButton.setStyle(styleExit(false));
        exitButton.setOnMouseEntered(e -> exitButton.setStyle(styleExit(true)));
        exitButton.setOnMouseExited(e -> exitButton.setStyle(styleExit(false)));
        exitButton.setOnMouseClicked(e -> topLevel.close());
        head.getChildren().addAll(title,exitButton);

        Button btnUpdate = new Button("Modifier");
        btnUpdate.setStyle("-fx-background-color: #0000ff;-fx-text-fill: white;-fx-font-weight: bold;-fx-cursor: hand;");
        Button btnDelete = new Button("Supprimer");
        btnDelete.setStyle("-fx-background-color: orangered;-fx-text-fill: white;-fx-font-weight: bold;-fx-cursor: hand;");
        btnUpdate.setPrefWidth(250);
        btnDelete.setPrefWidth(250);

        btnUpdate.setOnAction(e -> {
            if (onUpdate != null) {
                onUpdate.run();
            }
        });
        btnDelete.setOnAction(e -> {
            if (onDelete != null) {
                onDelete.run();
            }
        });

        frame.getChildren().setAll(head, btnUpdate, btnDelete);
        root.getChildren().add(frame);

        DeplacerWindow.rendreDeplacable(head, topLevel);

        Scene scene = new Scene(root, 1500,800);
        scene.setFill(Color.TRANSPARENT);
        topLevel.setScene(scene);
        topLevel.setOnHidden(e -> {
            if (currentStage == topLevel) {
                currentStage = null;
            }
        });
        topLevel.show();
    }

    public static void fermer() {
        if (currentStage != null) {
            currentStage.close();
            currentStage = null;
        }
    }

    private static String styleExit(boolean hover) {
        return "-fx-background-color: " + (hover ? "red" : "#262626") + 
                "; -fx-text-fill: #ffffff; -fx-font-size: 15; -fx-padding: 5px 0px;";
    }
}
