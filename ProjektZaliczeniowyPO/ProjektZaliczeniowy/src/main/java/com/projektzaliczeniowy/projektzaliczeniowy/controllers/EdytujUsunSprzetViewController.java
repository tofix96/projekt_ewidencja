package com.projektzaliczeniowy.projektzaliczeniowy.controllers;

import org.hibernate.Session;
import org.hibernate.query.Query;

import com.projektzaliczeniowy.projektzaliczeniowy.MainApp;
import com.projektzaliczeniowy.projektzaliczeniowy.Sprzet;
import com.projektzaliczeniowy.projektzaliczeniowy.Wydanie;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceException;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class EdytujUsunSprzetViewController {

    private final int KATEGORIA = 0;
    private final int MODEL = 1;
    @FXML
    private VBox edytujusunsprzet;
    @FXML
    private TextField kategoria;
    @FXML
    private TextField model;
    @FXML
    private TextField opis;
    @FXML
    private Button usun;
    @FXML
    private Button zmien;
    private Sprzet sprzet;

    private Runnable refreshCallback;
    private ObservableList<Boolean> warunki = FXCollections.observableArrayList(true, true);
    //funckja nasłuchujaca zmian i uruchomijaca guzik jesli dwa piersze pola tekstowe sa zapelnione
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

        kategoria.textProperty().addListener((ov, oldValue, newValue) -> {

            if(newValue.isEmpty()) warunki.set(KATEGORIA, false);
            else warunki.set(KATEGORIA, true);

        });

        model.textProperty().addListener((ov, oldValue, newValue) -> {

            if(newValue.isEmpty()) warunki.set(MODEL, false);
            else warunki.set(MODEL, true);

        });

    }

    public void init(Sprzet sprzet, Runnable refreshCallback) {

        this.refreshCallback = refreshCallback;

        if(sprzet == null) return;

        kategoria.setText(sprzet.getKategoria());
        model.setText(sprzet.getModel());
        opis.setText(sprzet.getOpis());

        try {

            Session sessions = MainApp.getSessionFactory().openSession();
            sessions.beginTransaction();

            Query<Wydanie> query = sessions.createQuery("FROM Wydanie w WHERE w.idSprzetu = :id_sprzetu", Wydanie.class);
            query.setParameter("id_sprzetu", sprzet.getId());

            if(!query.list().isEmpty()) usun.setDisable(true);

            sessions.close();

        }catch(Exception m) {

            System.out.println(m.getMessage());

        }

        this.sprzet = sprzet;

    }

    @FXML
    private void back() {

        refreshCallback.run();
        edytujusunsprzet.getScene().getWindow().hide();

    }

    //nie powinno się wyświetlić, ponieważ jest blokada na przycisku
    @FXML
    private void delete() {

        Session session = MainApp.getSessionFactory().openSession();
        session.beginTransaction();

        session.remove((Sprzet) session.getReference(Sprzet.class, sprzet.getId()));

        try {

            session.getTransaction().commit();

        }catch(PersistenceException e) {

            Alert error = new Alert(AlertType.ERROR);
            error.setHeaderText("Błąd");
            error.setContentText("Nie można usunąć sprzętu!\n" + e.getMessage());
            error.showAndWait();
            return;

        }

        session.close();

        back();

    }

    @FXML
    private void change() {

        Sprzet tempSprzet = new Sprzet(sprzet.getId(), kategoria.getText(), model.getText(), opis.getText());
        System.out.println(tempSprzet.toString());

        EntityManagerFactory emf = null;
        EntityManager em = null;
        EntityTransaction transaction = null;

        try {

            emf = Persistence.createEntityManagerFactory("ewidencja");
            em = emf.createEntityManager();
            transaction = em.getTransaction();
            transaction.begin();

            Sprzet sprzetEdit = em.find(Sprzet.class, tempSprzet.getId());
            sprzetEdit.setKategoria(tempSprzet.getKategoria());
            sprzetEdit.setModel(tempSprzet.getModel());
            sprzetEdit.setOpis(tempSprzet.getOpis());

            transaction.commit();

        }catch(Exception x) {

            transaction.rollback();

        }finally {

            em.close();
            emf.close();

        }

        back();

    }

}
