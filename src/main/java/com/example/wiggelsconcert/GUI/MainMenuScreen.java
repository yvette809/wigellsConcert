package com.example.wiggelsconcert.GUI;

import com.example.wiggelsconcert.Entities.*;
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
        Button manageWCButton = new Button("Hantera WC");

        buyTicketButton.setOnAction(e -> BuyTicketScreen.showBuyTicketScreen());
        manageCustomersButton.setOnAction(e -> ManagementScreen.showManagementScreen("Hantera kunder", Customer.class));
        manageAddressesButton.setOnAction(e -> ManagementScreen.showManagementScreen("Hantera adresser", Address.class));
        manageConcertsButton.setOnAction(e -> ManagementScreen.showManagementScreen("Hantera konserter", Concert.class));
        manageArenasButton.setOnAction(e -> ManagementScreen.showManagementScreen("Hantera arenor", Arena.class));
        manageWCButton.setOnAction(e -> ManagementScreen.showManagementScreen("Hantera WC", WC.class));

        vbox.getChildren().addAll(label, buyTicketButton, manageCustomersButton, manageAddressesButton, manageConcertsButton, manageArenasButton, manageWCButton);
        Scene scene = new Scene(vbox, 300, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
