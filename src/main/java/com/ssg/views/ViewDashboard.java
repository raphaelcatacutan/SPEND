package com.ssg.views;

import com.ssg.MainActivity;
import com.ssg.database.models.Expense;
import com.ssg.database.models.Officer;
import com.ssg.database.models.Project;
import com.ssg.utils.DateUtils;
import com.ssg.utils.RuntimeData;
import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.chart.TilesFXSeries;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;

public class ViewDashboard extends ViewController {
    @FXML private AnchorPane anpView;
    @FXML private Label lblWelcome;
    @FXML private Label lblNearestEvent;
    @FXML private VBox vbxRecentList;
    @FXML private AnchorPane anpDashboardOverview;
    @FXML private Button btnDashboardViewNextEvent;
    @FXML private Button btnQuickAddProject;
    @FXML private Button btnQuickAddOfficer;
    @FXML private Button btnNewSchoolYear;
    @FXML private Label lblDashboardExpenseChartTitle;

    private final XYChart.Series<String, Number> lncSeries = new XYChart.Series<>();
    private final String[] DBNEEDED = {"PROJECTS", "EXPENSES", "OFFICERS", "SCHOOLDATA"};
    private final String[] xValues = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8"};
    private final int lineSize = 8;
    private int chartDataType = 0;
    private Project nearestProject;

    @Override
    public void initialize() {
        ControllerUtils.EVENTBUS.register(this);
        setupLncMonthlyExpenses();
        btnDashboardViewNextEvent.setOnMouseClicked(e -> MainEvents.focusProject(nearestProject));
        btnQuickAddOfficer.setOnMouseClicked(this::quickAddOfficer);
        btnQuickAddProject.setOnMouseClicked(this::quickAddProject);
        btnNewSchoolYear.setOnMouseClicked(e -> ControllerUtils.triggerEvent("newSY"));

        refreshView(true);
    }

    @Override
    public void refreshView(boolean loadDB) {
        if (loadDB) loadDatabase();
        if (chartDataType >= 2) chartDataType = 0;
        else chartDataType++;
        displayWelcomeMessage();
        displayLncProjectsExpense();
        displayNextEvent();
        displayRecents();
    }

    @Override
    public void onNavigate() {
        refreshView(false);
    }

    private void displayWelcomeMessage() {
        if (RuntimeData.USER == null) return;
        lblWelcome.setText("Welcome " + RuntimeData.USER.getFirstName() + "!");
    }
    private void setupLncMonthlyExpenses() {
        AnchorPane p = anpDashboardOverview;
        double width = p.getPrefWidth() - p.getPadding().getLeft() - p.getPadding().getRight();
        double height = p.getPrefHeight() - p.getPadding().getBottom() - p.getPadding().getTop();
        // AreaChart Data
        Tile chart;
        lncSeries.setName("Project Expenses");
        lncSeries.getData().add(new XYChart.Data<>("", 0));
        for (int x = 0; x < lineSize - 1; x++) {
            lncSeries.getData().add(new XYChart.Data<>(xValues[lineSize - 2 - x], new Random().nextInt(20)));
        }
        lncSeries.getData().add(new XYChart.Data<>(" ", 0));

        chart = TileBuilder.create()
                .skinType(Tile.SkinType.SMOOTHED_CHART)
                .prefSize(width, height)
                .chartType(Tile.ChartType.AREA)
                .animated(true)
                .smoothing(true)
                .tooltipTimeout(1000)
                .backgroundColor(Color.TRANSPARENT)
                .showInfoRegion(false)
                .build();

        chart.setTilesFXSeries(new TilesFXSeries<>(lncSeries,
                Tile.BLUE,
                new LinearGradient(0, 0, 0, 1,
                        true, CycleMethod.NO_CYCLE,
                        new Stop(0, Tile.BLUE),
                        new Stop(1, Color.TRANSPARENT))));
        p.getChildren().add(chart);
        AnchorPane.setRightAnchor(chart, 0.0);
        AnchorPane.setBottomAnchor(chart, 0.0);
        AnchorPane.setLeftAnchor(chart, 0.0);
    }
    private void displayLncProjectsExpense() {
        int[] yValues = new int[lineSize];
        switch (chartDataType) {
            case 0 -> lblDashboardExpenseChartTitle.setText("Expenses from the last " + (lineSize - 2) + " months");
            case 1 -> lblDashboardExpenseChartTitle.setText("Expenses from the last " + (lineSize - 2) + " weeks");
            case 2 -> lblDashboardExpenseChartTitle.setText("Expenses from the last " + (lineSize - 2) + " days");
            default -> throw new IllegalStateException("Unexpected value: " + chartDataType);
        };
        for (Object e: expenses) {
            Expense expense = (Expense) e;
            int index = switch (chartDataType) {
                case 0 -> DateUtils.calculateMonthsAgo(expense.getUpdateTime());
                case 1 -> DateUtils.calculateWeeksAgo(expense.getUpdateTime());
                case 2 -> DateUtils.calculateDaysAgo(expense.getUpdateTime());
                default -> throw new IllegalStateException("Unexpected value: " + chartDataType);
            };
            if (index > lineSize) continue;
            yValues[index] += expense.getTotalPrice();

        }
        for (int i = 0; i < lineSize; i++) {
            lncSeries.getData().get(lineSize - 1 - i).setYValue(yValues[i]);
        }
        lncSeries.getData().get(0).setXValue("");
        lncSeries.getData().get(0).setYValue(0);
        lncSeries.getData().get(lineSize).setXValue(" ");
        lncSeries.getData().get(lineSize).setYValue(0);
    }
    private void displayNextEvent() {
        Date currentDate = new Date(System.currentTimeMillis());
        Date nearestEventDate = null;
        if (projects.size() == 0) {
            lblNearestEvent.setText("No Project");
            nearestProject = null;
            return;
        }
        nearestProject = (Project) projects.get(0);
        for (Object p : projects) {
            Project project = (Project) p;
            Date eventDate = project.getEventdate();
            if (eventDate.after(currentDate) && (nearestEventDate == null || eventDate.before(nearestEventDate))) {
                nearestEventDate = eventDate;
                nearestProject = project;
            }
        }
        lblNearestEvent.setText(nearestProject.getTitle());
    }
    private void displayRecents() {
        ObservableList<Object> data = FXCollections.observableArrayList();
        Timestamp currentDate = new Timestamp(System.currentTimeMillis());
        List<Object> recentObjects = new ArrayList<>();
        Map<Timestamp, List<Object>> objectsByTimestamp = new HashMap<>();

        // Add projects and officers to the data list
        data.addAll(projects);
        data.addAll(officers);

        // Group objects by timestamp
        for (Object d : data) {
            Timestamp updateTime;
            if (d instanceof Project project) updateTime = project.getUpdatetime();
            else if (d instanceof Officer officer) updateTime = officer.getUpdatetime();
            else continue;
            if (updateTime.after(currentDate)) continue;
            if (!objectsByTimestamp.containsKey(updateTime)) objectsByTimestamp.put(updateTime, new ArrayList<>());
            objectsByTimestamp.get(updateTime).add(d);
        }

        // Sort timestamps in reverse order
        List<Timestamp> timestamps = new ArrayList<>(objectsByTimestamp.keySet());
        timestamps.sort(Collections.reverseOrder());

        // Get the most recent objects, up to a limit of 3
        for (Timestamp timestamp : timestamps) {
            List<Object> objects = objectsByTimestamp.get(timestamp);
            for (Object object : objects) {
                recentObjects.add(object);
                if (recentObjects.size() >= 3) break;
            }
            if (recentObjects.size() == 3) break;
        }

        // Display recent objects in the UI
        ObservableList<Object> recent = FXCollections.observableArrayList(recentObjects);
        for (int x = 1; x <= 3; x++) {
            String objectName, objectType;
            EventHandler<MouseEvent> onClick;
            Image image;
            Object r = (recent.size() < x) ? null : recent.get(x - 1);
            if (r instanceof Project project) {
                objectName = project.getTitle();
                objectType = "Project";
                onClick = e -> MainEvents.focusProject(project);
            } else if (r instanceof Officer officer) {
                objectName = officer.getShortName();
                objectType = "Officer";
                onClick = e -> MainEvents.focusOfficer(officer);
            } else {
                objectName = "No Recents";
                objectType = "No Recents";
                onClick = e -> {};
            }
            String pathname = "assets/icons/" + objectType.toLowerCase() + ".png";
            if (objectType.equals("No Recents")) image = ControllerUtils.DEFAULTAVATAR;
            else image = new Image(Objects.requireNonNull(MainActivity.class.getResource(pathname)).toString());
            ((ImageView) vbxRecentList.lookup("#imvRecent" + x)).setImage(image);
            ((Label) vbxRecentList.lookup("#lblRecentTitle" + x)).setText(objectName);
            ((Label) vbxRecentList.lookup("#lblRecentSubtitle" + x)).setText(objectType);
            vbxRecentList.lookup("#pneRecentBox" + x).setOnMouseClicked(onClick);
        }
    }

    // Methods
    private void quickAddProject(MouseEvent event) {
        if (notAdmin()) return;
        MainEvents.quickAddProject();
    }
    private void quickAddOfficer(MouseEvent event) {
        if (notAdmin()) return;
        MainEvents.quickAddOfficer();
    }

}
