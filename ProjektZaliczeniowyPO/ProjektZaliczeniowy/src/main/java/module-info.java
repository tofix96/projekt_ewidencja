module com.projektzaliczeniowy.projektzaliczeniowy {

    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires transitive javafx.graphics;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    // requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.naming;
    requires java.sql;
    requires jakarta.persistence;
    requires transitive org.hibernate.orm.core;
    requires junit;

    opens com.projektzaliczeniowy.projektzaliczeniowy to javafx.fxml, org.hibernate.orm.core;
    opens com.projektzaliczeniowy.projektzaliczeniowy.controllers to javafx.fxml, org.hibernate.orm.core;

    exports com.projektzaliczeniowy.projektzaliczeniowy;

}
