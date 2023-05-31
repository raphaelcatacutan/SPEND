package com.ssg._test;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ChangeView extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        // Create the main window and set its title
        primaryStage.setTitle("Main Window");

        // Create the initial view
        Pane initialView = new Pane();
        initialView.setStyle("-fx-background-color: white");

        // Create a button that switches to a new view
        Button switchViewButton = new Button("Switch View");
        switchViewButton.setOnAction(event -> {
            // Create the new view
            Pane newView = new Pane();
            newView.setStyle("-fx-background-color: lightblue");

            // Set the new view as the root node of the scene
            primaryStage.getScene().setRoot(newView);
        });

        // Add the button to the initial view
        initialView.getChildren().add(switchViewButton);

        // Create the scene with the initial view as the root node
        Scene scene = new Scene(initialView, 400, 300);

        // Set the scene of the main window
        primaryStage.setScene(scene);

        // Show the main window
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
