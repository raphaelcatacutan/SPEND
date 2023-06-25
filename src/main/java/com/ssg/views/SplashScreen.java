package com.ssg.views;

import io.github.palexdev.materialfx.controls.MFXProgressBar;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class SplashScreen {

    @FXML private AnchorPane anpSplashScreen;
    @FXML private MFXProgressBar pgbSplashProgress;


    public void initialize() {
        pgbSplashProgress.setAnimationSpeed(1.0);
    }

    public void setProgress(double progress) {
        pgbSplashProgress.setProgress(progress);
    }
}
