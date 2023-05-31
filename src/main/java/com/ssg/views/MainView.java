package com.ssg.views;

import com.google.common.eventbus.Subscribe;
import com.ssg.database.SpendBUtils;
import com.ssg.database.models.Project;
import com.ssg.utils.ProgramUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
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

    @FXML private StackPane stkView;


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


        stkView.getChildren().addAll(officersViewer, projectsViewer, dashboardViewer, statisticsViewer, settingsViewer);
        // Storage for Navigators
        programViews.put(navDashboard, new Object[] {dashboardViewer, dashboardController});
        programViews.put(navOfficers, new Object[] {officersViewer, officersController});
        programViews.put(navStatistics, new Object[] {statisticsViewer, statisticsController});
        programViews.put(navProjects, new Object[] {projectsViewer, projectsController});
        programViews.put(navSettings, new Object[] {settingsViewer, settingsController});
        // Listener for Navigator
        for (Label navigator: programViews.keySet()) navigator.setOnMouseClicked(event -> navigate(navigator));
        triggerEvent("refreshViews");
    }
    public void navigate(Label navigator) {
        AnchorPane viewer = (AnchorPane) programViews.get(navigator)[0];
        ViewController controller = (ViewController) programViews.get(navigator)[1];
        for (Label nav: programViews.keySet()) {
            if (navigator == nav) continue;
            ((AnchorPane) programViews.get(nav)[0]).setVisible(false);
            nav.setTextFill(Color.web("#4f513a"));
        }
        navigator.setTextFill(Color.web("#eeff00"));
        viewer.setVisible(true);
        controller.onNavigate();
        // TODO: Change the dot to where the navigator is
    }
    @Subscribe
    public void eventBusListener(ControllerEvent event) {
        switch (event.getEventId()) {
            case "byPassLogin" -> {
                ((ViewController) programViews.get(navDashboard)[1]).refreshView();
                navigate(navDashboard);
            }
            case "refreshViews" -> {
                // FIXME This reloads the database for each view instead of loading once
                for (Object[] x : programViews.values()) ((ViewController) x[1]).refreshView();
                SpendBUtils.spendBUpdate(false);
                ProgramUtils.print(1, "Views Updated");
            }
            case "focusProject" -> {
                navigate(navProjects);
                ViewProjects controller = (ViewProjects) programViews.get(navProjects)[1];
                controller.setFocusedProject((Project) event.getSimpleArgs()[0]);
                controller.displayProjectDetails();
            }
        }

    }
}
