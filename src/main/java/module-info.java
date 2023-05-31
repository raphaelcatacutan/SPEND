module com.ssg {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires mysql.connector.j;
    requires java.desktop;
    requires javafx.media;
    requires com.google.common;
    requires MaterialFX;
    requires jasperreports;
    requires javafx.swing;
    requires eu.hansolo.fx.charts;

    opens com.ssg to javafx.fxml;
    exports com.ssg;
    exports com.ssg.views;
    exports com.ssg.database.models;
    opens com.ssg.views to javafx.fxml;
    exports com.ssg.views.templates;
    opens com.ssg.views.templates to javafx.fxml;
    exports com.ssg.views.dialogs;
    opens com.ssg.views.dialogs to javafx.fxml;
    exports com.ssg._test;
    opens com.ssg._test to javafx.fxml;
}