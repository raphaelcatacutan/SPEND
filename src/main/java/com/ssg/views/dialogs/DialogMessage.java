package com.ssg.views.dialogs;

import com.ssg.views.ControllerUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class DialogMessage {
    @FXML private AnchorPane anpDialogMessage;
    @FXML private Label lblDialogMessageTitle;
    @FXML private Label lblDialogMessageDescription;
    @FXML private Button btnDialogMessageButton1;
    @FXML private Button btnDialogMessageButton2;

    private Button[] dialogButtons;

    public void initialize() {
        dialogButtons = new Button[]{
                btnDialogMessageButton1,
                btnDialogMessageButton2
        };
        resetDialog();
    }
    private void resetDialog() {
        anpDialogMessage.setVisible(false);
        for (Button x: dialogButtons) {
            x.setVisible(false);
            x.setOnMouseClicked(null);
        }
    }
    public void setData(String title, String description, String... buttons) {
        lblDialogMessageTitle.setText(title);
        lblDialogMessageDescription.setText(description);

        for (int x = 0; x < buttons.length; x++) {
            String textButton = buttons[x];
            if (textButton == null || textButton.isEmpty()) return;
            dialogButtons[x].setText(textButton);
            dialogButtons[x].setVisible(true);
            dialogButtons[x].setOnMouseClicked(event -> {
                ControllerUtils.triggerEvent("hideDialog", textButton);
                resetDialog();
            });
        }
    }

}
