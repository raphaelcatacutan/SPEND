package com.ssg;

import com.ssg.database.XamppServer;
import com.ssg.utils.RuntimeData;
import com.ssg.views.ControllerUtils;
import com.ssg.database.SpendBConnection;
import com.ssg.utils.ProgramUtils;
import io.github.palexdev.materialfx.css.themes.MFXThemeManager;
import io.github.palexdev.materialfx.css.themes.Themes;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

public class MainActivity extends Application {
    private Stage primaryStage;
    private Stage splashStage;
    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void start(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;
        this.splashStage = new Stage();

        // TODO Animations
        // TODO Remove the Printouts
        // TODO Database failed handler.
        loadDatabase();
        loadFonts();
        loadSplashScene();
        loadMainScene();

        splashStage.show();
        splashStage.setAlwaysOnTop(false);
        // Gives time for the splash screen
        KeyFrame loadResource = new KeyFrame(Duration.seconds(16), event -> {
            Platform.runLater(() -> {
                primaryStage.show();
                primaryStage.setAlwaysOnTop(false);
                splashStage.hide();
                splashStage.close();
            });
        });
        new Timeline(loadResource).play();
    }

    public static void main(String[] args) {
        RuntimeData.CREATEDATABASE = Arrays.asList(args).contains("createDatabase");
        RuntimeData.MANAGEXAMPP = Arrays.asList(args).contains("manageXampp");
        launch();
    }

    public void loadSplashScene() throws IOException {
        AnchorPane splashRoot = ControllerUtils.getLoader("splash-screen").load();
        splashRoot.setPrefSize(550.0, 309.0);

        // Add the arc on the window's border
        Rectangle splashRect = new Rectangle(splashRoot.getPrefWidth(), splashRoot.getPrefHeight());
        splashRect.setArcHeight(40.0);
        splashRect.setArcWidth(40.0);
        splashRoot.setClip(splashRect);

        // Set Dragging Position
        splashRoot.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        splashRoot.setOnMouseDragged(event -> {
            splashStage.setX(event.getScreenX() - xOffset);
            splashStage.setY(event.getScreenY() - yOffset);
        });
        Scene splashScene = new Scene(splashRoot);
        splashScene.setFill(Color.TRANSPARENT);

        MFXThemeManager.addOn(splashScene, Themes.DEFAULT, Themes.LEGACY);
        splashStage.setScene(splashScene);
        splashStage.initStyle(StageStyle.TRANSPARENT);
        splashStage.setTitle("SPEND");
        splashStage.getIcons().add(new Image(Objects.requireNonNull(MainActivity.class.getResourceAsStream("assets/icons/school-logo.png"))));
        splashStage.centerOnScreen();
        splashStage.setAlwaysOnTop(true);
    }
    public void loadMainScene() throws IOException {
        // FIXME Scene Size
        AnchorPane login = ControllerUtils.getLoader("main-login").load();
        AnchorPane view = ControllerUtils.getLoader("main-view").load();
        HBox toolbar = ControllerUtils.getLoader("toolbar").load();
        StackPane dialogs = ControllerUtils.getLoader("dialogs/dialog-box").load();

        StackPane.setAlignment(toolbar, Pos.TOP_LEFT);

        StackPane mainRoot = new StackPane();
        mainRoot.getChildren().addAll(dialogs, view, login, toolbar);
        mainRoot.setPrefSize(975.0, 671.0);
        toolbar.toFront();

        // Set Dragging Position
        toolbar.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        toolbar.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - xOffset);
            primaryStage.setY(event.getScreenY() - yOffset);
        });

        // Add the arc on the window's border
        Rectangle mainRect = new Rectangle(mainRoot.getPrefWidth(), mainRoot.getPrefHeight());
        mainRect.setArcHeight(40.0);
        mainRect.setArcWidth(40.0);
        mainRoot.setClip(mainRect);

        Scene mainScene = new Scene(mainRoot);
        mainScene.setFill(Color.TRANSPARENT);

        MFXThemeManager.addOn(mainScene, Themes.DEFAULT, Themes.LEGACY);
        primaryStage.setScene(mainScene);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setTitle("SPEND");
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(MainActivity.class.getResourceAsStream("assets/icons/school-logo.png"))));
        primaryStage.centerOnScreen();
        primaryStage.setAlwaysOnTop(true);
    }
    public void loadDatabase() {
        if (RuntimeData.MANAGEXAMPP) XamppServer.manage(true);
        SpendBConnection.intializeConnection();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Application exiting...");
            if (RuntimeData.MANAGEXAMPP) XamppServer.manage(false);
        }));
    }
    public void loadFonts() {
        String fontsProperty = ProgramUtils.getProperty("load_resource", "fonts");
        String[] fonts = fontsProperty.split(",");
        final String DIRFONTS = "assets/fonts/";
        for (String font: fonts) {
            try {
                Font.loadFont(getClass().getResourceAsStream(DIRFONTS + font), 16);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}