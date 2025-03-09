package com.example.wiggelsconcert.GUI;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginScreen {
    public static void showLoginScreen(Stage primaryStage) {
        Stage loginStage = new Stage();
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));
        HBox buttonHBox = new HBox(145);

        Label userLabel = new Label("Användarnamn:");
        TextField userField = new TextField();
        Label passLabel = new Label("Lösenord:");
        PasswordField passField = new PasswordField();
        Button loginButton = new Button("Logga in");
        Button closeButton = new Button("Avbryt");

        loginButton.setOnAction(e -> {
            String username = userField.getText();
            String password = passField.getText();
            if (true/*TODO: database connection logic*/) {
                loginStage.close();
                MainMenuScreen.showMainMenu(primaryStage);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Felaktigt användarnamn eller lösenord!");
                alert.show();
            }
        });

        closeButton.setOnAction(e -> loginStage.close());

        buttonHBox.getChildren().addAll(loginButton, closeButton);
        vbox.getChildren().addAll(userLabel, userField, passLabel, passField, buttonHBox);
        Scene scene = new Scene(vbox, 300, 200);
        loginStage.setScene(scene);
        loginStage.setTitle("Databas inloggning");
        loginStage.show();
    }
}
