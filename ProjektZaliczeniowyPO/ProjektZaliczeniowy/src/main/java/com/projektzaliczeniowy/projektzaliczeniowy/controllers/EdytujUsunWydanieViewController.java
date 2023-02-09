package com.projektzaliczeniowy.projektzaliczeniowy.controllers;

import java.time.LocalDate;
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
import jakarta.persistence.EntityTransaction;
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

public class EdytujUsunWydanieViewController {

    private final int ID_KLIENTA = 0;
    private final int ID_SPRZETU = 1;
    private final int DATA_WYDANIA = 2;
    @FXML
    private VBox edytujusunwydanie;
    @FXML
    private ChoiceBox<Klient> idKlienta;
    @FXML
    private ChoiceBox<Sprzet> idSprzetu;
    @FXML
    private DatePicker dataWydania;
    @FXML
    private DatePicker dataZwrotu;
    @FXML
    private Button zmien;
    private Wydanie wydanie;
    private Runnable refreshCallback;
    private ObservableList<Boolean> warunki = FXCollections.observableArrayList(true, true, true);

    @FXML
    private void initialize() {

        warunki.addListener((ListChangeListener<? super Boolean>) change -> {

            while(change.next()) {

                if(change.wasReplaced()) {

                    if(change.getList().filtered(element -> element.equals(true)).size() == warunki.size()) zmien.setDisable(false);
                    else zmien.setDisable(true);

                }

            }

        });

    }

    public void init(Wydanie wydanie, Runnable refreshCallback) {

        ObservableList<Klient> observableKlienci = FXCollections.observableArrayList();
        ObservableList<Sprzet> observableSprzet = FXCollections.observableArrayList();

        try {

            Session session = MainApp.getSessionFactory().openSession();
            session.beginTransaction();

            Query<Klient> queryKlienci = session.createQuery("FROM Klient", Klient.class);
            observableKlienci.addAll(queryKlienci.list());

            Query<Wydanie> queryWydania = session.createQuery("FROM Wydanie", Wydanie.class);
            Set<Integer> wydania = queryWydania.list().stream().map(w -> w.getIdSprzetu()).filter(wId -> wId != wydanie.getIdSprzetu()).collect(Collectors.toSet());

            Query<Sprzet> querySprzet = session.createQuery("FROM Sprzet", Sprzet.class);
            observableSprzet.addAll(querySprzet.list().stream().filter(s -> !wydania.contains(s.getId())).toList());


            session.close();

        }catch(Exception e) {

            System.out.println(e.getMessage());

        }
        //konwertuje klase do string(na potrzeby choicebox)
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

        idKlienta.getItems().addAll(observableKlienci);
        idSprzetu.getItems().addAll(observableSprzet);

        try {

            idKlienta.setValue(observableKlienci.filtered(klient -> { return klient.getId().equals(wydanie.getIdKlienta()); }).get(0));
            warunki.set(ID_KLIENTA, true);

        }catch(IndexOutOfBoundsException e) {

            idKlienta.setValue(null);

        }

        try {

            idSprzetu.setValue(observableSprzet.filtered(sprzet -> { return sprzet.getId().equals(wydanie.getIdSprzetu()); }).get(0));
            warunki.set(ID_SPRZETU, true);

        }catch(IndexOutOfBoundsException e) {

            idSprzetu.setValue(null);

        }

        dataWydania.setValue(wydanie.getDataWydania());

        dataZwrotu.setValue(wydanie.getDataZwrotu());
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

        this.wydanie = wydanie;
        this.refreshCallback = refreshCallback;

    }

    @FXML
    private void change() {

        Wydanie tempWydanie = new Wydanie(wydanie.getId(), idSprzetu.getValue().getId(), idKlienta.getValue().getId(), dataWydania.getValue(),
                                          dataZwrotu.getValue());
        System.out.println(tempWydanie.toString());

        EntityManagerFactory emf = null;
        EntityManager em = null;
        EntityTransaction transaction = null;

        try {

            emf = Persistence.createEntityManagerFactory("ewidencja");
            em = emf.createEntityManager();
            transaction = em.getTransaction();
            transaction.begin();

            Wydanie wydanieEdit = em.find(Wydanie.class, tempWydanie.getId());
            wydanieEdit.setIdSprzetu(tempWydanie.getIdSprzetu());
            wydanieEdit.setIdKlienta(tempWydanie.getIdKlienta());
            wydanieEdit.setDataWydania(tempWydanie.getDataWydania());
            wydanieEdit.setDataZwrotu(tempWydanie.getDataZwrotu());

            transaction.commit();

        }catch(Exception x) {

            transaction.rollback();

        }finally {

            em.close();
            emf.close();

        }

        back();

    }

    @FXML
    private void delete() {

        Session session = MainApp.getSessionFactory().openSession();
        session.beginTransaction();
        Wydanie wydanieDel = (Wydanie) session.getReference(Wydanie.class, wydanie.getId());

        session.remove(wydanieDel);
        session.getTransaction().commit();

        session.close();

        back();

    }

    @FXML
    private void back() {

        refreshCallback.run();
        edytujusunwydanie.getScene().getWindow().hide();

    }

}
