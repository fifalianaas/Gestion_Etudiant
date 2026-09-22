package com.ge.java.model;

import com.ge.java.database.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GestionEtudiant {

    public static List<String> nouvelleListes = new ArrayList<>();


    // Accès aux données des étudiants.
    public static final class EtudiantDao {

        private EtudiantDao() {
        }

        public static boolean existeMatricule(String matricule) {
            if (matricule == null || matricule.trim().isEmpty()) {
                return false;
            }

            String sql = "SELECT 1 FROM etudiants WHERE matricule = ? LIMIT 1";
            try (Connection connection = Database.connect();
                 PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, matricule.trim());
                try (ResultSet result = statement.executeQuery()) {
                    return result.next();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                return false;
            }
        }

        public static boolean ajouter(Etudiant e) {
            String sql = """
                    INSERT INTO etudiants (
                        matricule, nom, prenom, date_naissance, email,
                        telephone, parcours, annee_universitaire, status
                    ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                    """;

            try (Connection connection = Database.connect();
                 PreparedStatement statement = connection.prepareStatement(sql)) {

                remplirStatement(statement, e);
                return statement.executeUpdate() == 1;

            } catch (SQLException ex) {
                ex.printStackTrace();
                return false;
            }
        }

        /** Modifie un étudiant à partir de sa clé primaire id. */
        public static boolean modifier(Etudiant e, int id) {
            if (id <= 0) {
                return false;
            }

            String sql = """
                    UPDATE etudiants SET
                        matricule = ?,
                        nom = ?,
                        prenom = ?,
                        date_naissance = ?,
                        email = ?,
                        telephone = ?,
                        parcours = ?,
                        annee_universitaire = ?,
                        status = ?
                    WHERE id = ?
                    """;

            try (Connection connection = Database.connect();
                 PreparedStatement statement = connection.prepareStatement(sql)) {

                remplirStatement(statement, e);
                statement.setInt(10, id);
                return statement.executeUpdate() == 1;

            } catch (SQLException ex) {
                ex.printStackTrace();
                return false;
            }
        }

        public static boolean supprimer(int id) {
            if (id <= 0) {
                return false;
            }

            String sql = "DELETE FROM etudiants WHERE id = ?";

            try (Connection connection = Database.connect();
                 PreparedStatement statement = connection.prepareStatement(sql)) {

                statement.setInt(1, id);
                return statement.executeUpdate() == 1;

            } catch (SQLException ex) {
                ex.printStackTrace();
                return false;
            }
        }

        public static Etudiant getById(int id) {
            if (id <= 0) {
                return null;
            }

            String sql = """
                    SELECT id, matricule, nom, prenom, date_naissance, email,
                           telephone, parcours, annee_universitaire, status
                    FROM etudiants
                    WHERE id = ?
                    """;

            try (Connection connection = Database.connect();
                 PreparedStatement statement = connection.prepareStatement(sql)) {

                statement.setInt(1, id);

                try (ResultSet result = statement.executeQuery()) {
                    return result.next() ? lireEtudiant(result) : null;
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
                return null;
            }
        }


        public static String prochainMatricule() {
            String prefix = "ETU" + java.time.Year.now().getValue() + "-";
            String sql = "SELECT matricule FROM etudiants WHERE matricule LIKE ?";
            int max = 0;

            try (Connection connection = Database.connect();
                 PreparedStatement statement = connection.prepareStatement(sql)) {

                statement.setString(1, prefix + "%");
                try (ResultSet result = statement.executeQuery()) {
                    while (result.next()) {
                        String matricule = result.getString(1);
                        if (matricule != null && matricule.startsWith(prefix)) {
                            try {
                                max = Math.max(max, Integer.parseInt(matricule.substring(prefix.length())));
                            } catch (NumberFormatException ignored) {
                                // Ignore les anciens matricules qui ne suivent pas le format attendu.
                            }
                        }
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            return prefix + (max + 1);
        }

        public static int compter() {
            String sql = "SELECT COUNT(*) FROM etudiants";

            try (Connection connection = Database.connect();
                 PreparedStatement statement = connection.prepareStatement(sql);
                 ResultSet result = statement.executeQuery()) {

                return result.next() ? result.getInt(1) : 0;

            } catch (SQLException ex) {
                ex.printStackTrace();
                return 0;
            }
        }

        public static List<Etudiant> getPage(int page, int parPage) {
            List<Etudiant> liste = new ArrayList<>();

            if (page < 1 || parPage < 1) {
                return liste;
            }

            int offset = (page - 1) * parPage;

            String sql = """
                    SELECT id, matricule, nom, prenom, date_naissance, email,
                           telephone, parcours, annee_universitaire, status
                    FROM etudiants
                    ORDER BY id DESC
                    LIMIT ? OFFSET ?
                    """;

            try (Connection connection = Database.connect();
                 PreparedStatement statement = connection.prepareStatement(sql)) {

                statement.setInt(1, parPage);
                statement.setInt(2, offset);

                try (ResultSet result = statement.executeQuery()) {
                    while (result.next()) {
                        liste.add(lireEtudiant(result));
                    }
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            return liste;
        }


        public static List<String> getAllForListSaved() {

            String sql = """
                    SELECT *
                    FROM etudiants
                    """;

            try (Connection connection = Database.connect();
                 PreparedStatement statement = connection.prepareStatement(sql)) {

                try (ResultSet result = statement.executeQuery()) {
                    while (result.next()) {
                        nouvelleListes.add(
                                result.getInt("id") + "|" +
                                result.getString("matricule") + "|" +
                                result.getString("nom") + "|" +
                                result.getString("prenom") + "|" +
                                result.getString("date_naissance") + "|" +
                                result.getString("email") + "|" +
                                result.getString("telephone") + "|" +
                                result.getString("parcours") + "|" +
                                result.getString("annee_universitaire") + "|" +
                                result.getString("status")
                        );

                    }
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            return nouvelleListes;
        }

        public static List<Etudiant> rechercher(
                String matricule,
                String nom,
                String parcours,
                String status) {

            List<Etudiant> liste = new ArrayList<>();

            String sql = """
                    SELECT id, matricule, nom, prenom, date_naissance, email,
                           telephone, parcours, annee_universitaire, status
                    FROM etudiants
                    WHERE matricule LIKE ?
                      AND nom LIKE ?
                      AND parcours LIKE ?
                      AND status LIKE ?
                    ORDER BY id DESC
                    """;

            try (Connection connection = Database.connect();
                 PreparedStatement statement = connection.prepareStatement(sql)) {

                statement.setString(1, "%" + valeur(matricule) + "%");
                statement.setString(2, "%" + valeur(nom) + "%");
                statement.setString(3, "%" + valeur(parcours) + "%");
                statement.setString(4, "%" + valeur(status) + "%");

                try (ResultSet result = statement.executeQuery()) {
                    while (result.next()) {
                        liste.add(lireEtudiant(result));
                    }
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            return liste;
        }

        private static void remplirStatement(PreparedStatement statement, Etudiant e)
                throws SQLException {
            statement.setString(1, valeur(e.getMatricule()));
            statement.setString(2, valeur(e.getNom()));
            statement.setString(3, valeur(e.getPrenom()));
            statement.setString(4, valeur(e.getDateNaissance()));
            statement.setString(5, valeur(e.getEmail()));
            statement.setString(6, valeur(e.getTelephone()));
            statement.setString(7, valeur(e.getParcours()));
            statement.setString(8, valeur(e.getAnneeUniversitaire()));
            statement.setString(9, valeur(e.getStatus()));
        }

        private static Etudiant lireEtudiant(ResultSet result) throws SQLException {
            return new Etudiant(
                    result.getInt("id"),
                    result.getString("matricule"),
                    result.getString("nom"),
                    result.getString("prenom"),
                    result.getString("date_naissance"),
                    result.getString("email"),
                    result.getString("telephone"),
                    result.getString("parcours"),
                    result.getString("annee_universitaire"),
                    result.getString("status")
            );
        }

        private static String valeur(String value) {
            return value == null ? "" : value.trim();
        }
    }
}
