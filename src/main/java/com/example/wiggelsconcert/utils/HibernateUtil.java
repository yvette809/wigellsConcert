package com.example.wiggelsconcert.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static SessionFactory sessionFactory;

    public static synchronized boolean initializeDatabase(String username, String password, String ip, String port) {
        if (sessionFactory != null) {
            System.out.println("SessionFactory is already initialized.");
            return true;
        }

        try {
            Configuration configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml");

            String dbUrl = "jdbc:mysql://" + ip + ":" + port + "/wigellsconcert?createDatabaseIfNotExist=true";

            // Replace placeholders in hibernate.cfg.xml
            configuration.setProperty("hibernate.connection.url", dbUrl);
            configuration.setProperty("hibernate.connection.username", username);
            configuration.setProperty("hibernate.connection.password", password);

            sessionFactory = configuration.buildSessionFactory();
            insertMockData();
            sessionFactory.openSession().close();

            return true;
        } catch (Exception e) {
            System.err.println("Database initialization failed: " + e.getMessage());
            e.printStackTrace(); // For debugging
            return false;
        }
    }

    public static synchronized SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static synchronized void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
            sessionFactory = null;
        }
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


