package com.ssg._test;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class FadeOutTransition extends Application {

    @Override
    public void start(Stage primaryStage) {
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setStyle("-fx-background-color: blue;");

        Button fadeOutButton = new Button("Fade Out");
        fadeOutButton.setOnAction(event -> fadeOut(anchorPane, 1000));

        anchorPane.getChildren().add(fadeOutButton);

        Scene scene = new Scene(anchorPane, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void fadeOut(AnchorPane anchorPane, int durationMillis) {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(durationMillis), anchorPane);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.setOnFinished(event -> {
            anchorPane.setVisible(false);
            anchorPane.setOpacity(1.0);
        });
        fadeTransition.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
