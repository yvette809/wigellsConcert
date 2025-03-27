package com.example.wiggelsconcert;

import com.example.wiggelsconcert.GUI.MainMenuScreen;
import javafx.application.Application;
import javafx.stage.Stage;

import com.example.wiggelsconcert.GUI.LoginScreen;

public class Main extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        MainMenuScreen mainMenuScreen = new MainMenuScreen();
        LoginScreen loginScreen = new LoginScreen(mainMenuScreen);

        primaryStage.setTitle("Wigells Concert System");
        loginScreen.showLoginScreen(primaryStage);
    }
}