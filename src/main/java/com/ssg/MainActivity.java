package com.ssg;

import com.ssg.database.SpendBConnection;
import com.ssg.database.XamppServer;
import com.ssg.utils.ProgramUtils;
import com.ssg.utils.RuntimeData;
import com.ssg.views.ControllerUtils;
import com.ssg.views.SplashScreen;
import io.github.palexdev.materialfx.controls.MFXProgressBar;
import io.github.palexdev.materialfx.css.themes.MFXThemeManager;
import io.github.palexdev.materialfx.css.themes.Themes;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
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

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends Application {
    private Stage primaryStage;
    private Stage splashStage;
    private Scene mainScene;
    private double xOffset = 0;
    private double yOffset = 0;


    @Override
    public void start(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;
        this.splashStage = new Stage();

        loadFonts();
        SplashScreen splashController = loadSplashScene();
        splashStage.show();
        splashStage.setAlwaysOnTop(false);

        Task<Void> mainSceneTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                Thread.sleep(1000);
                loadConfigs();
                splashController.setProgress(0.2);
                Thread.sleep(3000);

                loadDatabase();
                splashController.setProgress(0.5);
                Thread.sleep(3000);

                loadMainScene();
                splashController.setProgress(1.0);
                Thread.sleep(3000);
                return null;
            }
        };

        mainSceneTask.setOnSucceeded(event -> {
            primaryStage.setScene(mainScene);
            primaryStage.initStyle(StageStyle.TRANSPARENT);
            primaryStage.setTitle("SPEND");

            primaryStage.getIcons().add(new Image(Objects.requireNonNull(MainActivity.class.getResourceAsStream("assets/icons/school-logo.png"))));
            primaryStage.setAlwaysOnTop(true);
            primaryStage.centerOnScreen();
            primaryStage.show();
            primaryStage.setAlwaysOnTop(false);
            splashStage.hide();
            splashStage.close();
        });

        ProgramUtils.callDelay(5, () -> new Thread(mainSceneTask).start());
    }


    public static void main(String[] args) {
        RuntimeData.CREATEDATABASE = Arrays.asList(args).contains("createDatabase");
        RuntimeData.MANAGEXAMPP = !Arrays.asList(args).contains("noXAMPP");
        RuntimeData.FILLDATA = !Arrays.asList(args).contains("noFill");
        RuntimeData.NOSPLASH = Arrays.asList(args).contains("noSplash");
        launch();
    }

    public SplashScreen loadSplashScene() throws IOException {
        FXMLLoader splashLoader = ControllerUtils.getLoader("splash-screen");
        AnchorPane splashRoot = splashLoader.load();
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
        splashStage.centerOnScreen();

        return splashLoader.getController();
    }
    public void loadMainScene() throws IOException {
        try {// FIXME Scene Size
            AnchorPane login = ControllerUtils.getLoader("main-login").load();
            AnchorPane view = ControllerUtils.getLoader("main-view").load();
            HBox toolbar = ControllerUtils.getLoader("toolbar").load();
            StackPane dialogs = ControllerUtils.getLoader("dialogs/dialog-box").load();

            StackPane.setAlignment(toolbar, Pos.TOP_LEFT);

            StackPane mainRoot = new StackPane();
            mainRoot.getChildren().addAll(dialogs, view, login, toolbar);
            mainRoot.setPrefSize(975.0, 671.0);
            toolbar.toFront();
            login.toFront();


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

            this.mainScene = mainScene;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void loadDatabase() {
        if (RuntimeData.STARTXAMPP && RuntimeData.MANAGEXAMPP) XamppServer.manage(true);
        SpendBConnection.intializeConnection();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (RuntimeData.MANAGEXAMPP && RuntimeData.STARTXAMPP) XamppServer.manage(false);
            File folder = new File(ProgramUtils.SPENDTEMP);
            if (!folder.exists() || !folder.isDirectory()) return;
            File[] files = folder.listFiles();
            if (files == null) return;
            for (File file : files) {
                if (!file.isFile()) continue;
                file.delete();
            }
            System.out.println("Application exiting...");
        }));
    }
    public void loadFonts() {
        // ENHANCE Make this an inline variable
        String fontsProperty = ProgramUtils.getProperty("load_resource", "fonts");
        String[] fonts = fontsProperty.split(",");
        final String DIRFONTS = "assets/fonts/";
        for (String font : fonts) {
            try {
                Font.loadFont(getClass().getResourceAsStream(DIRFONTS + font), 16);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void loadConfigs() {
        HashMap<String, Object> template = new HashMap<>();
        template.put("startXAMPP", true);
        template.put("xamppLocation", "c:\\xampp");
        File jsonFile = new File(ProgramUtils.CONFIGFILE);

        // Reading the json file
        if (jsonFile.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(jsonFile));
                String line;
                StringBuilder jsonContent = new StringBuilder();
                while ((line = reader.readLine()) != null) jsonContent.append(line);
                reader.close();
                String jsonString = jsonContent.toString();
                jsonString = jsonString.trim().substring(1, jsonString.length() - 1);
                String[] keyValuePairs = jsonString.split(",");
                for (String pair : keyValuePairs) {
                    String[] entry = pair.split(":");
                    String key = entry[0].trim().replace("\"", "");
                    String valueString = String.join(":", Arrays.copyOfRange(entry, 1, entry.length)).trim();
                    Object value;
                    if (valueString.startsWith("\"") && valueString.endsWith("\"")) value = valueString.substring(1, valueString.length() - 1);
                    else if (valueString.equalsIgnoreCase("true") || valueString.equalsIgnoreCase("false")) value = Boolean.parseBoolean(valueString);
                    else if (valueString.contains(".")) value = Double.parseDouble(valueString);
                    else value = Integer.parseInt(valueString);
                    template.put(key, value);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                File directory = new File(ProgramUtils.SPENDDATA);
                directory.mkdir();
                StringBuilder jsonContent = new StringBuilder();
                jsonContent.append("{");
                for (Map.Entry<String, Object> entry : template.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    jsonContent.append("\"").append(key).append("\":");
                    if (value instanceof String) jsonContent.append("\"").append(value).append("\"");
                    else jsonContent.append(value);
                    jsonContent.append(",");
                }
                if (jsonContent.charAt(jsonContent.length() - 1) == ',') jsonContent.deleteCharAt(jsonContent.length() - 1);
                jsonContent.append("}");
                BufferedWriter writer = new BufferedWriter(new FileWriter(jsonFile));
                writer.write(jsonContent.toString());
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Setting the data
        RuntimeData.STARTXAMPP = (boolean) template.get("startXAMPP");
        RuntimeData.XAMPPLOCATION = (String) template.get("xamppLocation");
    }
}