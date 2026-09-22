package com.ge.java.ui;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.shape.Circle;

import static com.ge.java.ui.FenetreEtudiant.*;
import static com.ge.java.utils.BasculerEntreEtudiantEtNote.switchPane;
import static com.ge.java.utils.Bordure.showBordure;
import static com.ge.java.utils.DeplacerWindow.rendreDeplacable;
import static com.ge.java.utils.ShowPane.showEtudiant;
import static com.ge.java.utils.ShowPane.showNote;

public class Header {

    public static Image icone = chargerIcon();

    public static void showHeader() {

        ImageView avatar =  new ImageView(icone);
        avatar.setFitHeight(30);
        avatar.setFitWidth(30);

        Circle circle = new Circle( 15,15,15);
        avatar.setClip(circle);

        Label iconWindow =  new Label();
        iconWindow.setId("iconWindow");

        iconWindow.setGraphic(avatar);

        Label titleWindow = new Label("  Gestion Etudiant");
        titleWindow.setId("titleWindow");

        Label setting = new Label(" Settings ");
        setting.setId("settings");


        Label btnToNote = new Label(" Autre ");
        btnToNote.setId("btnToAutre");

        Label btnToEtudiant = new Label(" Etudiant ");
        btnToEtudiant.setId("btnToEtudiant");

        showBordure(btnToEtudiant);// Active default


        HBox space = new HBox();
        HBox.setHgrow(space, Priority.ALWAYS);
        HBox.setHgrow(space, Priority.ALWAYS);

        // Control
        Button btnReduire = new Button("−");
        btnReduire.setId("btnReduire");

        Button btnAgrandir  = new Button("□");
        btnAgrandir.setId("btnAgrandir");

        Button btnExit = new Button("×");
        btnExit.setId("btnExit");

        menu.getChildren().addAll(iconWindow,titleWindow,setting,btnToEtudiant,btnToNote,
                space,btnReduire,btnAgrandir,btnExit);

        btnToNote.setOnMouseClicked(e -> {
            switchPane(root,layoutEtudiant, layoutAutre,true);
            showBordure(btnToNote);
            showNote(btnToEtudiant,setting,layoutEtudiant, layoutAutre);

        });
        btnToEtudiant.setOnMouseClicked(e -> {
            switchPane(root, layoutAutre,layoutEtudiant,false);
            showBordure(btnToEtudiant);
            showEtudiant(btnToNote,setting,layoutEtudiant, layoutAutre);
        });
        setting.setOnMouseClicked(e->{
            showBordure(setting);
            btnToEtudiant.setGraphic(null);
            btnToNote.setGraphic(null);
        });

        btnExit.setOnAction(e -> {
            stage.close();
            System.exit(0);
        });
        btnAgrandir.setOnAction(e -> {

            stage.setFullScreen(!stage.isFullScreen());

        });
        btnReduire.setOnAction(e -> {
            stage.setIconified(true);
        });


        rendreDeplacable(menu, stage);


    }

}
