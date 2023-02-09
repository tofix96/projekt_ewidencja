package com.projektzaliczeniowy.projektzaliczeniowy.controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.projektzaliczeniowy.projektzaliczeniowy.Sprzet;
import org.hibernate.Session;
import org.hibernate.query.Query;

import com.projektzaliczeniowy.projektzaliczeniowy.MainApp;
import com.projektzaliczeniowy.projektzaliczeniowy.Wydanie;

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

public class ZarzadzajWydaniamiViewController {

    @FXML
    private TableView<Wydanie> tabela;
    @FXML
    private TextField wyszukaj;
    private ObservableList<Wydanie> observableWydania = FXCollections.observableArrayList();
    //
    private ZarzadzajWydaniamiViewController instance;

    public ZarzadzajWydaniamiViewController() { instance = this; }

    @FXML
    private void initialize() {

        TableColumn<Wydanie, Long> idWydania = new TableColumn<>("id");
        TableColumn<Wydanie, Long> idSprzetu = new TableColumn<>("id_sprzetu");
        TableColumn<Wydanie, Long> idKlienta = new TableColumn<>("id_klienta");
        TableColumn<Wydanie, LocalDate> dataWydania = new TableColumn<>("data_wydania");
        TableColumn<Wydanie, LocalDate> dataZwrotu = new TableColumn<>("data_zwrotu");

        idWydania.setCellValueFactory(new PropertyValueFactory<>("id"));
        idSprzetu.setCellValueFactory(new PropertyValueFactory<>("idSprzetu"));
        idKlienta.setCellValueFactory(new PropertyValueFactory<>("idKlienta"));
        dataWydania.setCellValueFactory(new PropertyValueFactory<>("dataWydania"));
        dataZwrotu.setCellValueFactory(new PropertyValueFactory<>("dataZwrotu"));

        tabela.getColumns().addAll(List.of(idWydania, idSprzetu, idKlienta, dataWydania, dataZwrotu));

        try {

            Session session = MainApp.getSessionFactory().openSession();
            session.beginTransaction();

            Query<Wydanie> query = session.createQuery("FROM Wydanie", Wydanie.class);

            List<Wydanie> wydaniaList = (List<Wydanie>) query.list();

            System.out.println(wydaniaList);

            session.close();

            observableWydania.addAll(wydaniaList);

        }catch(Exception e) {

            System.out.println(e.getMessage());

        }

        TableColumn<Wydanie, Void> usunEdytuj = new TableColumn<>("Edycja/Usuwanie");

        usunEdytuj.setCellFactory(param ->

        new TableCell<Wydanie, Void>() {

            private final Button button = new Button("Edytuj/Usuń");

            {

                this.setAlignment(Pos.CENTER);

                button.setOnAction(new EventHandler<ActionEvent>() {

                    /**
                     * Otwiera okienko do edycji danych wybranych wypożyczeń.
                     */
                    @Override
                    public void handle(ActionEvent event) {

                        final Stage editpop = new Stage();
                        editpop.initModality(Modality.APPLICATION_MODAL);
                        editpop.initOwner(tabela.getScene().getWindow());
                        editpop.setTitle("Wydanie - Edytuj/Usuń");
                        editpop.setResizable(false);

                        try {

                            FXMLLoader loader = new FXMLLoader(getClass().getResource("edytuj_usun_wydanie.fxml"));
                            Parent root = loader.load();
                            loader.<EdytujUsunWydanieViewController>getController().init(getTableView().getItems().get(getIndex()), instance::refreshTable);
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

                    setGraphic(button);

                }

            }

        }

        );
        tabela.getColumns().add(usunEdytuj);

        FilteredList<Wydanie> filteredWydania = new FilteredList<>(observableWydania, b -> true);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        wyszukaj.textProperty().addListener((observableValue, oldValue, newValue) -> {

            filteredWydania.setPredicate(wydanie -> {

                if(newValue == null || newValue.isEmpty()) { return true; }

                String lowerCaseFilter = newValue.toLowerCase();

                if(formatter.format(wydanie.getDataWydania()).toLowerCase().indexOf(lowerCaseFilter) != -1) {

                    return true;

                }else if(wydanie.getDataZwrotu() != null && formatter.format(wydanie.getDataZwrotu()).toLowerCase().indexOf(lowerCaseFilter) != -1) {

                    return true;

                }else return false;

            });

        });

        SortedList<Wydanie> sortedWydania = new SortedList<>(filteredWydania);

        sortedWydania.comparatorProperty().bind(tabela.comparatorProperty());

        tabela.setItems(sortedWydania);

    }

    @FXML
    private void openDodaj(ActionEvent event) {

        final Stage editpop = new Stage();
        editpop.initModality(Modality.APPLICATION_MODAL);
        editpop.initOwner(tabela.getScene().getWindow());
        editpop.setTitle("Wydanie - Dodaj");
        editpop.setResizable(false);

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("dodaj_wydanie.fxml"));
            Parent root = loader.load();
            loader.<DodajWydanieViewController>getController().init(this::refreshTable);
            editpop.setScene(new Scene(root));
            editpop.show();

        }catch(Exception e) {

            e.printStackTrace();

        }

    }

    @FXML
    private void refreshTable() {

        observableWydania.clear();

        try {

            Session session = MainApp.getSessionFactory().openSession();
            session.beginTransaction();

            Query<Wydanie> query = session.createQuery("FROM Wydanie", Wydanie.class);
            List<Wydanie> wydania = (List<Wydanie>) query.list();

            session.close();

            observableWydania.addAll(wydania);

        }catch(Exception e) {

            System.out.println(e.getMessage());

        }

    }

    @FXML
    private void switchToAdminView(ActionEvent event) {

        try {

            Parent root = FXMLLoader.load(getClass().getResource("admin.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        }catch(Exception e) {

            e.printStackTrace();

        }

    }

}
