package com.example.wiggelsconcert.GUI;

import com.example.wiggelsconcert.DAO.*;
import com.example.wiggelsconcert.Entities.*;
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
        ListView<T> listView = new ListView<>();
        Button addButton = new Button("Lägg till");
        Button updateButton = new Button("Uppdatera");
        Button deleteButton = new Button("Ta bort");

        // Disable these since it will only lead to issues down the line
        if (title.equals("Hantera adresser")) {
            deleteButton.setDisable(true);
        } else if (title.equals("Hantera WC")) {
            addButton.setDisable(true);
        }

        List<T> items = fetchEntities(entityClass);
        listView.getItems().addAll(items);

        addButton.setOnAction(e -> showEntityForm(entityClass, null));
        updateButton.setOnAction(e -> {
            T selectedEntity = listView.getSelectionModel().getSelectedItem();
            if (selectedEntity != null) {
                showEntityForm(entityClass, selectedEntity);
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

    private static <T> void showEntityForm(Class<T> entityClass, T entity) {
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
                numericField.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (!newValue.matches("\\d*")) {
                        numericField.setText(oldValue);
                    }
                });
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
                vbox.getChildren().addAll(fieldLabel, searchField, arenaComboBox);
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
            if (((Customer) entity).getId() == 0) customerDAO.saveCustomer((Customer) entity);
            else customerDAO.updateCustomer((Customer) entity);
        } else if (entityClass == Concert.class) {
            if (((Concert) entity).getConcert_id() == 0) concertDAO.saveConcert((Concert) entity);
            else concertDAO.updateConcert((Concert) entity);
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
        if (entityClass == Customer.class) customerDAO.deleteCustomer(((Customer) entity).getId());
        else if (entityClass == Concert.class) concertDAO.deleteConcert(((Concert) entity).getConcert_id());
        else if (entityClass == Arena.class) arenaDAO.deleteArena(((Arena) entity).getArena_id());
        else if (entityClass == WC.class) wcDAO.deleteWc(((WC) entity).getWc_id());
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Borttaget: " + entity.toString());
        alert.show();
    }
}
