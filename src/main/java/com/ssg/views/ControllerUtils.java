package com.ssg.views;

import com.google.common.eventbus.EventBus;
import com.ssg.MainActivity;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URISyntaxException;
import java.sql.Blob;
import java.util.Objects;

public class ControllerUtils {

    public static final EventBus EVENTBUS = new EventBus();
    public static final Image DEFAULTAVATAR;

    static {
        try {
            DEFAULTAVATAR = new Image(Objects.requireNonNull(MainActivity.class.getResource("assets/icons/school-logo.png")).toURI().toString());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static Stage getStage(Node node) {
        return (Stage) node.getScene().getWindow();
    }
    public static FXMLLoader getLoader(String path) {
        return new FXMLLoader(MainActivity.class.getResource("views/" + path + ".fxml"));
    }
    public static Image loadBlob(Blob blob) {
        try {
            return new Image(new ByteArrayInputStream(blob.getBytes(1, (int) blob.length())));
        } catch (Exception e) {
            return DEFAULTAVATAR;
        }
    }
    public static void triggerEvent(String eventID) {
        EVENTBUS.post(new ControllerEvent(eventID));
    }
    public static void triggerEvent(String eventID, Object... args) {
        EVENTBUS.post(new ControllerEvent(eventID, args));
    }
    public static void triggerEvent(String eventID, Object[]... args) {
        EVENTBUS.post(new ControllerEvent(eventID, args));
    }


}
