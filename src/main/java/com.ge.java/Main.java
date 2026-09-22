package com.ge.java;

import javafx.application.Application;
import javafx.stage.Stage;

import com.ge.java.database.Database;
import com.ge.java.ui.FenetreEtudiant;

public class Main extends Application {

    @Override
    public void start(Stage stage) {

        // Initialiser la base de données
        Database.initDatabase();

        FenetreEtudiant fenetre = new FenetreEtudiant();

        fenetre.createWidgets();
    }

    public static void main(String[] args) {
        launch(args);
    }
}