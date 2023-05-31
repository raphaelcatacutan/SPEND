package com.ssg.views.dialogs;

import com.google.common.eventbus.Subscribe;
import com.ssg.views.ControllerEvent;
import com.ssg.views.ControllerUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.Arrays;

public class DialogBox {
    @FXML private StackPane mainDialogBox;


    FXMLLoader messageLoader;

    DialogMessage messageController;

    AnchorPane messageDialog;

    public void initialize() throws IOException {
        ControllerUtils.EVENTBUS.register(this);

        messageLoader = ControllerUtils.getLoader("dialogs/dialog-message");

        messageDialog = messageLoader.load();

        messageController = messageLoader.getController();

        mainDialogBox.getChildren().addAll(messageDialog);
        mainDialogBox.getChildren().forEach((Node node) -> {
            node.setVisible(false);
            StackPane.setAlignment(node, Pos.CENTER);
        });
    }
    @Subscribe
    public void toggleDialog(ControllerEvent event) {
        switch (event.getEventId()) {
            case "showDialog" -> {
                mainDialogBox.toFront(); // In front of toolbar to prevent the user from logging out
                mainDialogBox.setVisible(true);
                String[] args = Arrays.stream(event.getSimpleArgs()).map(Object::toString).toArray(String[]::new);
                switch (args[0]) {
                    case "message" -> {
                        String[] mArgs = Arrays.copyOf(args, 6);
                        messageDialog.setVisible(true);
                        messageController.setData(mArgs[1], mArgs[2], mArgs[3], mArgs[4], mArgs[5]);
                    }
                }
            }
            case "hideDialog" -> {
                mainDialogBox.toBack();
                mainDialogBox.setVisible(false);
                ControllerUtils.triggerEvent((String) event.getSimpleArgs()[0]);
            }
        }
    }
}
