package com.example.wiggelsconcert.GUI;

import com.example.wiggelsconcert.DAO.ConcertDAO;
import com.example.wiggelsconcert.Entities.*;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.beans.property.SimpleStringProperty;
import javafx.stage.Stage;

import java.util.List;

public class MainMenuScreen {
    private static final ConcertDAO concertDAO = new ConcertDAO();

    public static void showMainMenu(Stage primaryStage) {
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        // Customer tab
        Tab customerTab = new Tab("Kund");
        VBox customerVbox = new VBox(10);
        customerVbox.setPadding(new Insets(20));
        Label label = new Label("Tillgängliga konserter");
        TableView<Concert> concertTable = new TableView<>();
        TextField searchField = new TextField();
        searchField.setPromptText("Sök konsert...");
        Button buyTicketButton = new Button("Köp biljett");

        TableColumn<Concert, String> artistColumn = new TableColumn<>("Artist");
        artistColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getArtist()));
        TableColumn<Concert, String> dateColumn = new TableColumn<>("Datum");
        dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDate().toString()));
        TableColumn<Concert, Double> priceColumn = new TableColumn<>("Pris");
        priceColumn.setCellValueFactory(cellData -> new SimpleObjectProperty(String.format("%.2f kr", cellData.getValue().getTicket_price())));
        TableColumn<Concert, String> arenaColumn = new TableColumn<>("Arena");
        arenaColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getArena().getName()));
        TableColumn<Concert, String> arenaTypeColumn = new TableColumn<>("Typ");
        arenaTypeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getArena().getType()));
        TableColumn<Concert, Integer> ageLimitColumn = new TableColumn<>("Åldersgräns");
        ageLimitColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getAge_limit()));
        concertTable.getColumns().addAll(artistColumn, dateColumn, priceColumn, arenaColumn, arenaTypeColumn, ageLimitColumn);

        ObservableList<Concert> concerts = FXCollections.observableArrayList(concertDAO.getAllConcerts());
        concertTable.setItems(concerts);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            List<Concert> filteredList = concerts.stream()
                    .filter(c -> c.getArtist().toLowerCase().contains(newValue.toLowerCase()) ||
                            c.getArena().getName().toLowerCase().contains(newValue.toLowerCase()))
                    .toList();
            concertTable.setItems(FXCollections.observableArrayList(filteredList));
        });

        buyTicketButton.setOnAction(e -> BuyTicketScreen.showBuyTicketScreen());

        customerVbox.getChildren().addAll(label, concertTable, searchField, buyTicketButton);
        customerTab.setContent(customerVbox);

        // Admin tab
        Tab adminTab = new Tab("Admin");
        VBox adminVbox = new VBox(10);
        adminVbox.setPadding(new Insets(10));
        Button manageCustomersButton = new Button("Hantera kunder");
        Button manageAddressesButton = new Button("Hantera adresser");
        Button manageConcertsButton = new Button("Hantera konserter");
        Button manageArenasButton = new Button("Hantera arenor");
        Button manageWCButton = new Button("Hantera WC");

        manageCustomersButton.setOnAction(e -> ManagementScreen.showManagementScreen("Hantera kunder", Customer.class));
        manageAddressesButton.setOnAction(e -> ManagementScreen.showManagementScreen("Hantera adresser", Address.class));
        manageConcertsButton.setOnAction(e -> ManagementScreen.showManagementScreen("Hantera konserter", Concert.class));
        manageArenasButton.setOnAction(e -> ManagementScreen.showManagementScreen("Hantera arenor", Arena.class));
        manageWCButton.setOnAction(e -> ManagementScreen.showManagementScreen("Hantera WC", WC.class));

        adminVbox.getChildren().addAll(manageCustomersButton, manageAddressesButton, manageConcertsButton, manageArenasButton, manageWCButton);
        adminTab.setContent(adminVbox);

        tabPane.getTabs().addAll(customerTab, adminTab);
        Scene scene = new Scene(tabPane, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
