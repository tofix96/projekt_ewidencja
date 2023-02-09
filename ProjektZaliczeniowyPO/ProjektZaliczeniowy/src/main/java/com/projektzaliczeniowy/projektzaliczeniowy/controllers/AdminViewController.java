package com.projektzaliczeniowy.projektzaliczeniowy.controllers;

import java.io.IOException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AdminViewController {

    /**
     * Obsługuje przycisk służacy do zmiany sceny na scene zarządzania wydaniami.
     */

    @FXML
    private void switchToZarzadzanieWydaniamiView(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("zarzadzaj_wydaniami.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setResizable(false);
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();

    }

    /**
     * Obsługuje przycisk służacy do zmiany sceny na scene zarządzania klientami.
     */
    @FXML
    private void switchToZarzadzanieKlientamiView(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("zarzadzaj_klientami.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setResizable(false);
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();

    }

    /**
     * Obsługuje przycisk służacy do zmiany sceny na scene zarządzania sprzętem.
     */
    @FXML
    private void switchToZarzadzanieSprzetemView(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("zarzadzaj_sprzetem.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setResizable(false);
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();

    }

    @FXML
    private void exitProgram(ActionEvent event) throws IOException { Platform.exit(); }

}
