package com.example.wiggelsconcert;

import com.example.wiggelsconcert.utils.HibernateUtil;
import com.example.wiggelsconcert.utils.MockDataUtil;
import javafx.application.Application;
import javafx.stage.Stage;

import com.example.wiggelsconcert.GUI.LoginScreen;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class Main extends Application {

    public static void main(String[] args) {

        String username = System.getenv("DB_USERNAME");
        String password = System.getenv("DB_PASSWORD");

        if (username == null || password == null) {
            username = "root";
            password = "root";
            System.out.println("Using default values for testing");
        }

        boolean dbInitialized = HibernateUtil.initializeDatabase(username, password, "127.0.0.1", "3306");

        if (!dbInitialized) {
            System.err.println("Failed to initialize Hibernate. Exiting...");
            return;
        }
        launch();  // Start JavaFX

    }

    @Override
    public void start(Stage primaryStage) {
        insertMockData();
        primaryStage.setTitle("Wigells Concert System");
        LoginScreen.showLoginScreen(primaryStage);

    }
    private static void insertMockData() {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        try {
            MockDataUtil.insertMockData(session);
            transaction.commit();
            System.out.println("✅ Mock data inserted successfully!");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error inserting mock data: " + e.getMessage());
        } finally {
            session.close();
        }
    }

}