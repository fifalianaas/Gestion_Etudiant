package com.ge.java.model;

import javafx.beans.property.*;

public class Etudiant {

    private final IntegerProperty id =
            new SimpleIntegerProperty();

    private final StringProperty matricule =
            new SimpleStringProperty();

    private final StringProperty nom =
            new SimpleStringProperty();

    private final StringProperty prenom =
            new SimpleStringProperty();

    private final StringProperty dateNaissance =
            new SimpleStringProperty();

    private final StringProperty email =
            new SimpleStringProperty();

    private final StringProperty telephone =
            new SimpleStringProperty();

    private final StringProperty parcours =
            new SimpleStringProperty();

    private final StringProperty anneeUniversitaire =
            new SimpleStringProperty();

    private final StringProperty status =
            new SimpleStringProperty();


    public Etudiant(
            int id,
            String matricule,
            String nom,
            String prenom,
            String dateNaissance,
            String email,
            String telephone,
            String parcours,
            String anneeUniversitaire,
            String status
    ) {

        this.id.set(id);
        this.matricule.set(matricule);
        this.nom.set(nom);
        this.prenom.set(prenom);
        this.dateNaissance.set(dateNaissance);
        this.email.set(email);
        this.telephone.set(telephone);
        this.parcours.set(parcours);
        this.anneeUniversitaire.set(anneeUniversitaire);
        this.status.set(status);
    }


    public int getId() {
        return id.get();
    }

    public String getMatricule() {
        return matricule.get();
    }

    public String getNom() {
        return nom.get();
    }

    public String getPrenom() {
        return prenom.get();
    }

    public String getDateNaissance() {
        return dateNaissance.get();
    }

    public String getEmail() {
        return email.get();
    }

    public String getTelephone() {
        return telephone.get();
    }

    public String getParcours() {
        return parcours.get();
    }

    public String getAnneeUniversitaire() {
        return anneeUniversitaire.get();
    }

    public String getStatus() {
        return status.get();
    }


}