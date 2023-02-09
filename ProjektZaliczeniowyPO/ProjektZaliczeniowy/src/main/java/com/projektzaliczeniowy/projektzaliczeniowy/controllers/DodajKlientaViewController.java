package com.projektzaliczeniowy.projektzaliczeniowy.controllers;

import com.projektzaliczeniowy.projektzaliczeniowy.Klient;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

public class DodajKlientaViewController {

    //klawisz dodaj żeby się podświetlał, indexy dla tej listy poniżej
    private final int IMIE = 0;
    private final int NAZWISKO = 1;
    private final int NUMER_TEL = 2;
    @FXML
    private VBox glowny;
    @FXML
    private TextField imie;
    @FXML
    private TextField nazwisko;
    @FXML
    private TextField numerTel;
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
        // warunki sprawdzające abyu nie dodać klienta bez danych (przycisk DODAJ podświelti się dopiero gdy będzie wszystko wprowadzone)
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

    //tworzenie nowego klienta dodawanie do bazy danych i napis w konsoli
    @FXML
    private void add() {

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("ewidencja");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();
        entityManager.persist(new Klient(null, imie.getText(), nazwisko.getText(), Integer.parseInt(numerTel.getText())));
        entityManager.getTransaction().commit();

        entityManager.close();
        entityManagerFactory.close();

        System.out.println("dodano klienta");

        refreshCallback.run();

        back();

    }

    //cofanie sceny
    @FXML
    private void back() {

        refreshCallback.run();
        glowny.getScene().getWindow().hide();

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
