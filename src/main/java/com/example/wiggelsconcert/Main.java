package com.example.wiggelsconcert;

import com.example.wiggelsconcert.Entities.Customer;
import javafx.application.Application;
import javafx.stage.Stage;

import com.example.wiggelsconcert.GUI.LoginScreen;
import org.hibernate.SessionFactory;
import java.io.IOException;
import java.util.Scanner;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class Main extends Application {
    public static SessionFactory sessionFactory;

    public static void main(String[] args) {
        login();
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Wigells Concert System");
        LoginScreen.showLoginScreen(primaryStage);
    private static void login() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Localhost: 3306 ");

        System.out.print("Ange ditt användarnamn: ");
        String username = scanner.nextLine();

        System.out.print("Ange ditt lösenord: ");
        String password = scanner.nextLine();

        initializeDatabase(username, password);
    }

    public static void initializeDatabase(String username, String password) {
        try {
            Configuration configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml");

            // Replace placeholders in hibernate.cfg.xml
            configuration.setProperty("hibernate.connection.username", username);
            configuration.setProperty("hibernate.connection.password", password);

            SessionFactory sessionfactory = configuration.buildSessionFactory();
            Session session = sessionfactory.openSession();

            session.close();
            sessionfactory.close();

            System.out.println("Database initialization complete.");

        } catch (Exception e) {
            System.err.println("Database initialization failed: " + e.getMessage());
            e.printStackTrace(); // For debugging
        }

    }
}