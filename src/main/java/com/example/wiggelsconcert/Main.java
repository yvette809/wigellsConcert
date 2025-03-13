package com.example.wiggelsconcert;

import com.example.wiggelsconcert.utils.HibernateUtil;
import javafx.application.Application;
import javafx.stage.Stage;

import com.example.wiggelsconcert.GUI.LoginScreen;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class Main extends Application {

    public static void main(String[] args) {
        launch();

    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Wigells Concert System");
        LoginScreen.showLoginScreen(primaryStage);
    }


}