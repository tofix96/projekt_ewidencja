package com.projektzaliczeniowy.projektzaliczeniowy.controllers;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import com.projektzaliczeniowy.projektzaliczeniowy.MainApp;
import com.projektzaliczeniowy.projektzaliczeniowy.Sprzet;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ZarzadzajSprzetemViewController {

    @FXML
    private TableView<Sprzet> tabela;
    @FXML
    private TextField wyszukaj;
    private ObservableList<Sprzet> observableSprzet = FXCollections.observableArrayList();
    private List<Wydanie> wydania;
    private final ZarzadzajSprzetemViewController instance;

    public ZarzadzajSprzetemViewController() { instance = this; }

    /**
     *
     */
    @FXML
    private void initialize() {

        TableColumn<Sprzet, Long> idSprzetu = new TableColumn<>("id");
        TableColumn<Sprzet, String> kategoria = new TableColumn<>("kategoria");
        TableColumn<Sprzet, String> model = new TableColumn<>("model");
        TableColumn<Sprzet, String> opis = new TableColumn<>("opis");
        TableColumn<Sprzet, Void> wydany = new TableColumn<>("wydany");
        TableColumn<Sprzet, Void> usunEdytuj = new TableColumn<>("Edycja/Usuwanie");

        idSprzetu.setCellValueFactory(new PropertyValueFactory<>("id"));
        kategoria.setCellValueFactory(new PropertyValueFactory<>("kategoria"));
        model.setCellValueFactory(new PropertyValueFactory<>("model"));
        opis.setCellValueFactory(new PropertyValueFactory<>("opis"));


        wydany.setCellFactory(param -> new TableCell<Sprzet, Void>() {

            private final CheckBox checkBox = new CheckBox();
            {

                this.setAlignment(Pos.CENTER);

            }
            @Override
            public void updateItem(Void item, boolean empty) {

                super.updateItem(item, empty);

                if(empty) {

                    setGraphic(null);

                }else {

                    checkBox.setAlignment(Pos.CENTER);
                    checkBox.setMouseTransparent(true);
                    checkBox.setSelected(wydania.stream().anyMatch(w -> w.getIdSprzetu().equals(getTableView().getItems().get(getIndex()).getId()) && w.getDataZwrotu() == null));

                    setGraphic(checkBox);

                }

            }

        }

        );
        usunEdytuj.setCellFactory(param ->

        new TableCell<Sprzet, Void>() {

            private final Button button = new Button("Edytuj/Usuń");

            {

                this.setAlignment(Pos.CENTER);

                button.setOnAction(new EventHandler<ActionEvent>() {

                    /**
                     * Otwiera okienko do edycji danych wybranego sprzętu.
                     */
                    @Override
                    public void handle(ActionEvent event) {

                        final Stage editpop = new Stage();
                        editpop.initModality(Modality.APPLICATION_MODAL);
                        editpop.initOwner(tabela.getScene().getWindow());
                        editpop.setTitle("Sprzęt - Edytuj/Usuń");
                        editpop.setResizable(false);

                        try {

                            FXMLLoader loader = new FXMLLoader(getClass().getResource("edytuj_usun_sprzet.fxml"));
                            Parent root = loader.load();
                            loader.<EdytujUsunSprzetViewController>getController().init(getTableView().getItems().get(getIndex()), instance::refreshTable);
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

        tabela.getColumns().addAll(List.of(idSprzetu, kategoria, model, opis, wydany, usunEdytuj));

        try {

            Session session = MainApp.getSessionFactory().openSession();
            session.beginTransaction();

            Query<Sprzet> query1 = session.createQuery("FROM Sprzet", Sprzet.class);
            List<Sprzet> sprzet = (List<Sprzet>) query1.list();

            Query<Wydanie> query2 = session.createQuery("FROM Wydanie", Wydanie.class);
            wydania = new ArrayList<>((List<Wydanie>) query2.list());

            session.close();

            observableSprzet.addAll(sprzet);

        }catch(Exception e) {

            System.out.println(e.getMessage());

        }

        FilteredList<Sprzet> filteredSprzet = new FilteredList<>(observableSprzet, b -> true);

        wyszukaj.textProperty().addListener((observableValue, oldValue, newValue) -> {

            filteredSprzet.setPredicate(sprzet -> {

                if(newValue == null || newValue.isEmpty()) { return true; }

                String lowerCaseFilter = newValue.toLowerCase();

                if(sprzet.getModel().toLowerCase().indexOf(lowerCaseFilter) != -1) {

                    return true;

                }else if(sprzet.getOpis().toLowerCase().indexOf(lowerCaseFilter) != -1) {

                    return true;

                }else if(sprzet.getKategoria().toLowerCase().indexOf(lowerCaseFilter) != -1) {

                    return true;

                }else return false;

            });

        });

        SortedList<Sprzet> sortedSprzet = new SortedList<>(filteredSprzet);

        sortedSprzet.comparatorProperty().bind(tabela.comparatorProperty());

        tabela.setItems(sortedSprzet);

    }

    @FXML
    private void openDodaj(ActionEvent event) {

        final Stage editpop = new Stage();
        editpop.initModality(Modality.APPLICATION_MODAL);
        editpop.initOwner(tabela.getScene().getWindow());
        editpop.setTitle("Sprzęt - Dodaj");
        editpop.setResizable(false);

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("dodaj_sprzet.fxml"));
            Parent root = loader.load();
            loader.<DodajSprzetViewController>getController().init(this::refreshTable);
            editpop.setScene(new Scene(root));
            editpop.show();

        }catch(Exception e) {

            e.printStackTrace();

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

    @FXML
    private void refreshTable() {

        observableSprzet.clear();

        try {

            Session session = MainApp.getSessionFactory().openSession();
            session.beginTransaction();

            Query<Sprzet> query = session.createQuery("FROM Sprzet", Sprzet.class);
            List<Sprzet> sprzet = (List<Sprzet>) query.list();

            session.close();

            observableSprzet.addAll(sprzet);

        }catch(Exception e) {

            System.out.println(e.getMessage());

        }

    }

}
