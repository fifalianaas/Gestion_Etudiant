package com.ge.java.ui;


import com.ge.java.model.Etudiant;
import com.ge.java.model.GestionEtudiant;
import com.ge.java.style.Style;
import com.ge.java.utils.ToplevelUpdateDelete;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import static com.ge.java.autre.Autre.*;
import static com.ge.java.ui.Header.showHeader;
import static com.ge.java.utils.CacherPane.cacherFormulaire;

import static com.ge.java.utils.EnregistrerListe.*;
import static com.ge.java.utils.EnregistrerListe.lien;
import static com.ge.java.utils.ShowPane.*;
import static com.ge.java.utils.ToplevelUpdateDelete.creerToplevelUpdateDelete;
import static com.ge.java.utils.remplirChamps.remplirChamps;


public class FenetreEtudiant {

    public static Stage stage;
    public static HBox menu;
    public static VBox root;
    public static VBox layoutEtudiant;
    public static VBox layoutAutre;

    private int ETUDIANTS_PAR_PAGE = 10;
    private int pageActuelle = 1;
    private int totalPages = 1;
    public VBox pane_table;
    private HBox pagination;
    private int selectedId = -1;
    private ComboBox<String> addEtuParPage;


    private Button btnModifierForm;
    private Button btnAjouter;
    private TextField matriculeForm;
    private TextField nomForm;
    private TextField prenomForm;
    private DatePicker dateForm;
    private TextField emailForm;
    private TextField telephoneForm;
    private ComboBox<String> parcoursForm;
    private ComboBox<String> anneeForm;
    private ComboBox<String> statusForm;
    private VBox tableContainerRef;

    private Button btnShowForm;
    private VBox paneForm;


    // Liste enregistrera dans fichier txt



    private final Image icon = chargerIcon();

    public static Image chargerIcon() {
        var stream = FenetreEtudiant.class.getClassLoader().getResourceAsStream("icon/icon.png");
        return stream == null ? null : new Image(stream);
    }


    public FenetreEtudiant() {

        stage = new Stage();
        if (icon != null) {
            stage.getIcons().add(icon);
        }
    }

    // ========================================================
    // WIDGETS DE CLASS FenetreEtudiant
    // ========================================================
    public void createWidgets() {


        // ========================================================
        // ROOT ETUDIANT
        // ========================================================

        root = new VBox();
        root.setId("fenetreEtudiant");
        VBox.setVgrow(root, Priority.ALWAYS);


        layoutEtudiant = new VBox(8);
        layoutEtudiant.setPadding(new Insets(10));
        layoutEtudiant.setId("fenetreEtudiant");

        VBox.setVgrow(layoutEtudiant, Priority.ALWAYS);

        layoutAutre = new VBox(8);
        layoutAutre.setPadding(new Insets(10));
        layoutAutre.setId("fenetreAutre");
        VBox.setVgrow(layoutAutre, Priority.ALWAYS);

        ShowAutre();
        // cacher au debut


        // ========================================================
        // HEADER
        // ========================================================

        Pane header = new VBox(20);
        header.setPadding(new Insets(5, 5, 0, 5));
        header.setId("header");


        VBox vb_header = new VBox();

        Label title = new Label("ZONE DE RECHERCHE");
        title.setId("title_search");
        title.setMaxWidth(Double.MAX_VALUE);

        Label lbl_search = new Label("🔍 Recherche d'etudiant");
        lbl_search.setId("lbl_search");


        HBox pane_widgets_header = new HBox(5);
        pane_widgets_header.setMaxWidth(Double.MAX_VALUE);
        pane_widgets_header.setAlignment(Pos.CENTER_LEFT);
        pane_widgets_header.setId("pane_widgets_h");


        Label matri = new Label("Matricule : ");
        matri.setId("lbl");

        TextField entry_matricule = new TextField();
        entry_matricule.setId("entry_matricule");

        Label nom = new Label("Nom : ");
        nom.setId("lbl");
        TextField entry_nom = new TextField();
        Label parcours = new Label("Parcours : ");
        parcours.setId("lbl");

        String[] data_parcours = {"Genie logiciel", "Developpement d'application"};
        ComboBox<String> combo_parcours = new ComboBox<>();
        combo_parcours.getItems().addAll(data_parcours);
        combo_parcours.setPromptText("------------");


        Label status = new Label("Status : ");
        status.setId("lbl");


        String[] data_status = {"Actif", "Inactif"};

        ComboBox<String> combo_status = new ComboBox<>();
        combo_status.getItems().addAll(data_status);
        combo_status.setPromptText("-------------");


        Button btn_search = new Button("🔍 Rechercher");
        btn_search.setId("btn_search");

        Button btn_clear = new Button("⟲ Reinitialiser");
        btn_clear.setId("btn_clear");


        pane_widgets_header.getChildren().addAll(
                matri,
                entry_matricule,
                nom,
                entry_nom,
                parcours,
                combo_parcours,
                status,
                combo_status,
                btn_search,
                btn_clear
        );

        HBox.setHgrow(entry_nom, Priority.ALWAYS);

        vb_header.getChildren().addAll(title, lbl_search, pane_widgets_header);

        header.getChildren().add(vb_header);


        // ========================================================
        // BODY
        // ========================================================

        Pane body = new HBox(10);
        HBox.setHgrow(body, Priority.ALWAYS);
        VBox.setVgrow(body, Priority.ALWAYS);

        body.setId("body");


        // ========================================================
        // FORMULAIRE
        // ========================================================

        VBox pane_formulaire = new VBox(10);
        pane_formulaire.setId("pane_formulaire");
        VBox.setVgrow(pane_formulaire, Priority.ALWAYS);


        Label lbl_titre_form = new Label("👤 Formulaire Etudiant : ");
        lbl_titre_form.setId("lbl_titre_form");


        Pane frames_form = new HBox(0);
        frames_form.setId("frames_form");

        Pane left_form = new VBox(10);
        left_form.setId("left_form");
        HBox.setHgrow(left_form, Priority.ALWAYS);


        String[] textes = {
                "Matricule : ",
                "Nom : ",
                "Prenom : ",
                "Date de naissance : ",
                "Email : ",
                "Telephone : ",
                "Parcours : ",
                "Annee universitaire : ",
                "Status : "
        };

        String[] ids = {
                "lbl_matricule",
                "lbl_nom",
                "lbl_prenom",
                "lbl_date_naiss",
                "lbl_email",
                "lbl_telephone",
                "lbl_parcours",
                "lbl_annee_univ",
                "lbl_status"
        };


        for (int i = 0; i < textes.length; i++) {

            Label label = new Label(textes[i]);
            label.setId(ids[i]);
            left_form.getChildren().add(label);

        }


        Pane right_form = new VBox(10);
        right_form.setId("right_form");
        HBox.setHgrow(right_form, Priority.ALWAYS);


        TextField matricule_entry = new TextField();
        matricule_entry.setId("matricule_entry");

        TextField nom_entry = new TextField();
        nom_entry.setId("nom_entry");

        TextField prenom_entry = new TextField();
        prenom_entry.setId("prenom_entry");

        DatePicker date_entry = new DatePicker();
        date_entry.setId("date_entry");

        TextField email_entry = new TextField();
        email_entry.setId("email_entry");

        TextField telephone_entry = new TextField();
        telephone_entry.setId("telephone_entry");


        ComboBox<String> parcours_entry = new ComboBox<>();
        parcours_entry.getItems().addAll(data_parcours);
        parcours_entry.setPromptText("Veillez selectionner");
        parcours_entry.setId("parcours_entry");

        ComboBox<String> annee_univ_entry = new ComboBox<>();
        annee_univ_entry.getItems().addAll(
                        "2026 - 2027", "2027 - 2028",
                        "2028 - 2029", "2029 - 2030");

        annee_univ_entry.getSelectionModel().select(0);
        annee_univ_entry.setId("annee_univ_entry");

        ComboBox<String> status_entry = new ComboBox<>();
        status_entry.getItems().addAll("Actif", "Inactif");

        status_entry.getSelectionModel().select(0);
        status_entry.setId("status_entry");

        right_form.getChildren().addAll(
                matricule_entry,
                nom_entry,
                prenom_entry,
                date_entry,
                email_entry,
                telephone_entry,
                parcours_entry,
                annee_univ_entry,
                status_entry
        );


        frames_form.getChildren().addAll(left_form, right_form);



        HBox btn_form = new HBox(20);
        btn_form.setId("btn_form");

        Button btn_add_new = new Button("➕ Add New");
        btn_add_new.setId("btn_add_new");


        Button btn_ajouter = new Button("💾 Ajouter");
        btn_ajouter.setId("btn_ajouter");

        Button btn_modifier = new Button("💾 Enregistrer");
        btn_modifier.setId("btn_ajouter");
        btn_modifier.setVisible(false);
        btn_modifier.setManaged(false);

        btnModifierForm = btn_modifier;
        btnAjouter = btn_ajouter;
        matriculeForm = matricule_entry;
        nomForm = nom_entry;
        prenomForm = prenom_entry;
        dateForm = date_entry;
        emailForm = email_entry;
        telephoneForm = telephone_entry;
        parcoursForm = parcours_entry;
        anneeForm = annee_univ_entry;
        statusForm = status_entry;

        btn_form.getChildren().addAll(btn_add_new, btn_ajouter, btn_modifier);

        // Bouton pour cacher

        Button btn_cacher_form = new Button("‹");
        btn_cacher_form.setId("btn_cacher_form");

        Button btn_show_form = new Button("›");
        btn_show_form.setId("btn_show_form");
        VBox.setVgrow(btn_show_form, Priority.ALWAYS);
        btn_show_form.setVisible(false);
        btn_show_form.setManaged(false);

        btnShowForm = btn_show_form;
        paneForm = pane_formulaire;

        pane_formulaire.getChildren().addAll(btn_cacher_form,lbl_titre_form, frames_form, btn_form);

        // Cacher la page formulaire
        cacherFormulaire(btn_cacher_form/* Action masquer */,pane_formulaire/* Masquer */,btn_show_form/* Afficher */);

        // Reaficher la page formulaire
        showFormulaire(btn_show_form, pane_formulaire, btn_show_form);


        // ========================================================
        // TABLEAU
        // ========================================================


        pane_table = new VBox(5);
        pane_table.setId("pane_table");



        Label lbl_titre_table = new Label("📃 Liste des Etudiants : ");
        lbl_titre_table.setId("lbl_titre_table");


        // --------------------------------------------------------
        // Zone du tableau
        // --------------------------------------------------------



        ScrollPane scroll_table = new ScrollPane();
        VBox.setVgrow(scroll_table, Priority.ALWAYS);
        scroll_table.setId("scroll_table");



        VBox tableContainer = new VBox (0);
        tableContainerRef = tableContainer;
        tableContainerRef.setStyle("-fx-alignment: top-center;-fx-background-color: red;");
        tableContainer.setId("tableContainer");


        VBox.setVgrow(tableContainer, Priority.ALWAYS);
        HBox.setHgrow(tableContainer, Priority.ALWAYS);


        // --------------------------------------------------------
        // Pagination
        // --------------------------------------------------------

        pagination = new HBox(5);
        pagination.setPadding(new Insets(10, 0, 10, 0));
        pagination.setAlignment(Pos.CENTER);
        HBox.setHgrow(pagination, Priority.ALWAYS);
        VBox.setVgrow(tableContainer, Priority.ALWAYS);
        pagination.setId("pagination");

        scroll_table.setContent(tableContainer);
        scroll_table.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        pane_table.getChildren().addAll(lbl_titre_table, scroll_table, pagination);

        matricule_entry.setText(GestionEtudiant.EtudiantDao.prochainMatricule());

        // ============================================================
        // CREER UN NOUVEAU ETUDIANT
        // ============================================================
        btn_add_new.setOnAction(e -> {
            selectedId = -1;
            btn_modifier.setVisible(false);
            btn_modifier.setManaged(false);
            btnAjouter.setVisible(true);
            btnAjouter.setManaged(true);

            matricule_entry.setText(GestionEtudiant.EtudiantDao.prochainMatricule());
            nom_entry.clear();
            prenom_entry.clear();
            date_entry.setValue(null);
            email_entry.clear();
            telephone_entry.clear();
            parcours_entry.getSelectionModel().clearSelection();
            annee_univ_entry.getSelectionModel().selectFirst();
            status_entry.getSelectionModel().selectFirst();
        });

        // ============================================================
        // AJOUTER UN
        // ============================================================
        btn_ajouter.setOnAction(e -> {


            if (matricule_entry.getText().isBlank() || nom_entry.getText().isBlank()) {

                new Alert(Alert.AlertType.WARNING,
                        "Matricule et Nom sont obligatoires.").showAndWait();
                return;
            }

            String date = "";

            if (date_entry.getValue() != null) {

                date = date_entry.getValue().toString();
            }



            Etudiant etudiant =
                    new Etudiant(

                            0,
                            matricule_entry.getText(),
                            nom_entry.getText(),
                            prenom_entry.getText(),
                            date,
                            email_entry.getText(),
                            telephone_entry.getText(),
                            parcours_entry.getValue(),
                            annee_univ_entry.getValue(),
                            status_entry.getValue()
                    );


            if (GestionEtudiant.EtudiantDao.ajouter(etudiant)) {

                new Alert(Alert.AlertType.INFORMATION, "Étudiant enregistré."
                ).showAndWait();

                matricule_entry.setText(GestionEtudiant.EtudiantDao.prochainMatricule());
                selectedId = -1;
                btn_modifier.setVisible(false);
                btn_modifier.setManaged(false);
                nom_entry.clear();
                prenom_entry.clear();
                date_entry.setValue(null);
                email_entry.clear();
                telephone_entry.clear();
                parcours_entry.setValue(null);
                status_entry.getSelectionModel().selectFirst();

                pageActuelle = 1;
                afficherTable(tableContainer);

            } else {

                new Alert(Alert.AlertType.ERROR,
                        "Impossible d'enregistrer l'étudiant."
                ).showAndWait();
            }
        });

        // ============================================================
        // ENREGISTRER lA MODIFICATION D' UN ETUDIANT
        // ============================================================
        btn_modifier.setOnAction(e -> {


            if (matricule_entry.getText().isBlank() || nom_entry.getText().isBlank()) {

                new Alert(Alert.AlertType.WARNING,
                        "Matricule et Nom sont obligatoires.").showAndWait();
                return;
            }

            String date = "";

            if (date_entry.getValue() != null) {

                date = date_entry.getValue().toString();
            }



            Etudiant etudiant =
                    new Etudiant(

                            0,
                            matricule_entry.getText(),
                            nom_entry.getText(),
                            prenom_entry.getText(),
                            date,
                            email_entry.getText(),
                            telephone_entry.getText(),
                            parcours_entry.getValue(),
                            annee_univ_entry.getValue(),
                            status_entry.getValue()
                    );


            if (GestionEtudiant.EtudiantDao.modifier(etudiant, selectedId)) {

                new Alert(Alert.AlertType.INFORMATION, "Étudiant modifié."
                ).showAndWait();

                matricule_entry.setText(GestionEtudiant.EtudiantDao.prochainMatricule());
                selectedId = -1;
                btn_modifier.setVisible(false);
                btn_modifier.setManaged(false);
                btnAjouter.setManaged(true);
                btnAjouter.setVisible(true);
                nom_entry.clear();
                prenom_entry.clear();
                date_entry.setValue(null);
                email_entry.clear();
                telephone_entry.clear();
                parcours_entry.setValue(null);
                status_entry.getSelectionModel().selectFirst();



                pageActuelle = 1;
                afficherTable(tableContainer);

            } else {

                new Alert(Alert.AlertType.ERROR,
                        "Erreur lors de modification !"
                ).showAndWait();
            }
        });

        // ========================================================
        // RECHERCHE
        // ========================================================
        btn_search.setOnAction(e -> {
            List<Etudiant> resultats =
                    GestionEtudiant.EtudiantDao.rechercher(

                            entry_matricule.getText(),
                            entry_nom.getText(),
                            combo_parcours.getValue() == null ? "" : combo_parcours.getValue(),
                            combo_status.getValue() == null ? "" : combo_status.getValue()
                    );

            afficherResultats(tableContainer, resultats);
            pagination.getChildren().clear();
        });

        /*
         ========================================================
         REINITIALISER
         ========================================================
        */

        btn_clear.setOnAction(e -> {

            entry_matricule.clear();
            entry_nom.clear();
            combo_parcours.getSelectionModel().clearSelection();
            combo_status.getSelectionModel().clearSelection();

            selectedId = -1;
            pageActuelle = 1;

            afficherTable(tableContainer);
        });


        btnImporter.setOnAction(event -> {
            if (Files.notExists(Path.of(String.valueOf(lien)))){
                new Alert(Alert.AlertType.ERROR,"Aucune exportation trouvee! ").show();
            } else {
                try {
                    ImportListe(Path.of(lien));
                } catch (IOException ex) {
                    new Alert(Alert.AlertType.ERROR,
                            "Impossible de lire le fichier :\n" + ex.getMessage()
                    ).showAndWait();
                }
            }

            selectedId = -1;
            pageActuelle = 1;

            afficherTable(tableContainer);
        });


        /*
         ========================================================
         BODY
         ========================================================
        */


        addEtuParPage = new ComboBox<>();
        addEtuParPage.getItems().addAll("10","12","15","20");
        addEtuParPage.setPrefWidth(60);
        addEtuParPage.getSelectionModel().selectFirst();

        body.getChildren().addAll(btn_show_form, pane_formulaire, pane_table);




        // ========================================================
        // ROOT ETUDIANT
        // ========================================================

        layoutEtudiant.getChildren().addAll(header, body);


        // ========================================================
        // MENU
        // ========================================================

        menu = new HBox();
        menu.setId("menu");

        showHeader();

        root.getChildren().addAll(menu,layoutEtudiant);



        Scene scene = new Scene(root, 1000, 650);

        Style.appliquer(scene);

        stage.setTitle("Gestion Étudiants");
        stage.setScene(scene);
        stage.setMinWidth(1000);
        stage.setMinHeight(650);
        stage.initStyle(StageStyle.UNDECORATED);

        stage.show();


        // Afficher au demarrage
        afficherTable(tableContainer);
    }

    // ============================================================
    // AFFICHER UNE PAGE
    // ============================================================
    private void afficherTable(VBox container) {

        int total = GestionEtudiant.EtudiantDao.compter();

        totalPages = Math.max(1, (total + ETUDIANTS_PAR_PAGE - 1) / ETUDIANTS_PAR_PAGE);


        if (pageActuelle > totalPages) {
            pageActuelle = totalPages;
        }

        List<Etudiant> etudiants = GestionEtudiant.EtudiantDao.getPage(pageActuelle, ETUDIANTS_PAR_PAGE);

        afficherResultats(container, etudiants);

        creerPagination(container,pagination);
    }

    // ============================================================
    // AFFICHER LES ETUDIANTS
    // ============================================================
    private void afficherResultats(VBox container, List<Etudiant> etudiants) {

        container.getChildren().clear();
        container.setAlignment(Pos.TOP_CENTER);

        container.setStyle("-fx-background-color: #142541;");

        // --------------------------------------------------------
        // TEXT DE l' HEADER
        // --------------------------------------------------------

        HBox table_header = new HBox();
        table_header.setId("table_header");

        table_header.getChildren().addAll(

                colonne("",0),
                colonne("Matricule", 90),
                colonne("Nom", 150),
                colonne("Prénom", 130),
                colonne("Date naissace", 100),
                colonne("Email", 180),
                colonne("Parcours", 120),
                colonne("Annee Universite", 120),
                colonne("Status", 60)
        );


        container.getChildren().add(table_header);


        // --------------------------------------------------------
        // TEXTES DES LIGNES
        // --------------------------------------------------------
        for (Etudiant e : etudiants) {

            HBox row = new HBox();
            row.setId("table_row");

            row.getChildren().addAll(
                    // Rq: 'e.getId()' = Recuperation de l' Id d'un etudiant
                    colonneList(e.getMatricule(), 90, e.getId()),
                    colonneList(e.getNom(), 150, e.getId()),
                    colonneList(e.getPrenom(), 130, e.getId()),
                    colonneList(e.getDateNaissance(),100, e.getId()),
                    colonneList(e.getEmail(), 180, e.getId()),
                    colonneList(e.getParcours(), 120, e.getId()),
                    colonneList(e.getAnneeUniversitaire(),120, e.getId()),
                    colonneList(e.getStatus(), 60, e.getId())
            );

            container.getChildren().add(row);

        }
    }

    // ============================================================
    // HEADER DE LA LISTE
    // ============================================================
    private Label colonne(String texte, double largeur) {

        Label label = new Label(texte == null ? "" : texte);


        label.setMaxWidth(largeur);
        label.setMinWidth(largeur);

        label.setId("colonne");
        label.setPadding(new Insets(6, 8, 6, 8));


        return label;
    }

    // ============================================================
    // LABEL contenant la listes des etudiants
    // ============================================================
    private Label colonneList(String texte, double largeur, int labelId) {

        Label label = new Label(texte == null ? "" : texte);
        label.setStyle("-fx-border-width: 1px;-fx-border-color: #172c4e;" +
                "-fx-text-fill: #ffffff;-fx-alignment: center-left;");

        if ("Actif".equals(label.getText())) {
            label.setStyle("-fx-text-fill: #000;" +
                    "-fx-background-color: #90da7a;" +
                    "-fx-border-width: 1px;" +
                    "-fx-border-color: #172c4e;");
            label.setOnMouseEntered(e -> {
                label.setStyle(
                        "-fx-background-color: #727272;" +
                        "-fx-text-fill: #ffffff;" +
                        "-fx-cursor: hand;"

                );
            });

            label.setOnMouseExited(e -> {
                label.setStyle(
                        "-fx-text-fill: #000000;" +
                        "-fx-background-color: #90da7a;" +
                        "-fx-border-width: 1px;" +
                        "-fx-border-color: #172c4e;"
                );
            });
        }

        if ("Inactif".equals(label.getText())) {
            label.setStyle("-fx-text-fill: #fff;" +
                    "-fx-background-color: orangered;" +
                    "-fx-border-width: 1px;" +
                    "-fx-border-color: #172c4e;");
            label.setOnMouseEntered(e -> {
                label.setStyle(
                        "-fx-background-color: #727272;" +
                                "-fx-text-fill: #ffffff;" +
                                "-fx-cursor: hand;"

                );
            });

            label.setOnMouseExited(e -> {
                label.setStyle(
                        "-fx-text-fill: #000000;" +
                                "-fx-background-color: orangered;" +
                                "-fx-border-width: 1px;" +
                                "-fx-border-color: #172c4e;"
                );
            });
        }

        if ((!Objects.equals(label.getText(), "Actif")) && (!Objects.equals(label.getText(), "Inactif"))) {


            label.setOnMouseEntered(e -> {
                label.setStyle(
                        "-fx-background-color: #727272;" +
                                "-fx-text-fill: #ffffff;" +
                                "-fx-cursor: hand;-fx-alignment: center-left;"

                );
            });

            label.setOnMouseExited(e -> {
                label.setStyle(
                        "-fx-text-fill: #ffffff;" +
                                "-fx-border-width: 1px;" +
                                "-fx-border-color: #172c4e;-fx-alignment: center-left;"
                );
            });

        }

        label.setMaxWidth(largeur);
        label.setMinWidth(largeur);

        label.setId(String.valueOf(labelId));
        label.setPadding(new Insets(6, 8, 6, 8));

            // List clicked
        label.setOnMouseClicked(event -> {
                selectedId = Integer.parseInt(label.getId());
                creerToplevelUpdateDelete(icon, this::modifierEtudiantSelectionne, this::supprimerEtudiantSelectionne,
                        label.getText());
        });




        return label;
    }

    // ============================================================
    // PAGINATION DE TABLEAU
    // ============================================================
    private void creerPagination(VBox container, HBox pagination) {

        pagination.getChildren().clear();
        pagination.setPadding(new  Insets(5, 5, 5, 5));

        // --------------------------------------------------------
        // PREVIOUS
        // --------------------------------------------------------

        Label total_etudiant = new Label("  Total: "+ GestionEtudiant.EtudiantDao.compter() +" etudiant(s)");
        HBox.setHgrow(total_etudiant, Priority.ALWAYS);
        total_etudiant.setStyle("-fx-text-fill: #ffffff;");

        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);


        Button previous = new Button("‹");
        // Desactiver si une page seullement
        previous.setDisable(pageActuelle == 1);
        previous.setOnAction(e -> {

            if (pageActuelle > 1) {

                pageActuelle--;

                afficherTable(container);
            }
        });



        pagination.getChildren().addAll(total_etudiant, spacer, previous);

        // --------------------------------------------------------
        // NUMEROS
        // --------------------------------------------------------

        for (int i = 1; i <= totalPages; i++) {

            final int page = i;

            Button button = new Button(String.valueOf(i));
            button.setDisable(page == pageActuelle);

            button.setOnAction(e -> {

                pageActuelle = page;
                afficherTable(container);
            });

            pagination.getChildren().add(button);
        }


        // --------------------------------------------------------
        // NEXT
        // --------------------------------------------------------

        Button next = new Button("›");
        next.setDisable(pageActuelle == totalPages);

        next.setOnAction(e -> {

            if (pageActuelle < totalPages) {

                pageActuelle++;
                afficherTable(container);
            }
        });

        Pane spacer_Un = new Pane();
        HBox.setHgrow(spacer_Un, Priority.ALWAYS);

        int total = GestionEtudiant.EtudiantDao.compter();
        int debut = total == 0 ? 0 : (pageActuelle - 1) * ETUDIANTS_PAR_PAGE + 1;
        int fin = Math.min(pageActuelle * ETUDIANTS_PAR_PAGE, total);
        Label nombre_page = new Label(
                debut + " - " + fin + " sur " + total
        );
        nombre_page.setStyle("-fx-text-fill: #ffffff;");


        addEtuParPage.setOnAction(e -> {

            ETUDIANTS_PAR_PAGE = Integer.parseInt(addEtuParPage.getValue());
            afficherTable(container);
            addEtuParPage.getSelectionModel().select(addEtuParPage.getValue());

        });

        pagination.getChildren().addAll(next, spacer_Un,nombre_page,addEtuParPage);
    }

    // ============================================================
    // MODIFICATION DE SELECTION
    // ============================================================
    private void modifierEtudiantSelectionne() {
        if (selectedId <= 0) {
            return;
        }

        Etudiant etudiant = GestionEtudiant.EtudiantDao.getById(selectedId);
        if (etudiant == null) {
            new Alert(Alert.AlertType.ERROR, "L'étudiant sélectionné n'existe plus.").showAndWait();
            ToplevelUpdateDelete.fermer();
            afficherTable(tableContainerRef);
            return;
        }


        // aficher la page formulaire
        paneForm.setManaged(true);
        paneForm.setVisible(true);

        btnShowForm.setVisible(false);
        btnShowForm.setManaged(false);

        remplirChamps(matriculeForm, nomForm, prenomForm, dateForm, emailForm,
                telephoneForm, parcoursForm, anneeForm, statusForm, etudiant,
                btnAjouter
        );
        btnModifierForm.setManaged(true);
        btnModifierForm.setVisible(true);

        ToplevelUpdateDelete.fermer();
    }

    // ============================================================
    // SUPRESION DE SELECTION
    // ============================================================
    private void supprimerEtudiantSelectionne() {
        if (selectedId <= 0) {
            return;
        }

        Etudiant etudiant = GestionEtudiant.EtudiantDao.getById(selectedId);
        if (etudiant == null) {
            ToplevelUpdateDelete.fermer();
            afficherTable(tableContainerRef);
            return;
        }

        Alert confirmation = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Supprimer l'étudiant « " + etudiant.getNom() + " " + etudiant.getPrenom() + " » ?",
                ButtonType.YES, ButtonType.NO
        );
        confirmation.setHeaderText("Confirmation de suppression");

        confirmation.showAndWait().ifPresent(button -> {
            if (button == ButtonType.YES && GestionEtudiant.EtudiantDao.supprimer(selectedId)) {
                selectedId = -1;
                btnModifierForm.setVisible(false);
                btnModifierForm.setManaged(false);
                ToplevelUpdateDelete.fermer();
                afficherTable(tableContainerRef);
            }
        });
    }

} // END