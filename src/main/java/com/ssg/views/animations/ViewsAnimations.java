package com.ssg.views.animations;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class ViewsAnimations {
    public static void fadeOut(AnchorPane node) {
        if (!node.isVisible()) return;
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(200), node);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.setOnFinished(event -> {
            node.setVisible(false);
            node.setOpacity(1.0);
            node.toBack();
        });
        fadeTransition.play();
    }
    public static void fadeIn(AnchorPane node) {
        if (node.isVisible()) return;
        node.setOpacity(0.0);
        node.setVisible(true);
        node.toFront();

        PauseTransition delay = new PauseTransition(Duration.millis(200));
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(500), node);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);

        delay.setOnFinished(event -> fadeTransition.play());
        delay.play();
    }

    public static void showEditor(AnchorPane node) {
        TranslateTransition slideTransition = new TranslateTransition(Duration.seconds(0.7), node);
        node.getParent().setVisible(true);
        slideTransition.setFromX(621);
        slideTransition.setToX(0);
        slideTransition.setOnFinished(event -> {
            node.getParent().toFront();
            node.getParent().setVisible(true);
        });
        slideTransition.play();
    }

    public static void hideEditor(AnchorPane node) {
        TranslateTransition slideTransition = new TranslateTransition(Duration.seconds(0.6), node);
        slideTransition.setOnFinished(event -> node.getParent().setVisible(false));
        slideTransition.setFromX(0);
        slideTransition.setToX(621);
        slideTransition.play();
    }

    public static void focusModel(AnchorPane node) {
        if (node.isVisible()) return;
        node.setVisible(true);

        TranslateTransition slideTransition = new TranslateTransition(Duration.seconds(0.5), node);
        slideTransition.setFromY(node.getHeight());
        slideTransition.setToY(0);
        slideTransition.play();
    }

    public static void unfocusModel(AnchorPane node) {
        if (!node.isVisible()) return;
        TranslateTransition slideTransition = new TranslateTransition(Duration.seconds(0.5), node);
        slideTransition.setFromY(0);
        slideTransition.setToY(node.getHeight());
        slideTransition.setOnFinished(event -> node.setVisible(false));
        slideTransition.play();
    }

    public static void showDialog(AnchorPane node) {
        node.setOpacity(0);

        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.25), node);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);

        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.25), node);
        scaleTransition.setFromX(0);
        scaleTransition.setFromY(0);
        scaleTransition.setToX(1);
        scaleTransition.setToY(1);

        fadeTransition.play();
        scaleTransition.play();

        node.setVisible(true);
    }

    public static void hideDialog(AnchorPane node) {
        FadeTransition fadeOutTransition = new FadeTransition(Duration.seconds(0.25), node);
        fadeOutTransition.setFromValue(1);
        fadeOutTransition.setToValue(0);

        ScaleTransition scaleOutTransition = new ScaleTransition(Duration.seconds(0.25), node);
        scaleOutTransition.setFromX(1);
        scaleOutTransition.setFromY(1);
        scaleOutTransition.setToX(0);
        scaleOutTransition.setToY(0);

        scaleOutTransition.setOnFinished(event -> {
            node.setVisible(false);
            node.getParent().toBack();
            node.getParent().setVisible(false);
        });

        fadeOutTransition.play();
        scaleOutTransition.play();
    }
}
