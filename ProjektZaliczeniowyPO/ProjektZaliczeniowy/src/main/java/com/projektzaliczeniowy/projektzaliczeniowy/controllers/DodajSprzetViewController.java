package com.projektzaliczeniowy.projektzaliczeniowy.controllers;

import com.projektzaliczeniowy.projektzaliczeniowy.Sprzet;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class DodajSprzetViewController {

    //prywatne zmienne na potrzeby walidacji (nie dodanie sprzętu bez modelu i kategorii)
    private final int KATEGORIA = 0;
    private final int MODEL = 1;
    //zmienne id od fxml
    @FXML
    private VBox glowny;
    @FXML
    private TextField kategoria;
    @FXML
    private TextField model;
    @FXML
    private TextField opis;
    @FXML
    private Button dodaj;
    private Runnable refreshCallback;
    private ObservableList<Boolean> warunki = FXCollections.observableArrayList(false, false);

    public void init(Runnable refreshCallback) { this.refreshCallback = refreshCallback; }



    @FXML
    private void initialize() {

        warunki.addListener((ListChangeListener<? super Boolean>) change -> {

            while(change.next()) {

                if(change.wasReplaced()) {

                    if(change.getList().filtered(element -> element.equals(true)).size() == warunki.size()) dodaj.setDisable(false);
                    else dodaj.setDisable(true);

                }

            }

        });

        //sprawdzanie warunków poprawności w dodawaniu

        kategoria.textProperty().addListener((ov, oldValue, newValue) -> {

            if(newValue.isEmpty()) warunki.set(KATEGORIA, false);
            else warunki.set(KATEGORIA, true);

        });

        model.textProperty().addListener((ov, oldValue, newValue) -> {

            if(newValue.isEmpty()) warunki.set(MODEL, false);
            else warunki.set(MODEL, true);

        });

    }
    //dodawanie nowego sprzetu
    @FXML
    private void add() {

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("ewidencja");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();
        entityManager.persist(new Sprzet(null, kategoria.getText(), model.getText(), opis.getText()));
        entityManager.getTransaction().commit();

        entityManager.close();
        entityManagerFactory.close();

        System.out.println("dodano sprzet");

        back();

    }

    @FXML
    private void back() {

        refreshCallback.run();
        glowny.getScene().getWindow().hide();

    }

}
