package com.example.wiggelsconcert;

import javafx.application.Application;
import javafx.stage.Stage;

import com.example.wiggelsconcert.GUI.LoginScreen;

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