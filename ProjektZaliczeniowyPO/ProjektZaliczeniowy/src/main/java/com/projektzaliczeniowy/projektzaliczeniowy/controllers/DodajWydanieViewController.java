package com.projektzaliczeniowy.projektzaliczeniowy.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.Session;
import org.hibernate.query.Query;

import com.projektzaliczeniowy.projektzaliczeniowy.Klient;
import com.projektzaliczeniowy.projektzaliczeniowy.MainApp;
import com.projektzaliczeniowy.projektzaliczeniowy.Sprzet;
import com.projektzaliczeniowy.projektzaliczeniowy.Wydanie;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

public class DodajWydanieViewController {

    private final int ID_SPRZETU = 0;
    private final int ID_KLIENTA = 1;
    private final int DATA_WYDANIA = 2;
    @FXML //Pozwala tworzy kontrolery i pozwala się dostać to prywatnych pól klasy
    private VBox glowny;
    @FXML
    private ChoiceBox<Sprzet> idSprzetu;
    @FXML
    private ChoiceBox<Klient> idKlienta;
    @FXML
    private DatePicker dataWydania;
    @FXML
    private DatePicker dataZwrotu;
    @FXML
    private Button dodaj;
    private Runnable refreshCallback;
    private ObservableList<Boolean> warunki = FXCollections.observableArrayList(false, false, false);

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

        try {

            Session session = MainApp.getSessionFactory().openSession();
            session.beginTransaction();

            Query<Klient> queryKlienci = session.createQuery("FROM Klient", Klient.class);
            List<Klient> klienci = queryKlienci.list();

            idKlienta.getItems().addAll(klienci);

            try {

                idKlienta.setValue(klienci.get(0));
                warunki.set(ID_KLIENTA, true);

            }catch(IndexOutOfBoundsException e) {

                idKlienta.setDisable(true);

            }

            Query<Wydanie> queryWydania = session.createQuery("FROM Wydanie", Wydanie.class);
            Set<Integer> wydania = queryWydania.list().stream().filter(w -> w.getDataZwrotu() == null).map(w -> w.getIdSprzetu()).collect(Collectors.toSet());

            Query<Sprzet> querySprzet = session.createQuery("FROM Sprzet", Sprzet.class);
            List<Sprzet> sprzet = querySprzet.list().stream().filter(s -> !wydania.contains(s.getId())).collect(Collectors.toList());

            idSprzetu.getItems().addAll(sprzet);

            try {

                idSprzetu.setValue(sprzet.get(0));
                warunki.set(ID_SPRZETU, true);

            }catch(IndexOutOfBoundsException e) {

                idSprzetu.setDisable(true);

            }

            session.close();

        }catch(Exception e) {

            System.out.println(e.getMessage());

        }

        idKlienta.setConverter(new StringConverter<Klient>() {

            @Override
            public String toString(Klient klient) {

                return klient != null ? klient.getId() + ": " + klient.getImie() + " " + klient.getNazwisko() + " " + klient.getNumerTel() : "";

            }

            @Override
            public Klient fromString(String string) { return null; }

        });

        idSprzetu.setConverter(new StringConverter<Sprzet>() {

            @Override
            public String toString(Sprzet sprzet) { return sprzet != null ? sprzet.getId() + ": " + sprzet.getKategoria() + " " + sprzet.getModel() : ""; }

            @Override
            public Sprzet fromString(String string) { return null; }

        });

        dataZwrotu.setDayCellFactory(d -> {

            return new DateCell() {

                @Override
                public void updateItem(LocalDate item, boolean empty) {

                    super.updateItem(item, empty);
                    setDisable(item.isBefore(dataWydania.getValue()));

                }

            };

        });

        dataWydania.valueProperty().addListener((ov, oldValue, newValue) -> {

            if(newValue == null) warunki.set(DATA_WYDANIA, false);
            else warunki.set(DATA_WYDANIA, true);

        });

    }

    @FXML
    private void add() {

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("ewidencja");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();
        entityManager.persist(new Wydanie(null, idSprzetu.getValue().getId(), idKlienta.getValue().getId(), dataWydania.getValue(), dataZwrotu.getValue()));
        entityManager.getTransaction().commit();

        entityManager.close();
        entityManagerFactory.close();

        System.out.println("dodano wydanie");

        back();

    }

    @FXML
    private void back() {

        refreshCallback.run();
        glowny.getScene().getWindow().hide();

    }

}
