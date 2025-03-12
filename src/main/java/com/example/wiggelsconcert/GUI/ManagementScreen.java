package com.example.wiggelsconcert.GUI;

import com.example.wiggelsconcert.DAO.*;
import com.example.wiggelsconcert.Entities.*;
import com.example.wiggelsconcert.utils.WcOperations;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ManagementScreen {

    private static final CustomerDAO customerDAO = new CustomerDAO();
    private static final ConcertDAO concertDAO = new ConcertDAO();
    private static final ArenaDAO arenaDAO = new ArenaDAO();
    private static final AddressDAO addressDAO = new AddressDAO();
    private static final WcDAO wcDAO = new WcDAO();

    public static <T> void showManagementScreen(String title, Class<T> entityClass) {
        Stage stage = new Stage();
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        Label label = new Label(title);
        ObservableList<T> observableList = FXCollections.observableArrayList(fetchEntities(entityClass));
        ListView<T> listView = new ListView<>(observableList);
        Button addButton = new Button("Lägg till ny");
        Button updateButton = new Button("Uppdatera/Se info");
        Button deleteButton = new Button("Ta bort");

        addButton.setOnAction(e -> showEntityForm(entityClass, null, observableList));
        updateButton.setOnAction(e -> {
            T selectedEntity = listView.getSelectionModel().getSelectedItem();
            if (selectedEntity != null) {
                showEntityForm(entityClass, selectedEntity, observableList);
            }
        });
        deleteButton.setOnAction(e -> deleteEntity(listView.getSelectionModel().getSelectedItem(), entityClass));

        vbox.getChildren().addAll(label, listView, addButton, updateButton, deleteButton);
        Scene scene = new Scene(vbox, 400, 400);
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();
    }

    private static <T> List<T> fetchEntities(Class<T> entityClass) {
        if (entityClass == Customer.class) return (List<T>) customerDAO.getAllCustomers();
        if (entityClass == Concert.class) return (List<T>) concertDAO.getAllConcerts();
        if (entityClass == Arena.class) return (List<T>) arenaDAO.getAllArenas();
        if (entityClass == Address.class) return (List<T>) addressDAO.getAllAddresses();
        if (entityClass == WC.class) return (List<T>) wcDAO.getAllWcRegistrations();
        return List.of();
    }

    private static <T> void showEntityForm(Class<T> entityClass, T entity, ObservableList<T> observableList) {
        Stage stage = new Stage();
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        Label titleLabel = new Label(entity == null ? "Lägg till " + entityClass.getSimpleName() : "Uppdatera " + entityClass.getSimpleName());
        vbox.getChildren().add(titleLabel);

        Field[] entityFields = entityClass.getDeclaredFields();
        Map<String, Object> fieldInputs = new HashMap<>();
        VBox formFields = generateFormFields(entityClass, entity, fieldInputs);
        vbox.getChildren().add(formFields);

        Button saveButton = new Button("Spara");
        saveButton.setOnAction(e -> {
            try {
                for (Object input : fieldInputs.values()) {
                    if (input instanceof TextField && ((TextField) input).getText().trim().isEmpty()) {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Alla fält måste fyllas i!");
                        alert.show();
                        return;
                    }
                }

                T newInstance = entity == null ? entityClass.getDeclaredConstructor().newInstance() : entity;
                for (Field field : entityClass.getDeclaredFields()) {
                    field.setAccessible(true);
                    if (fieldInputs.containsKey(field.getName())) {
                        Object input = fieldInputs.get(field.getName());
                        if (input instanceof TextField) {
                            if (field.getType() == int.class || field.getType() == Integer.class) {
                                field.set(newInstance, Integer.parseInt(((TextField) input).getText()));
                            } else if (field.getType() == double.class || field.getType() == Double.class) {
                                field.set(newInstance, Double.parseDouble(((TextField) input).getText()));
                            } else {
                                field.set(newInstance, ((TextField) input).getText());
                            }
                        } else if (input instanceof DatePicker) {
                            field.set(newInstance, ((DatePicker) input).getValue());
                        } else if (input instanceof CheckBox) {
                            field.set(newInstance, ((CheckBox) input).isSelected());
                        } else if (input instanceof ComboBox) {
                            field.set(newInstance, ((ComboBox<?>) input).getValue());
                        }
                    }
                }
                saveOrUpdateEntity(newInstance, entityClass);
                observableList.setAll(fetchEntities(entityClass));
                stage.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        vbox.getChildren().add(saveButton);
        Scene scene = new Scene(vbox, 300, 200 + (entityFields.length * 50));
        stage.setScene(scene);
        stage.setTitle("Formulär");
        stage.show();
    }

    private static <T> VBox generateFormFields(Class<T> entityClass, T entity, Map<String, Object> fieldInputs) {
        VBox vbox = new VBox(10);
        Field[] entityFields = entityClass.getDeclaredFields();

        for (Field field : entityFields) {
            field.setAccessible(true);
            // Don't give access to the id field since we probably want to automate it
            if (field.getName().contains("_id")) continue;

            Label fieldLabel = new Label(field.getName() + ":");

            if (field.getType() == LocalDate.class) {
                DatePicker datePicker = new DatePicker();
                if (entity != null) {
                    try {
                        datePicker.setValue((LocalDate) field.get(entity));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                fieldInputs.put(field.getName(), datePicker);
                vbox.getChildren().addAll(fieldLabel, datePicker);
            } else if (field.getType() == boolean.class || field.getType() == Boolean.class) {
                CheckBox checkBox = new CheckBox();
                if (entity != null) {
                    try {
                        checkBox.setSelected((boolean) field.get(entity));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                fieldInputs.put(field.getName(), checkBox);
                vbox.getChildren().addAll(fieldLabel, checkBox);
            } else if (field.getType() == int.class || field.getType() == Integer.class || field.getType() == double.class || field.getType() == Double.class) {
                TextField numericField = new TextField();
                if (entity != null) {
                    try {
                        numericField.setText(field.get(entity).toString());
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                fieldInputs.put(field.getName(), numericField);
                vbox.getChildren().addAll(fieldLabel, numericField);
            } else if (field.getType() == Address.class) {
                ObservableList<Address> addresses = FXCollections.observableArrayList(addressDAO.getAllAddresses());
                ComboBox<Address> addressComboBox = new ComboBox<>(addresses);
                addressComboBox.setPrefWidth(300);

                TextField searchField = new TextField();
                searchField.setPromptText("Sök adress...");
                searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                    List<Address> filteredList = addresses.stream()
                            .filter(a -> a.toString().toLowerCase().contains(newValue.toLowerCase()))
                            .collect(Collectors.toList());
                    addressComboBox.setItems(FXCollections.observableArrayList(filteredList));
                });

                if (entity != null) {
                    try {
                        Address currentAddress = (Address) field.get(entity);
                        if (currentAddress != null) {
                            addressComboBox.setValue(currentAddress);
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }

                fieldInputs.put(field.getName(), addressComboBox);
                vbox.getChildren().addAll(fieldLabel, addressComboBox, searchField);
            } else if (field.getType() == Arena.class) {
                ObservableList<Arena> arenas = FXCollections.observableArrayList(arenaDAO.getAllArenas());
                ComboBox<Arena> arenaComboBox = new ComboBox<>(arenas);
                arenaComboBox.setPrefWidth(300);

                TextField searchField = new TextField();
                searchField.setPromptText("Sök arena...");
                searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                    List<Arena> filteredList = arenas.stream()
                            .filter(a -> a.getName().toLowerCase().contains(newValue.toLowerCase()))
                            .collect(Collectors.toList());
                    arenaComboBox.setItems(FXCollections.observableArrayList(filteredList));
                });

                if (entity != null) {
                    try {
                        Arena currentArena = (Arena) field.get(entity);
                        if (currentArena != null) {
                            arenaComboBox.setValue(currentArena);
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }

                fieldInputs.put(field.getName(), arenaComboBox);
                vbox.getChildren().addAll(fieldLabel, arenaComboBox, searchField);
            } else if (field.getType() == Customer.class) {
                ObservableList<Customer> customers = FXCollections.observableArrayList(customerDAO.getAllCustomers());
                ComboBox<Customer> customerComboBox = new ComboBox<>(customers);
                customerComboBox.setPrefWidth(300);

                TextField searchField = new TextField();
                searchField.setPromptText("Sök kund...");
                searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                    List<Customer> filteredList = customers.stream()
                            .filter(c -> c.toString().toLowerCase().contains(newValue.toLowerCase()))
                            .collect(Collectors.toList());
                    customerComboBox.setItems(FXCollections.observableArrayList(filteredList));
                });

                if (entity != null) {
                    try {
                        Customer currentCustomer = (Customer) field.get(entity);
                        if (currentCustomer != null) {
                            customerComboBox.setValue(currentCustomer);
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }

                fieldInputs.put(field.getName(), customerComboBox);
                vbox.getChildren().addAll(fieldLabel, customerComboBox, searchField);
            } else if (field.getType() == Concert.class) {
                ObservableList<Concert> concerts = FXCollections.observableArrayList(concertDAO.getAllConcerts());
                ComboBox<Concert> concertComboBox = new ComboBox<>(concerts);
                concertComboBox.setPrefWidth(300);

                TextField searchField = new TextField();
                searchField.setPromptText("Sök konsert...");
                searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                    List<Concert> filteredList = concerts.stream()
                            .filter(c -> c.getArtist().toLowerCase().contains(newValue.toLowerCase()))
                            .collect(Collectors.toList());
                    concertComboBox.setItems(FXCollections.observableArrayList(filteredList));
                });

                if (entity != null) {
                    try {
                        Concert currentConcert = (Concert) field.get(entity);
                        if (currentConcert != null) {
                            concertComboBox.setValue(currentConcert);
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }

                fieldInputs.put(field.getName(), concertComboBox);
                vbox.getChildren().addAll(fieldLabel, concertComboBox, searchField);
            } else {
                TextField textField = new TextField();
                if (entity != null) {
                    try {
                        textField.setText(field.get(entity).toString());
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                fieldInputs.put(field.getName(), textField);
                vbox.getChildren().addAll(fieldLabel, textField);
            }
        }
        return vbox;
    }

    // We use the fact that new entities don't yet have an id to tell new and old entries apart
    private static <T> void saveOrUpdateEntity(T entity, Class<T> entityClass) {
        if (entityClass == Customer.class) {
            if (((Customer) entity).getCustomer_id() == 0) customerDAO.saveCustomer((Customer) entity);
            else customerDAO.updateCustomer((Customer) entity);
        } else if (entityClass == Concert.class) {
            if (((Concert) entity).getConcert_id() == 0) concertDAO.saveConcert((Concert) entity);
            else concertDAO.updateConcert((Concert) entity);
            MainMenuScreen.updateConcertTable();
        } else if (entityClass == Arena.class) {
            if (((Arena) entity).getArena_id() == 0) arenaDAO.saveArena((Arena) entity);
            else arenaDAO.updateArena((Arena) entity);
        } else if (entityClass == Address.class) {
            if (((Address) entity).getAddress_id() == 0) addressDAO.saveAddress((Address) entity);
            else addressDAO.updateAddress((Address) entity);
        } else if (entityClass == WC.class) {
            if (((WC) entity).getWc_id() == 0) wcDAO.saveWc((WC) entity);
            else wcDAO.updateWc((WC) entity);
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Sparad!");
        alert.show();
    }

    private static <T> void deleteEntity(T entity, Class<T> entityClass) {
        if (entity == null) return;
        WcOperations wcOperations = new WcOperations();

        if (entityClass == Address.class) {
            Address address = (Address) entity;
            // Prevent deletion of addresses connected to customers and arenas
            List<Customer> customersUsingAddress = customerDAO.getAllCustomers().stream()
                    .filter(c -> c.getAddress() != null && c.getAddress().getAddress_id() == address.getAddress_id())
                    .collect(Collectors.toList());
            List<Arena> arenasUsingAddress = arenaDAO.getAllArenas().stream()
                    .filter(a -> a.getAddress() != null && a.getAddress().getAddress_id() == address.getAddress_id())
                    .collect(Collectors.toList());

            if (!customersUsingAddress.isEmpty() || !arenasUsingAddress.isEmpty()) {
                StringBuilder warningMessage = new StringBuilder("Adressen används av:\n");
                customersUsingAddress.forEach(c -> warningMessage.append(" - Kund: ").append(c.getFirst_name()).append(" ").append(c.getLast_name()).append("\n"));
                arenasUsingAddress.forEach(a -> warningMessage.append(" - Arena: ").append(a.getName()).append("\n"));
                warningMessage.append("och kan därför inte tas bort!");

                Alert alert = new Alert(Alert.AlertType.WARNING, warningMessage.toString());
                alert.show();
                return;
            }
            addressDAO.deleteAddress(address.getAddress_id());

        } else if (entityClass == Customer.class) {
            Customer customer = (Customer) entity;
            // Delete all booking connected to the customer we delete
            List<WC> customerBookings = wcDAO.getAllWcRegistrations().stream()
                    .filter(wc -> wc.getCustomer().getCustomer_id() == customer.getCustomer_id())
                    .collect(Collectors.toList());
            customerBookings.forEach(wc -> wcDAO.deleteWc(wc.getWc_id()));
            customerDAO.deleteCustomer(customer.getCustomer_id());

        } else if (entityClass == Concert.class) {
            Concert concert = (Concert) entity;
            // Prevent deletion of a concert with bookings
            List<Customer> customersWithBookings = wcOperations.getCustomerByConcert(concert);
            if (!customersWithBookings.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING,
                        "Du måste först ta bort alla bokningar till denna konsert innan den kan tas bort!");
                alert.show();
                return;
            }
            concertDAO.deleteConcert(concert.getConcert_id());

        } else if (entityClass == Arena.class) {
            Arena arena = (Arena) entity;
            // We prevent deletion of arenas with planned concerts
            List<Concert> concertsAtArena = concertDAO.getAllConcerts().stream()
                    .filter(c -> c.getArena() != null && c.getArena().getArena_id() == arena.getArena_id())
                    .collect(Collectors.toList());
            if (!concertsAtArena.isEmpty()) {
                StringBuilder warningMessage = new StringBuilder("Arenan har planerade konserter och kan därför inte tas bort:\n");
                concertsAtArena.forEach(c -> warningMessage.append(" - Konsert: ").append(c.getArtist()).append(", ").append(c.getDate()).append("\n"));
                Alert alert = new Alert(Alert.AlertType.WARNING, warningMessage.toString());
                alert.show();
                return;
            }
            arenaDAO.deleteArena(arena.getArena_id());

        } else if (entityClass == WC.class) {
            wcDAO.deleteWc(((WC) entity).getWc_id());
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Borttaget: " + entity.toString());
        alert.show();
    }
}
