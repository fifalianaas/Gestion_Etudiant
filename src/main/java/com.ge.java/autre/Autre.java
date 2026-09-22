package com.ge.java.autre;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.io.IOException;

import static com.ge.java.model.GestionEtudiant.EtudiantDao.getAllForListSaved;
import static com.ge.java.model.GestionEtudiant.nouvelleListes;
import static com.ge.java.ui.FenetreEtudiant.layoutAutre;
import static com.ge.java.ui.FenetreEtudiant.stage;
import static com.ge.java.utils.EnregistrerListe.*;

public class Autre {

    public static Button btnImporter;
    public static VBox preview;
    public static void ShowAutre() {

        VBox frame = new VBox(10);
        VBox.setVgrow(frame, Priority.ALWAYS);

        frame.setAlignment(Pos.TOP_CENTER);

        HBox headAutre =  new HBox(10);
        HBox.setHgrow(headAutre, Priority.ALWAYS);
        headAutre.setId("headAutre");

        Button btnExporter = new Button("Exporter une liste dans un fichier   >");
        btnExporter.setId("btnEnregistrer");

        Button btnListerImporter = new Button("Importer l'étudiant   >");
        btnListerImporter.setId("btnImporter");


        btnImporter = new Button("Importer");
        btnImporter.setId("btnImporter");


        VBox paneTable = new VBox(5);
        VBox.setVgrow(paneTable, Priority.ALWAYS);
        paneTable.setAlignment(Pos.TOP_CENTER);
        paneTable.setMaxWidth(850);



        preview = new VBox(5);
        preview.setId("preview");



        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(preview);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);




        VBox body = new VBox(10);
        body.setAlignment(Pos.TOP_CENTER);
        VBox.setVgrow(body, Priority.ALWAYS);
        body.setStyle("-fx-background-color: #00000000;");

        body.setVisible(false);
        body.setManaged(false);


        HBox footerEx = new HBox(10);
        footerEx.setId("footer_auth");
        HBox.setHgrow(footerEx, Priority.ALWAYS);

        HBox footerIm = new HBox(10);
        footerIm.setId("footer_auth");
        HBox.setHgrow(footerIm, Priority.ALWAYS);


        Button btnEnregistrer  = new Button("Enregistrer");
        btnEnregistrer.setId("btnEnregistrer");


        btnExporter.setOnAction(e -> {

            footerEx.setManaged(true);
            footerEx.setVisible(true);
            footerIm.setVisible(false);
            footerIm.setManaged(false);

            body.setManaged(true);
            body.setVisible(true);

            nouvelleListes.clear();

            getAllForListSaved();
            preview.getChildren().clear();

            for (String  item : nouvelleListes) {

                HBox hBox = new HBox();
                hBox.setAlignment(Pos.CENTER_LEFT);
                HBox.setHgrow(hBox, Priority.ALWAYS);

                Label lbl = new Label("EXP : [ "+item+" ]");
                lbl.setId("lblNouvelleListe");

                hBox.getChildren().add(lbl);

                preview.getChildren().add(hBox);
            }


        });


        btnEnregistrer.setOnAction(event -> {
            try {
                saveListe(btnEnregistrer.getScene().getWindow());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });


        btnListerImporter.setOnAction(e -> {
            footerEx.setVisible(false);
            footerEx.setManaged(false);
            footerIm.setManaged(true);
            footerIm.setVisible(true);

            body.setManaged(true);
            body.setVisible(true);

            preview.getChildren().clear();
            hBoxList.clear();

            choisirEtImporter(stage);

        });


        footerEx.getChildren().add(btnEnregistrer);
        footerIm.getChildren().add(btnImporter);

        headAutre.getChildren().addAll(btnExporter,btnListerImporter);
        paneTable.getChildren().add(scrollPane);
        body.getChildren().addAll(paneTable,footerEx,footerIm);
        frame.getChildren().addAll(headAutre,body);
        layoutAutre.getChildren().add(frame);
    }
}
