package com.ssg.views.animations;

import javafx.animation.TranslateTransition;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class MainViewAnimation {
    public Label navDashboard;
    public Label navOfficers;
    public Label navStatistics;
    public Label navProjects;
    public Label navSettings;
    public ImageView navCircleIndicator;
    public StackPane stkView;

    public void animateNavigate(double x) {
        // FIXME Positioning
        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.3), navCircleIndicator);
        transition.setToX(x - 438);
        transition.setCycleCount(1);
        transition.play();
    }
}
