package com.ssg.views;

import com.google.common.eventbus.Subscribe;
import com.ssg.database.SpendBUtils;
import com.ssg.database.models.Officer;
import com.ssg.database.models.Project;
import com.ssg.utils.ProgramUtils;
import com.ssg.views.animations.SelfishAnimation;
import com.ssg.views.animations.ViewsAnimations;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.HashMap;

import static com.ssg.views.ControllerUtils.triggerEvent;

public class MainView {
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
            if (lastFocus != null) ViewsAnimations.fadeOut(lastFocus);
            AnchorPane viewer = (AnchorPane) programViews.get(navigator)[0];
            ViewsAnimations.fadeIn(viewer);
            focusedPane = navigator;
        }

        ViewController controller = (ViewController) programViews.get(navigator)[1];
        double position = (double) programViews.get(navigator)[2];
        for (Label nav: programViews.keySet()) {
            if (navigator == nav) continue;
            nav.setTextFill(Color.web("#4f513a"));
        }
        SelfishAnimation.animateNavigate(navCircleIndicator, position);
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

            // View Dashboard
            case "signIn" -> {
                ((ViewController) programViews.get(navDashboard)[1]).refreshView(false);
                ((ViewController) programViews.get(navSettings)[1]).refreshView(false);
                navigate(navDashboard);
            }
            case "signOut" -> {
                for (Object[] view: programViews.values()) ((ViewController) view[1]).resetAll();
            }
            case "quickAddProject" -> {
                navigate(navProjects);
                ViewProjects controller = (ViewProjects) programViews.get(navProjects)[1];
                controller.quickAddProject();
            }
            case "quickAddOfficer" -> {
                navigate(navOfficers);
                ViewOfficers controller = (ViewOfficers) programViews.get(navOfficers)[1];
                controller.quickAddOfficer();
            }

            // View Project
            case "focusProject" -> {
                if (event.getSimpleArgs()[0] == null) return;
                navigate(navProjects);
                ViewProjects controller = (ViewProjects) programViews.get(navProjects)[1];
                controller.setFocusedProject((Project) event.getSimpleArgs()[0]);
                controller.displayProjectDetails();
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
