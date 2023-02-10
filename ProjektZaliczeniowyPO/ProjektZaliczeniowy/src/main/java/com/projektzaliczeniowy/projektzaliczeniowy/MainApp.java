package com.projektzaliczeniowy.projektzaliczeniowy;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * @author Daniel Ślęk
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    static SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

    public static void main(String[] args) {

        launch(args);

    }

    /**
     * Otwiera pierwszą scenę w projekcie.
     */
    public void start(Stage stage) {

        stage.setTitle("Ewidencja");

        try {

            Parent root = FXMLLoader.load(getClass().getResource("controllers/admin.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

        }catch(Exception e) {

            e.printStackTrace();

        }

    }

    public static SessionFactory getSessionFactory() {

        return sessionFactory;

    }

}
