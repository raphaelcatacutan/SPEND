package com.ssg.views;

import io.github.palexdev.materialfx.controls.MFXProgressBar;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class SplashScreen {

    @FXML private AnchorPane anpSplashScreen;
    @FXML private MFXProgressBar pgbSplashProgress;
    @FXML private Label lblProgressStatus;


    public void initialize() {
        pgbSplashProgress.setAnimationSpeed(1.0);
    }

    public MFXProgressBar getPgbSplashProgress() {
        return pgbSplashProgress;
    }

    public Label getLblProgressStatus() {
        return lblProgressStatus;
    }
}
