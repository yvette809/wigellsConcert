package com.example.wiggelsconcert.GUI;

import com.example.wiggelsconcert.Entities.*;
import com.example.wiggelsconcert.DAO.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.stream.Collectors;

public class BuyTicketScreen {
    private static final CustomerDAO customerDAO = new CustomerDAO();
    private static final ConcertDAO concertDAO = new ConcertDAO();
    private static final WcDAO wcDAO = new WcDAO();

    public static void showBuyTicketScreen() {
        Stage stage = new Stage();
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        Label label = new Label("Välj kund och konsert");
        TextField customerSearch = new TextField();
        customerSearch.setPromptText("Sök kund...");
        ListView<Customer> customersList = new ListView<>();
        ObservableList<Customer> customerData = FXCollections.observableArrayList(customerDAO.getAllCustomers());
        TextField concertSearch = new TextField();
        concertSearch.setPromptText("Sök konsert...");
        ListView<Concert> concertsList = new ListView<>();
        ObservableList<Concert> concertData = FXCollections.observableArrayList(concertDAO.getAllConcerts());

        Button confirmButton = new Button("Bekräfta köp");

        customersList.setItems(customerData);
        concertsList.setItems(concertData);

        customerSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            customersList.setItems(FXCollections.observableArrayList(
                    customerData.stream()
                            .filter(k -> k.getFirst_name().toLowerCase().contains(newValue.toLowerCase()) ||
                                    k.getLast_name().toLowerCase().contains(newValue.toLowerCase()))
                            .collect(Collectors.toList())
            ));
        });

        concertSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            concertsList.setItems(FXCollections.observableArrayList(
                    concertData.stream()
                            .filter(c -> c.getArtist().toLowerCase().contains(newValue.toLowerCase()))
                            .collect(Collectors.toList())
            ));
        });

        confirmButton.setOnAction(e -> {
            Customer selectedCustomer = customersList.getSelectionModel().getSelectedItem();
            Concert selectedConcert = concertsList.getSelectionModel().getSelectedItem();
            if (selectedCustomer != null && selectedConcert != null) {
                buyTicket(selectedCustomer, selectedConcert);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Välj en kund och en konsert!");
                alert.show();
            }
        });

        vbox.getChildren().addAll(label, customerSearch, customersList, concertSearch, concertsList, confirmButton);
        Scene scene = new Scene(vbox, 400, 400);
        stage.setScene(scene);
        stage.setTitle("Köp biljett");
        stage.show();
    }

    private static void buyTicket(Customer customer, Concert concert) {
        WC wcEntry = new WC("Biljettköp", customer, concert); // Is this the correct use for the name field?
        wcDAO.saveWc(wcEntry);

        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Biljett köpt!");
        alert.show();
    }
}
