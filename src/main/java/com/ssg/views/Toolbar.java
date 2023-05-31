package com.ssg.views;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class Toolbar {
    @FXML ImageView imgExit;
    @FXML ImageView imgLogOut;
    @FXML ImageView imgMinimize;

    public void initialize() {
        imgExit.setOnMouseClicked(event -> exitApplication());
        imgLogOut.setOnMouseClicked(event -> signOut());
        imgMinimize.setOnMouseClicked(event -> minimize());
    }

    public void exitApplication() {
        if (!workPrompt()) return;
        Stage stage = ControllerUtils.getStage(imgExit);
        stage.hide();
        System.exit(0);
    }

    public void signOut() {
        if (!workPrompt()) return;
        // TODO Reset GUI
        // TODO Show the Login Screen
    }

    public void minimize() {
        Stage stage = ControllerUtils.getStage(imgMinimize);
        stage.setIconified(true);
    }

    public boolean workPrompt() {
        return true;
        // TODO Show Prompt
        // Want to stop XAMPP?
    }
}
