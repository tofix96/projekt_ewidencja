package com.projektzaliczeniowy.projektzaliczeniowy.controllers;

import org.hibernate.Session;
import org.hibernate.query.Query;

import com.projektzaliczeniowy.projektzaliczeniowy.Klient;
import com.projektzaliczeniowy.projektzaliczeniowy.MainApp;
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
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

public class EdytujUsunKlientaViewController {

    private final int IMIE = 0;
    private final int NAZWISKO = 1;
    private final int NUMER_TEL = 2;
    @FXML
    private VBox edytujusunklienta;
    @FXML
    private TextField imie;
    @FXML
    private TextField nazwisko;
    @FXML
    private TextField numerTel;
    @FXML
    private Button usun;
    @FXML
    private Button zmien;
    private Klient klient;
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

        imie.textProperty().addListener((ov, oldValue, newValue) -> {

            if(newValue.isEmpty()) warunki.set(IMIE, false);
            else warunki.set(IMIE, true);

        });

        nazwisko.textProperty().addListener((ov, oldValue, newValue) -> {

            if(newValue.isEmpty()) warunki.set(NAZWISKO, false);
            else warunki.set(NAZWISKO, true);

        });

        numerTel.textProperty().addListener((ov, oldValue, newValue) -> {

            if(newValue.length() != 9) warunki.set(NUMER_TEL, false);
            else warunki.set(NUMER_TEL, true);

        });

    }

    public void init(Klient klient, Runnable refreshCallback) {

        imie.setText(klient.getImie());
        nazwisko.setText(klient.getNazwisko());
        numerTel.setText(String.valueOf(klient.getNumerTel()));

        this.klient = klient;
        this.refreshCallback = refreshCallback;

        try {

            Session sessions = MainApp.getSessionFactory().openSession();
            sessions.beginTransaction();

            Query<Wydanie> query = sessions.createQuery("FROM Wydanie w WHERE w.idKlienta = :id_klienta", Wydanie.class);
            query.setParameter("id_klienta", klient.getId());

            if(!query.list().isEmpty()) usun.setDisable(true);

            sessions.close();

        }catch(Exception m) {

            System.out.println(m.getMessage());

        }

    }

    @FXML
    private void change() {

        Klient tempKlient = new Klient(klient.getId(), imie.getText(), nazwisko.getText(), Integer.parseInt(numerTel.getText()));
        System.out.println(tempKlient.toString());

        EntityManagerFactory emf = null;
        EntityManager em = null;
        EntityTransaction transaction = null;

        try {

            emf = Persistence.createEntityManagerFactory("ewidencja");
            em = emf.createEntityManager();
            transaction = em.getTransaction();
            transaction.begin();

            Klient klientEdit = em.find(Klient.class, tempKlient.getId());
            klientEdit.setImie(tempKlient.getImie());
            klientEdit.setNazwisko(tempKlient.getNazwisko());
            klientEdit.setNumerTel(tempKlient.getNumerTel());

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
    private void back() {

        refreshCallback.run();
        edytujusunklienta.getScene().getWindow().hide();

    }

    @FXML
    private void delete() {

        Session session = MainApp.getSessionFactory().openSession();
        session.beginTransaction();

        session.remove((Klient) session.getReference(Klient.class, klient.getId()));

        try {

            session.getTransaction().commit();

        }catch(PersistenceException e) {

            Alert error = new Alert(AlertType.ERROR);
            error.setHeaderText("Błąd");
            error.setContentText("Nie można usunąć klienta!\n" + e.getMessage());
            error.showAndWait();
            return;

        }


        session.close();

        // }

        back();

    }

    /**
     * Uniemozliwa wpisywanie liter do textFielda do ktorego funkcja zostala przypisana.
     */
    @FXML
    private void validateTel(KeyEvent event) {

        TextField field = (TextField) event.getTarget();

        TextFormatter<String> textFormatter = new TextFormatter<>(change -> {

            if(!change.isContentChange()) { return change; }

            String text = change.getControlNewText();

            for(char c : text.toCharArray()) { if("1234567890".indexOf(c) == -1) return null; }

            return change;

        });

        field.setTextFormatter(textFormatter);

    }

    /**
     * Uniemozliwa wpisywanie ciagu znaku dluzszego niz 9 do przypisanego textFielda.
     */
    @FXML
    private void dlugoscTel(KeyEvent event) {

        final int maxLength = 9;

        TextField field = (TextField) event.getTarget();

        field.setOnKeyTyped(t -> {

            if(field.getText().length() > maxLength) {

                int pos = field.getCaretPosition();
                field.setText(field.getText(0, maxLength));
                field.positionCaret(pos);

            }

        });

    }

}
