package com.example.wiggelsconcert;

import javafx.application.Application;
import javafx.stage.Stage;

import com.example.wiggelsconcert.GUI.LoginScreen;

public class Main extends Application {
    LoginScreen loginScreen;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Wigells Concert System");
        loginScreen = new LoginScreen();
        loginScreen.showLoginScreen();
    }
}