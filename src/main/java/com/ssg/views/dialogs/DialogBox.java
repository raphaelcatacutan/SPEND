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

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;

public class DialogBox {
    @FXML private StackPane mainDialogBox;

    FXMLLoader messageLoader;
    FXMLLoader choiceLoader;

    AnchorPane messageDialog;
    AnchorPane choiceDialog;

    DialogMessage messageController;
    DialogChoice choiceController;

    public void initialize() throws IOException {
        ControllerUtils.EVENTBUS.register(this);

        messageLoader = ControllerUtils.getLoader("dialogs/dialog-message");
        choiceLoader = ControllerUtils.getLoader("dialogs/dialog-choice");

        messageDialog = messageLoader.load();
        choiceDialog = choiceLoader.load();

        messageController = messageLoader.getController();
        choiceController = choiceLoader.getController();

        mainDialogBox.getChildren().addAll(messageDialog, choiceDialog);
        mainDialogBox.getChildren().forEach((Node node) -> {
            node.setVisible(false);
            StackPane.setAlignment(node, Pos.CENTER);
        });
    }

    @Subscribe public void manageDialog(ControllerEvent event) {
        switch (event.getEventId()) {
            case "showDialog" -> {
                mainDialogBox.toFront(); // In front of toolbar to prevent the user from logging out
                mainDialogBox.setVisible(true);
                switch ((String) event.getSimpleArgs()[0]) {
                    case "message" -> {
                        /*
                         * Event Parameter Usage
                         * showDialog, message, title, description, ...buttons
                         */
                        String[] args = Arrays.stream(event.getSimpleArgs()).map(Object::toString).toArray(String[]::new);
                        String[] mArgs = Arrays.copyOf(args, 6);
                        messageDialog.setVisible(true);
                        messageController.setData(mArgs[1], mArgs[2], mArgs[3], mArgs[4]);
                    }
                    case "choice" -> {
                        /*
                         * Event Parameter Usage
                         * sArgs = choice, choiceType
                         * aArgs = selectedID, choicesModel
                         * showDialog, sArgs, aArgs
                         */
                        int[] selectedID = Arrays.stream(event.getArrayArgs()[0]).mapToInt(obj -> Integer.parseInt(obj.toString())).toArray();
                        choiceController.setData(selectedID, (String) event.getSimpleArgs()[1], event.getArrayArgs()[1]);
                        choiceDialog.setVisible(true);
                    }
                }
            }
            case "hideDialog" -> {
                mainDialogBox.toBack();
                mainDialogBox.setVisible(false);
                if (event.getSimpleArgs()[0] != null) ControllerUtils.triggerEvent((String) event.getSimpleArgs()[0]);

                messageDialog.setVisible(false);
                choiceDialog.setVisible(false);
                choiceController.clear();
            }
        }
    }
}
