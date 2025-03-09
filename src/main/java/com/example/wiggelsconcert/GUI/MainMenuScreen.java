package com.example.wiggelsconcert.GUI;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainMenuScreen {
    public static void showMainMenu(Stage primaryStage) {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));

        Label label = new Label("Huvudmeny");
        Button buyTicketButton = new Button("Köp biljett");
        Button manageCustomersButton = new Button("Hantera kunder");
        Button manageAddressesButton = new Button("Hantera adresser");
        Button manageConcertsButton = new Button("Hantera konserter");
        Button manageArenasButton = new Button("Hantera arenor");

        buyTicketButton.setOnAction(e -> BuyTicketScreen.showBuyTicketScreen());
//        manageCustomersButton.setOnAction(e -> showManagementScreen("Hantera kunder", Kund.class)); // We need to know how the hibernate classes will look
//        manageAddressesButton.setOnAction(e -> showManagementScreen("Hantera adresser", Adress.class));
//        manageConcertsButton.setOnAction(e -> showManagementScreen("Hantera konserter", Konsert.class));
//        manageArenasButton.setOnAction(e -> showManagementScreen("Hantera arenor", Arena.class));

        vbox.getChildren().addAll(label, buyTicketButton, manageCustomersButton, manageAddressesButton, manageConcertsButton, manageArenasButton);
        Scene scene = new Scene(vbox, 300, 250);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
