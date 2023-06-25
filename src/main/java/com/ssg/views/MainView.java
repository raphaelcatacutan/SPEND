package com.ssg.views;

import com.google.common.eventbus.Subscribe;
import com.ssg.database.SpendBUtils;
import com.ssg.database.models.Officer;
import com.ssg.database.models.Project;
import com.ssg.utils.MethodArgument;
import com.ssg.utils.ProgramUtils;
import com.ssg.views.animations.AnimationUtils;
import com.ssg.views.animations.MainViewAnimation;
import io.github.palexdev.materialfx.controls.MFXProgressBar;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.io.IOException;
import java.util.HashMap;

import static com.ssg.views.ControllerUtils.triggerEvent;

public class MainView extends MainViewAnimation {
    /**
     * Contains information about the views
     * Keys contains the Navigator Labels
     * Values contains an Object array [AnchorPane View, Controller]
     */
    private final HashMap<Label, Object[]> programViews = new HashMap<>();
    // Navigation
    @FXML private Label navDashboard;
    @FXML private Label navOfficers;
    @FXML private Label navStatistics;
    @FXML private Label navProjects;
    @FXML private Label navSettings;
    @FXML private ImageView navCircleIndicator;
    @FXML private MFXProgressBar pgbRefreshing;

    @FXML private StackPane stkView;

    @FXML private Label focusedPane = null;

    public void initialize() throws IOException {
        ControllerUtils.EVENTBUS.register(this);
        // Set up the Views
        FXMLLoader dashboardLoader = ControllerUtils.getLoader("view-dashboard");
        FXMLLoader officersLoader = ControllerUtils.getLoader("view-officers");
        FXMLLoader statisticsLoader = ControllerUtils.getLoader("view-statistics");
        FXMLLoader projectsLoader = ControllerUtils.getLoader("view-projects");
        FXMLLoader settingsLoader = ControllerUtils.getLoader("view-settings");

        AnchorPane dashboardViewer = dashboardLoader.load();
        AnchorPane officersViewer = officersLoader.load();
        AnchorPane statisticsViewer = statisticsLoader.load();
        AnchorPane projectsViewer = projectsLoader.load();
        AnchorPane settingsViewer = settingsLoader.load();

        ViewDashboard dashboardController = dashboardLoader.getController();
        ViewOfficers officersController = officersLoader.getController();
        ViewStatistics statisticsController = statisticsLoader.getController();
        ViewProjects projectsController = projectsLoader.getController();
        ViewSettings settingsController = settingsLoader.getController();


        stkView.getChildren().addAll(officersViewer, projectsViewer, statisticsViewer, settingsViewer, dashboardViewer);
        // Storage for Navigators
        programViews.put(navDashboard, new Object[] {dashboardViewer, dashboardController, 442.0});
        programViews.put(navOfficers, new Object[] {officersViewer, officersController, 551.0});
        programViews.put(navProjects, new Object[] {projectsViewer, projectsController, 649.0});
        programViews.put(navStatistics, new Object[] {statisticsViewer, statisticsController, 748.0});
        programViews.put(navSettings, new Object[] {settingsViewer, settingsController, 846.0});
        // Listener for Navigator
        for (Label navigator: programViews.keySet()) {
            ((AnchorPane) programViews.get(navigator)[0]).setVisible(false);
            navigator.setOnMouseClicked(event -> navigate(navigator));
        }
        triggerEvent("refreshViews");
    }
    public void navigate(Label navigator) {
        AnchorPane lastFocus = null;
        if (focusedPane != navigator) {
            if (focusedPane != null) lastFocus = (AnchorPane) programViews.get(focusedPane)[0];
            if (lastFocus != null) AnimationUtils.fadeOut(lastFocus, 200);
            AnchorPane viewer = (AnchorPane) programViews.get(navigator)[0];
            AnimationUtils.fadeIn(viewer, 500);
            focusedPane = navigator;
        }

        ViewController controller = (ViewController) programViews.get(navigator)[1];
        double position = (double) programViews.get(navigator)[2];
        for (Label nav: programViews.keySet()) {
            if (navigator == nav) continue;
            nav.setTextFill(Color.web("#4f513a"));
        }
        animateNavigate(position);
        navigator.setTextFill(Color.web("#eeff00"));
        controller.onNavigate();
    }

    /**
     * Executes events triggered by views
     * @param event The event instance
     */
    @Subscribe public void eventBusListener(ControllerEvent event) {
        switch (event.getEventId()) {
            case "refreshViews" -> {
                for (Object[] x : programViews.values()) ((ViewController) x[1]).refreshView(true);
                SpendBUtils.spendBUpdate(false);
                ProgramUtils.print(1, "Views Updated");
            }
            case "startLoading" -> {
                pgbRefreshing.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
                if (event.getSimpleArgs()[0] == null) return;
                int delay = (int) event.getSimpleArgs()[0];
                ProgramUtils.callDelay(delay, () -> pgbRefreshing.setProgress(0.0));
            }
            case "stopLoading" -> pgbRefreshing.setProgress(0.0);

            // View Dashboard
            case "signIn" -> {
                for (Object[] view: programViews.values()) ((ViewController) view[1]).resetAll();
                ((ViewController) programViews.get(navDashboard)[1]).refreshView(false);
                ((ViewController) programViews.get(navSettings)[1]).refreshView(false);
                navigate(navDashboard);
            }

            // View Project
            case "focusProject" -> {
                if (event.getSimpleArgs()[0] == null) return;
                navigate(navProjects);
                ViewProjects controller = (ViewProjects) programViews.get(navProjects)[1];
                controller.setFocusedProject((Project) event.getSimpleArgs()[0]);
                controller.displayProjectDetails();
            }
            case "quickAddProject" -> {
                navigate(navProjects);
                ViewProjects controller = (ViewProjects) programViews.get(navProjects)[1];
                controller.projectsListBack();
                controller.projectDialogEditor("add");
            }
            case "quickAddOfficer" -> {
                navigate(navOfficers);
                ViewOfficers controller = (ViewOfficers) programViews.get(navOfficers)[1];
                controller.officerProfileBack();
                controller.officerDialogEditor("add");
            }

            // View Officer
            case "focusOfficer" -> {
                navigate(navOfficers);
                ViewOfficers controller = (ViewOfficers) programViews.get(navOfficers)[1];
                controller.setFocusedOfficer((Officer) event.getSimpleArgs()[0]);
                controller.displayOfficerDetails();
            }
        }

    }
}
