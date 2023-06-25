package com.ssg.views.animations;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class AnimationUtils {

    public static void fadeOut(AnchorPane anchorPane, int durationMillis) {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(durationMillis), anchorPane);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.setOnFinished(event -> {
            anchorPane.setVisible(false);
            anchorPane.setOpacity(1.0);
            anchorPane.toBack();
        });
        fadeTransition.play();
    }
    public static void fadeIn(AnchorPane anchorPane, int durationMillis) {
        anchorPane.setOpacity(0.0);
        anchorPane.setVisible(true);

        PauseTransition delay = new PauseTransition(Duration.millis(200));
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(durationMillis), anchorPane);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);

        delay.setOnFinished(event -> fadeTransition.play());
        delay.play();
    }

    public static void slideAndFadeFromRightToLeft(AnchorPane anchorPane, int durationMillis) {
        anchorPane.setTranslateX(anchorPane.getWidth()); // Start position at the right edge of the scene

        TranslateTransition slideTransition = new TranslateTransition(Duration.millis(durationMillis / 2), anchorPane);
        slideTransition.setFromX(anchorPane.getWidth());
        slideTransition.setToX(anchorPane.getWidth() / 2);

        FadeTransition fadeTransition = new FadeTransition(Duration.millis(durationMillis / 2), anchorPane);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);

        ParallelTransition parallelTransition = new ParallelTransition(slideTransition, fadeTransition);
        parallelTransition.play();
    }
}
