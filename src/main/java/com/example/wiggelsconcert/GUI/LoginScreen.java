package com.example.wiggelsconcert.GUI;

import com.example.wiggelsconcert.Main;
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
        HBox buttonHBox = new HBox(195);

        Label infoLabel = new Label("Skriv in inloggningsuppgifter till en MySQL databas.");
        Label userLabel = new Label("Användarnamn:");
        TextField userField = new TextField();
        Label passLabel = new Label("Lösenord:");
        PasswordField passField = new PasswordField();
        Label ipLabel = new Label("IP-adress:");
        TextField ipField = new TextField("localhost");
        Label portLabel = new Label("Port:");
        TextField portField = new TextField("3306");
        Button loginButton = new Button("Logga in");
        Button closeButton = new Button("Avbryt");

        loginButton.setOnAction(e -> {
            String username = userField.getText();
            String password = passField.getText();
            String ip = ipField.getText();
            String port = portField.getText();
            if (Main.initializeDatabase(username, password, ip, port)) {
                loginStage.close();
                MainMenuScreen.showMainMenu(primaryStage);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Felaktiga inloggningsuppgifter!");
                alert.show();
            }
        });

        closeButton.setOnAction(e -> loginStage.close());

        buttonHBox.getChildren().addAll(loginButton, closeButton);
        vbox.getChildren().addAll(infoLabel, userLabel, userField, passLabel, passField, ipLabel, ipField, portLabel, portField, buttonHBox);
        Scene scene = new Scene(vbox, 350, 350);
        loginStage.setScene(scene);
        loginStage.setTitle("Databas inloggning");
        loginStage.show();
    }
}
