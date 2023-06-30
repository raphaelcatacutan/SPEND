package com.ssg.views;

import com.google.common.eventbus.EventBus;
import com.ssg.MainActivity;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.net.URISyntaxException;
import java.util.Objects;
import java.util.function.UnaryOperator;

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

    public static TextFormatter<String> textFormatter(int maxLength, boolean restrictToNumbers) {
        UnaryOperator<TextFormatter.Change> lengthFilter = change -> {
            if (change.isContentChange()) {
                int newLength = change.getControlNewText().length();
                if (newLength > maxLength) {
                    int replaceLength = change.getControlText().length();
                    int removeLength = newLength - maxLength;
                    try {
                        change.setText(change.getText().substring(0, maxLength - replaceLength));
                    } catch (Exception ignored) {}
                    try {
                        change.setRange(change.getRangeStart(), change.getRangeStart() + removeLength);
                    } catch (Exception ignored) {}
                    try {
                        change.setCaretPosition(change.getCaretPosition() - removeLength);
                    } catch (Exception ignored) {}
                }
            }
            if (restrictToNumbers) {
                if (!change.getControlNewText().matches("-?\\d*\\.?\\d*")) {
                    change.setText("");
                }
            }
            return change;
        };
        return new TextFormatter<>(lengthFilter);
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
    public static void triggerEvent(String eventID, Object[] sArgs, Object[][] aArgs) {
        ControllerEvent event = new ControllerEvent(eventID);
        event.setArrayArgs(aArgs);
        event.setSimpleArgs(sArgs);
        EVENTBUS.post(event);
    }


}
