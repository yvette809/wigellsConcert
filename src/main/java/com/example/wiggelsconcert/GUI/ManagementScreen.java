package com.example.wiggelsconcert.GUI;

import com.example.wiggelsconcert.DAO.*;
import com.example.wiggelsconcert.Entities.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.lang.reflect.Field;
import java.util.List;

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

        // Disable deleting addresses as that will lead to issues
        if (title.equals("Hantera adresser")) {
            deleteButton.setDisable(true);
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

        TextField[] fields = new TextField[entityClass.getDeclaredFields().length];
        Field[] entityFields = entityClass.getDeclaredFields();

        for (int i = 0; i < entityFields.length; i++) {
            if (!entityFields[i].getName().equals("id")) {
                Label fieldLabel = new Label(entityFields[i].getName() + ":");
                fields[i] = new TextField();
                vbox.getChildren().addAll(fieldLabel, fields[i]);
            }
        }

        Button saveButton = new Button("Spara");
        saveButton.setOnAction(e -> {
            try {
                if (entity == null) {
                    T newInstance = entityClass.getDeclaredConstructor().newInstance();
                    for (int i = 0; i < entityFields.length; i++) {
                        if (!entityFields[i].getName().equals("id")) {
                            entityFields[i].setAccessible(true);
                            entityFields[i].set(newInstance, fields[i].getText());
                        }
                    }
                    saveEntity(newInstance, entityClass);
                } else {
                    for (int i = 0; i < entityFields.length; i++) {
                        if (!entityFields[i].getName().equals("id")) {
                            entityFields[i].setAccessible(true);
                            entityFields[i].set(entity, fields[i].getText());
                        }
                    }
                    updateEntity(entity, entityClass);
                }
                stage.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        vbox.getChildren().add(saveButton);
        Scene scene = new Scene(vbox, 300, 150 + (entityFields.length * 50));
        stage.setScene(scene);
        stage.setTitle("Formulär");
        stage.show();
    }

    private static <T> void saveEntity(T entity, Class<T> entityClass) {
        if (entityClass == Customer.class) customerDAO.saveCustomer((Customer) entity);
        else if (entityClass == Concert.class) concertDAO.saveConcert((Concert) entity);
        else if (entityClass == Arena.class) arenaDAO.saveArena((Arena) entity);
        else if (entityClass == Address.class) addressDAO.saveAddress((Address) entity);
        else if (entityClass == WC.class) wcDAO.saveWc((WC) entity);
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Sparad!");
        alert.show();
    }

    private static <T> void updateEntity(T entity, Class<T> entityClass) {
        if (entityClass == Customer.class) customerDAO.updateCustomer((Customer) entity);
        else if (entityClass == Concert.class) concertDAO.updateConcert((Concert) entity);
        else if (entityClass == Arena.class) arenaDAO.updateArena((Arena) entity);
        else if (entityClass == Address.class) addressDAO.updateAddress((Address) entity);
        else if (entityClass == WC.class) wcDAO.updateWc((WC) entity);
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Uppdaterad!");
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
