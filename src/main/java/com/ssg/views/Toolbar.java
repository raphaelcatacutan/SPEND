package com.ssg.views;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Toolbar {
    @FXML Pane pneExit;
    @FXML Pane pneLogout;
    @FXML Pane pneMinimize;

    public void initialize() {
        pneExit.setOnMouseClicked(event -> exitApplication());
        pneLogout.setOnMouseClicked(event -> MainEvents.signOut());
        pneMinimize.setOnMouseClicked(event -> minimize());
    }

    public void exitApplication() {
        Stage stage = ControllerUtils.getStage(pneExit);
        stage.hide();
        System.exit(0);
    }

    public void minimize() {
        Stage stage = ControllerUtils.getStage(pneMinimize);
        stage.setIconified(true);
    }

}
