package com.projektzaliczeniowy.projektzaliczeniowy.controllers;

import java.io.IOException;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import com.projektzaliczeniowy.projektzaliczeniowy.Klient;
import com.projektzaliczeniowy.projektzaliczeniowy.MainApp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ZarzadzajKlientamiViewController {

    @FXML
    private TableView<Klient> tabela;
    @FXML
    private TextField wyszukaj;
    private ObservableList<Klient> observableKlienci = FXCollections.observableArrayList();
    private ZarzadzajKlientamiViewController instance;

    public ZarzadzajKlientamiViewController() { instance = this; }

    @FXML
    private void initialize() {
        //tworzy kolumny
        TableColumn<Klient, Long> idKlienta = new TableColumn<>("id");
        TableColumn<Klient, String> imie = new TableColumn<>("imie");
        TableColumn<Klient, String> nazwisko = new TableColumn<>("nazwisko");
        TableColumn<Klient, Integer> numerTel = new TableColumn<>("numer_tel");
        //ustawia im wartosci
        idKlienta.setCellValueFactory(new PropertyValueFactory<>("id"));
        imie.setCellValueFactory(new PropertyValueFactory<>("imie"));
        nazwisko.setCellValueFactory(new PropertyValueFactory<>("nazwisko"));
        numerTel.setCellValueFactory(new PropertyValueFactory<>("numerTel"));

        tabela.getColumns().addAll(List.of(idKlienta, imie, nazwisko, numerTel));

        try {

            Session session = MainApp.getSessionFactory().openSession();
            session.beginTransaction();

            Query<Klient> query = session.createQuery("FROM Klient", Klient.class);
            List<Klient> klienci = (List<Klient>) query.list();

            session.close();

            observableKlienci.addAll(klienci);

        }catch(Exception e) {

            System.out.println(e.getMessage());

        }

        TableColumn<Klient, Void> usunEdytuj = new TableColumn<>("Edycja/Usuwanie");

        usunEdytuj.setCellFactory(param ->

        new TableCell<Klient, Void>() {

            private final Button btn = new Button("Edytuj/Usuń");

            {

                this.setAlignment(Pos.CENTER);

                btn.setOnAction(new EventHandler<ActionEvent>() {

                    /**
                     * Otwiera okienko do edycji danych wybranego klienta.
                     */

                    @Override
                    public void handle(ActionEvent event) {

                        final Stage editpop = new Stage();
                        editpop.initModality(Modality.APPLICATION_MODAL);
                        editpop.initOwner(tabela.getScene().getWindow());
                        editpop.setTitle("Klienci - Edytuj/Usuń");
                        editpop.setResizable(false);

                        try {

                            FXMLLoader loader = new FXMLLoader(getClass().getResource("edytuj_usun_klienta.fxml"));
                            Parent root = loader.load();
                            loader.<EdytujUsunKlientaViewController>getController().init(getTableView().getItems().get(getIndex()), instance::refreshTable);
                            editpop.setScene(new Scene(root));
                            editpop.show();

                        }catch(Exception e) {

                            e.printStackTrace();

                        }

                    }

                });

            }

            /**
             * Przypisuje przycisk Edytuj/Usuń do kazdego zapełnionengo wiersza tabeli.
             */
            @Override
            public void updateItem(Void item, boolean empty) {

                super.updateItem(item, empty);

                if(empty) {

                    setGraphic(null);

                }else {

                    setGraphic(btn);

                }

            }

        }

        );
        tabela.getColumns().add(usunEdytuj);

        FilteredList<Klient> filteredKlienci = new FilteredList<>(observableKlienci, b -> true);

        wyszukaj.textProperty().addListener((observableValue, oldValue, newValue) -> {

            filteredKlienci.setPredicate(klienci -> {

                if(newValue == null || newValue.isEmpty()) { return true; }

                String lowerCaseFilter = newValue.toLowerCase();

                if(klienci.getImie().toLowerCase().indexOf(lowerCaseFilter) != -1) {

                    return true;

                }else if(klienci.getNazwisko().toLowerCase().indexOf(lowerCaseFilter) != -1) {

                    return true;

                }else if(String.valueOf(klienci.getNumerTel()).toLowerCase().indexOf(lowerCaseFilter) != -1) {

                    return true;

                }else return false;

            });

        });
        //tworzy nową liste z wyszukanymi klientami

        SortedList<Klient> sortedKlienci = new SortedList<>(filteredKlienci);

        sortedKlienci.comparatorProperty().bind(tabela.comparatorProperty());

        tabela.setItems(sortedKlienci);

    }

    @FXML
    private void refreshTable() {

        observableKlienci.clear();

        try {

            Session session = MainApp.getSessionFactory().openSession();
            session.beginTransaction();

            Query<Klient> query = session.createQuery("FROM Klient", Klient.class);
            List<Klient> klienci = (List<Klient>) query.list();

            session.close();

            observableKlienci.addAll(klienci);

        }catch(Exception e) {

            System.out.println(e.getMessage());

        }

    }

    @FXML
    private void openDodaj() {

        final Stage editpop = new Stage();
        editpop.initModality(Modality.APPLICATION_MODAL);
        editpop.initOwner(tabela.getScene().getWindow());
        editpop.setTitle("Sprzęt - Dodaj");
        editpop.setResizable(false);

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("dodaj_klienta.fxml"));
            Parent root = loader.load();
            loader.<DodajKlientaViewController>getController().init(this::refreshTable);
            editpop.setScene(new Scene(root));
            editpop.show();

        }catch(Exception e) {

            e.printStackTrace();

        }

    }

    /**
     * Obsługuje przycisk służacy do zmiany sceny na scene zarządzania klientami.
     */
    @FXML
    private void switchToAdminView(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("admin.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();

    }

}
