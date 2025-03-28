package com.example.wiggelsconcert.GUI;

import com.example.wiggelsconcert.DAO.AddressDAO;
import com.example.wiggelsconcert.DAO.ConcertDAO;
import com.example.wiggelsconcert.DAO.CustomerDAO;
import com.example.wiggelsconcert.DAO.WcDAO;
import com.example.wiggelsconcert.Entities.*;
import com.example.wiggelsconcert.utils.WcOperations;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.beans.property.SimpleStringProperty;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;

public class MainMenuScreen {
    private final ConcertDAO concertDAO = new ConcertDAO();
    private final AddressDAO addressDAO = new AddressDAO();
    private final CustomerDAO customerDAO = new CustomerDAO();
    private final WcDAO wcDAO = new WcDAO();
    private Customer loggedInCustomer = null;
    private final ObservableList<Concert> concerts = FXCollections.observableArrayList();

    public void showMainMenu(Stage primaryStage) {
        // Remove past concerts since they are no longer bookable
        removePastConcerts();

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
        buyTicketButton.setVisible(false);

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

        concerts.setAll(concertDAO.getAllConcerts());
        concertTable.setItems(concerts);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            List<Concert> filteredList = concerts.stream()
                    .filter(c -> c.getArtist().toLowerCase().contains(newValue.toLowerCase()) ||
                            c.getArena().getName().toLowerCase().contains(newValue.toLowerCase()))
                    .toList();
            concertTable.setItems(FXCollections.observableArrayList(filteredList));
        });

        Label loggedInLabel = new Label("Inte inloggad");
        Label bookedConcertsLabel = new Label("Dina bokningar");
        bookedConcertsLabel.setVisible(false);
        ListView<Concert> bookedConcertsList = new ListView<>();
        bookedConcertsList.setVisible(false);
        VBox loginSection = createLoginSection(buyTicketButton, loggedInLabel, bookedConcertsList, bookedConcertsLabel);

        buyTicketButton.setOnAction(e -> {
            Concert selectedConcert = concertTable.getSelectionModel().getSelectedItem();
            buyTicket(loggedInCustomer, selectedConcert, bookedConcertsList, bookedConcertsLabel);
        });

        customerVbox.getChildren().addAll(label, concertTable, searchField, buyTicketButton, loginSection, bookedConcertsLabel, bookedConcertsList);
        customerTab.setContent(customerVbox);

        // Update this tab every time we switch to it
        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (newTab == customerTab) {
                updateCustomerTab(loggedInLabel, (Button) loginSection.getChildren().get(1),
                        (Button) loginSection.getChildren().get(2), (Button) loginSection.getChildren().get(3),
                        buyTicketButton, bookedConcertsList, bookedConcertsLabel);
            }
        });

        // Admin tab
        Tab adminTab = new Tab("Admin");
        VBox adminVbox = new VBox(10);
        adminVbox.setPadding(new Insets(10));
        adminVbox.setStyle("-fx-alignment: center;");
        Button manageCustomersButton = new Button("Hantera kunder");
        Button manageAddressesButton = new Button("Hantera adresser");
        Button manageConcertsButton = new Button("Hantera konserter");
        Button manageArenasButton = new Button("Hantera arenor");
        Button manageWCButton = new Button("Hantera WC");
        Button viewBookingsButton = new Button("Se bokningar");

        ManagementScreen managementScreen = new ManagementScreen(this);

        manageCustomersButton.setOnAction(e -> managementScreen.showManagementScreen("Hantera kunder", Customer.class));
        manageAddressesButton.setOnAction(e -> managementScreen.showManagementScreen("Hantera adresser", Address.class));
        manageConcertsButton.setOnAction(e -> managementScreen.showManagementScreen("Hantera konserter", Concert.class));
        manageArenasButton.setOnAction(e -> managementScreen.showManagementScreen("Hantera arenor", Arena.class));
        manageWCButton.setOnAction(e -> managementScreen.showManagementScreen("Hantera WC", WC.class));
        viewBookingsButton.setOnAction(e -> showBookingsScreen());

        adminVbox.getChildren().addAll(manageCustomersButton, manageAddressesButton, manageConcertsButton, manageArenasButton, manageWCButton, viewBookingsButton);
        adminTab.setContent(adminVbox);

        tabPane.getTabs().addAll(customerTab, adminTab);
        Scene scene = new Scene(tabPane, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Let's just update everything just to make sure
    private void updateCustomerTab(Label loggedInLabel, Button existingCustomerButton, Button newCustomerButton,
                                          Button logoutButton, Button buyTicketButton, ListView<Concert> bookedConcertsList,
                                          Label bookedConcertsLabel) {
        updateConcertTable();
        updateBookedConcerts(bookedConcertsList);
        updateLoginSection(loggedInLabel, existingCustomerButton, newCustomerButton, logoutButton, buyTicketButton, bookedConcertsList, bookedConcertsLabel);
    }

    public void updateConcertTable() {
        concerts.setAll(concertDAO.getAllConcerts());
    }
    private void updateBookedConcerts(ListView<Concert> bookedConcertsList) {
        if (loggedInCustomer != null) {
            WcOperations wcOperations = new WcOperations();
            List<Concert> bookedConcerts = wcOperations.getConcertByCustomer(loggedInCustomer);
            bookedConcertsList.setItems(FXCollections.observableArrayList(bookedConcerts));
            bookedConcertsList.setVisible(true);
        } else {
            bookedConcertsList.getItems().clear();
            bookedConcertsList.setVisible(false);
        }
    }

    private void updateLoginSection(Label loggedInLabel, Button existingCustomerButton, Button newCustomerButton, Button logoutButton, Button buyTicketButton, ListView<Concert> bookedConcertsList, Label bookedConcertsLabel) {
        if (loggedInCustomer != null) {
            loggedInLabel.setText("Inloggad som: " + loggedInCustomer.getFirst_name() + " " + loggedInCustomer.getLast_name());
            existingCustomerButton.setVisible(false);
            newCustomerButton.setVisible(false);
            logoutButton.setVisible(true);
            buyTicketButton.setVisible(true);
            updateBookedConcerts(bookedConcertsList);
            bookedConcertsList.setVisible(true);
            bookedConcertsLabel.setVisible(true);
        } else {
            loggedInLabel.setText("Inte inloggad");
            buyTicketButton.setVisible(false);
            existingCustomerButton.setVisible(true);
            newCustomerButton.setVisible(true);
            logoutButton.setVisible(false);
            bookedConcertsList.setVisible(false);
            bookedConcertsList.getItems().clear();
            bookedConcertsLabel.setVisible(false);
        }
    }

    private VBox createLoginSection(Button buyTicketButton, Label loggedInLabel, ListView<Concert> bookedConcertsList, Label bookedConcertsLabel) {
        VBox loginSection = new VBox(10);
        loginSection.setPadding(new Insets(10));

        Button existingCustomerButton = new Button("Logga in");
        Button newCustomerButton = new Button("Registrera dig");
        Button logoutButton = new Button("Logga ut");
        logoutButton.setVisible(false);
        bookedConcertsList.setVisible(false);
        bookedConcertsLabel.setVisible(false);

        existingCustomerButton.setOnAction(e -> {
            showCustomerSelection(buyTicketButton, loggedInLabel, bookedConcertsList, bookedConcertsLabel);
        });

        newCustomerButton.setOnAction(e -> {
            showNewCustomerForm(buyTicketButton, loggedInLabel, bookedConcertsList, bookedConcertsLabel);
        });

        logoutButton.setOnAction(e -> {
            loggedInCustomer = null;
            updateLoginSection(loggedInLabel, existingCustomerButton, newCustomerButton, logoutButton, buyTicketButton, bookedConcertsList, bookedConcertsLabel);
        });

        loginSection.getChildren().addAll(loggedInLabel, existingCustomerButton, newCustomerButton, logoutButton);
        return loginSection;
    }

    private void showCustomerSelection(Button buyTicketButton, Label loggedInLabel, ListView<Concert> bookedConcertsList, Label bookedConcertsLabel) {
        Stage stage = new Stage();
        VBox vbox = new VBox(10);
        vbox.setPadding(new javafx.geometry.Insets(10));

        ListView<Customer> customerListView = new ListView<>();
        customerListView.setItems(javafx.collections.FXCollections.observableArrayList(new CustomerDAO().getAllCustomers()));
        Button selectButton = new Button("Välj");

        selectButton.setOnAction(e -> {
            loggedInCustomer = customerListView.getSelectionModel().getSelectedItem();
            if (loggedInCustomer != null) {
                updateLoginSection(loggedInLabel, (Button) ((VBox) loggedInLabel.getParent()).getChildren().get(1),
                        (Button) ((VBox) loggedInLabel.getParent()).getChildren().get(2),
                        (Button) ((VBox) loggedInLabel.getParent()).getChildren().get(3),
                        buyTicketButton, bookedConcertsList, bookedConcertsLabel);
                stage.close();
            }
        });

        vbox.getChildren().addAll(new Label("Välj en kund:"), customerListView, selectButton);
        Scene scene = new Scene(vbox, 300, 400);
        stage.setScene(scene);
        stage.setTitle("Välj Kund");
        stage.show();
    }

    private void showNewCustomerForm(Button buyTicketButton, Label loggedInLabel, ListView<Concert> bookedConcertsList, Label bookedConcertsLabel) {
        Stage stage = new Stage();
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        TextField firstNameField = new TextField();
        firstNameField.setPromptText("Förnamn");
        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Efternamn");
        DatePicker birthDatePicker = new DatePicker();
        birthDatePicker.setPromptText("Födelsedatum");
        TextField phoneNumberField = new TextField();
        phoneNumberField.setPromptText("Telefonnummer");

        TextField streetField = new TextField();
        streetField.setPromptText("Gata");
        TextField numberField = new TextField();
        numberField.setPromptText("Husnummer");
        TextField zipCodeField = new TextField();
        zipCodeField.setPromptText("Postnummer");
        TextField regionField = new TextField();
        regionField.setPromptText("Stad");

        Button registerButton = new Button("Registrera");

        registerButton.setOnAction(e -> {
            String street = streetField.getText();
            String number = numberField.getText();
            String zipCode = zipCodeField.getText();
            String region = regionField.getText();

            Address existingAddress = addressDAO.getAllAddresses().stream()
                    .filter(a -> a.getStreet().equalsIgnoreCase(street) &&
                            a.getNumber().equalsIgnoreCase(number) &&
                            a.getZip_code().equalsIgnoreCase(zipCode) &&
                            a.getRegion().equalsIgnoreCase(region))
                    .findFirst()
                    .orElse(null);

            if (existingAddress == null) {
                existingAddress = new Address(street, number, zipCode, region);
                addressDAO.saveAddress(existingAddress);
            }

            Customer newCustomer = new Customer(firstNameField.getText(), lastNameField.getText(), birthDatePicker.getValue(), phoneNumberField.getText(), existingAddress);
            customerDAO.saveCustomer(newCustomer);
            loggedInCustomer = newCustomer;
            updateLoginSection(loggedInLabel, (Button) ((VBox) loggedInLabel.getParent()).getChildren().get(1),
                    (Button) ((VBox) loggedInLabel.getParent()).getChildren().get(2),
                    (Button) ((VBox) loggedInLabel.getParent()).getChildren().get(3),
                    buyTicketButton, bookedConcertsList, bookedConcertsLabel);
            stage.close();
        });

        vbox.getChildren().addAll(new Label("Registrera ny kund:"), firstNameField, lastNameField, birthDatePicker, phoneNumberField,
                new Label("Adress:"), streetField, numberField, zipCodeField, regionField, registerButton);
        Scene scene = new Scene(vbox, 350, 450);
        stage.setScene(scene);
        stage.setTitle("Registrera Kund");
        stage.show();
    }

    private void buyTicket(Customer customer, Concert concert, ListView<Concert> bookedConcertsList, Label bookedConcertsLabel) {
        if (customer == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Du måste vara inloggad för att köpa en biljett!");
            alert.show();
            return;
        }
        if (concert == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Du måste välja en konsert för att köpa en biljett!");
            alert.show();
            return;
        }

        WC wcEntry = new WC("Biljettköp", customer, concert);
        wcDAO.saveWc(wcEntry);

        Alert alert = new Alert(Alert.AlertType.INFORMATION,
                "Biljett köpt!\nKund: " + customer.getFirst_name() + " " + customer.getLast_name() +
                        "\nKonsert: " + concert.getArtist() +
                        "\nDatum: " + concert.getDate());
        alert.show();
        updateBookedConcerts(bookedConcertsList);
        bookedConcertsList.setVisible(true);
        bookedConcertsLabel.setVisible(true);
    }

    private void showBookingsScreen() {
        Stage stage = new Stage();
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        Label selectConcertLabel = new Label("Välj konsert:");
        ComboBox<Concert> concertComboBox = new ComboBox<>(FXCollections.observableArrayList(concertDAO.getAllConcerts()));
        ListView<Customer> customerListView = new ListView<>();
        customerListView.setPlaceholder(new Label("Inga bokningar"));

        concertComboBox.setOnAction(e -> {
            Concert selectedConcert = concertComboBox.getValue();
            if (selectedConcert != null) {
                WcOperations wcOperations = new WcOperations();
                List<Customer> bookedCustomers = wcOperations.getCustomerByConcert(selectedConcert);
                customerListView.setItems(FXCollections.observableArrayList(bookedCustomers));
            }
        });

        vbox.getChildren().addAll(selectConcertLabel, concertComboBox, customerListView);
        Scene scene = new Scene(vbox, 400, 400);
        stage.setScene(scene);
        stage.setTitle("Bokningar");
        stage.show();
    }

    private void removePastConcerts() {
        List<Concert> allConcerts = concertDAO.getAllConcerts();
        LocalDate today = LocalDate.now();

        for (Concert concert : allConcerts) {
            if (concert.getDate().isBefore(today)) {
                List<WC> relatedBookings = wcDAO.getAllWcRegistrations().stream()
                        .filter(wc -> wc.getConcert().getConcert_id() == concert.getConcert_id())
                        .toList();

                // Delete all related bookings
                for (WC wc : relatedBookings) {
                    wcDAO.deleteWc(wc.getWc_id());
                    System.out.println("Raderade bokning för kund: " + wc.getCustomer().getFirst_name() +
                            " " + wc.getCustomer().getLast_name() + " till konsert: " + concert.getArtist());
                }
                concertDAO.deleteConcert(concert.getConcert_id());
                System.out.println("Raderade gammal konsert: " + concert.getArtist() + " den " + concert.getDate());
            }
        }
    }

    public void logoutIfCustomerDeleted(int deletedCustomerId) {
        if (loggedInCustomer != null && loggedInCustomer.getCustomer_id() == deletedCustomerId) {
            loggedInCustomer = null;
        }
    }
}
