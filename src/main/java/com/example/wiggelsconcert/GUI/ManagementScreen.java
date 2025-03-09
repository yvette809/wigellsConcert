package com.example.wiggelsconcert.GUI;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.lang.reflect.Field;
import java.util.List;

import static com.example.wiggelsconcert.Main.sessionFactory;

public class ManagementScreen {
    public <T> void showManagementScreen(String title, Class<T> entityClass) {
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

        Session session = sessionFactory.openSession();
        List<T> items = session.createQuery("FROM " + entityClass.getSimpleName(), entityClass).list();
        session.close();

        listView.getItems().addAll(items);

        addButton.setOnAction(e -> showEntityForm(entityClass, null));
        updateButton.setOnAction(e -> {
            T selectedEntity = listView.getSelectionModel().getSelectedItem();
            if (selectedEntity != null) {
                showEntityForm(entityClass, selectedEntity);
            }
        });
        deleteButton.setOnAction(e -> deleteEntity(listView.getSelectionModel().getSelectedItem()));

        vbox.getChildren().addAll(label, listView, addButton, updateButton, deleteButton);
        Scene scene = new Scene(vbox, 400, 400);
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();
    }

    private <T> void showEntityForm(Class<T> entityClass, T entity) {
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
    }

    private <T> void saveEntity(T entity) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.saveOrUpdate(entity);
        tx.commit();
        session.close();
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Sparad!");
        alert.show();
    }

    private <T> void deleteEntity(T entity) {
        if (entity == null) return;
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.delete(entity);
        tx.commit();
        session.close();
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Borttaget: " + entity.toString());
        alert.show();
    }
}
