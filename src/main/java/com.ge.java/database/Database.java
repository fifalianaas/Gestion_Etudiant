package com.ge.java.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public final class Database {

    private static final String URL = "jdbc:sqlite:etudiants.db";

    private Database() {
    }

    public static Connection connect() throws SQLException {
        Connection connection = DriverManager.getConnection(URL);

        try (Statement statement = connection.createStatement()) {
            statement.execute("PRAGMA foreign_keys = ON");
        }

        return connection;
    }

    public static void initDatabase() {

        String sqlEtudiants = """
                CREATE TABLE IF NOT EXISTS etudiants (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    matricule TEXT NOT NULL UNIQUE,
                    nom TEXT NOT NULL,
                    prenom TEXT,
                    date_naissance TEXT,
                    email TEXT,
                    telephone TEXT,
                    parcours TEXT,
                    annee_universitaire TEXT,
                    status TEXT
                )
                """;


        try (Connection connection = connect();
             Statement statement = connection.createStatement()) {

            // Création de la table étudiants
            statement.executeUpdate(sqlEtudiants);


        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Impossible d'initialiser la base de données.",
                    e
            );
        }
    }
}