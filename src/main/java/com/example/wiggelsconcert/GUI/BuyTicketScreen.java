package com.example.wiggelsconcert.GUI;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.hibernate.Session;

import static com.example.wiggelsconcert.Main.sessionFactory;

public class BuyTicketScreen {
    public static void showBuyTicketScreen() {
        Stage stage = new Stage();
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        Label label = new Label("Välj kund och konsert");
        ListView<Kund> customersList = new ListView<>();
        ListView<Konsert> concertsList = new ListView<>();
        Button confirmButton = new Button("Bekräfta köp");

        Session session = sessionFactory.openSession();
        customersList.getItems().addAll(session.createQuery("FROM Kund", Kund.class).list());
        concertsList.getItems().addAll(session.createQuery("FROM Konsert", Konsert.class).list());
        session.close();

        confirmButton.setOnAction(e -> {
            Kund selectedCustomer = customersList.getSelectionModel().getSelectedItem();
            Konsert selectedConcert = concertsList.getSelectionModel().getSelectedItem();
            if (selectedCustomer != null && selectedConcert != null) {
                buyTicket(selectedCustomer, selectedConcert);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Välj en kund och en konsert!");
                alert.show();
            }
        });

        vbox.getChildren().addAll(label, customersList, concertsList, confirmButton);
        Scene scene = new Scene(vbox, 400, 400);
        stage.setScene(scene);
        stage.setTitle("Köp biljett");
        stage.show();
    }

    private static void buyTicket(Kund customer, Konsert concert) {
        // TODO: Implementation needed
    }
}
