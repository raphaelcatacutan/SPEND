package com.ssg.views.animations;

import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class SelfishAnimation {
    public static void animateNavigate(Node node, double x) {
        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.3), node);
        transition.setToX(x - 438);
        transition.setCycleCount(1);
        transition.play();
    }
}
