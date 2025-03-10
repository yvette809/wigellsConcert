package com.example.wiggelsconcert;

import javafx.application.Application;
import javafx.stage.Stage;

import com.example.wiggelsconcert.GUI.LoginScreen;
import org.hibernate.SessionFactory;

import org.hibernate.cfg.Configuration;
import org.hibernate.Session;

public class Main extends Application {
    public static SessionFactory sessionFactory;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Wigells Concert System");
        LoginScreen.showLoginScreen(primaryStage);
    }

    public static boolean initializeDatabase(String username, String password) {
        try {
            Configuration configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml");

            // Replace placeholders in hibernate.cfg.xml
            configuration.setProperty("hibernate.connection.username", username);
            configuration.setProperty("hibernate.connection.password", password);

            sessionFactory = configuration.buildSessionFactory();
            Session session = sessionFactory.openSession();

            session.close();
            sessionFactory.close();

            return true;
        } catch (Exception e) {
            System.err.println("Database initialization failed: " + e.getMessage());
            e.printStackTrace(); // For debugging
            return false;
        }
    }
}