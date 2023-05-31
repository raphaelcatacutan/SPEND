package com.ssg.views;

import com.ssg.MainActivity;
import com.ssg.database.models.Expense;
import com.ssg.database.models.Officer;
import com.ssg.database.models.Project;
import com.ssg.utils.ProgramUtils;
import com.ssg.utils.RuntimeData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

public class ViewDashboard extends ViewController {
    @FXML private Label lblWelcome;
    @FXML private Label lblNearestEvent;
    @FXML private PieChart picProjectStatus;
    @FXML private LineChart<String, Number> lncProjectsExpense;
    @FXML private VBox vbxRecentList;

    private final int lineChartMonthsNumber = 10;
    private final XYChart.Series<String, Number> series = new XYChart.Series<>();
    private final SimpleDateFormat yearMonth = new SimpleDateFormat("yyyy-MM");
    private final SimpleDateFormat month = new SimpleDateFormat("MMM");
    private final String[] DBNEEDED = {"PROJECTS", "EXPENSES", "OFFICERS"};

    @Override
    public void initialize() {
        ControllerUtils.EVENTBUS.register(this);
        // Initialize Expenses Chart
        for (Date date: ProgramUtils.getLastMonths(lineChartMonthsNumber)) series.getData().add(new XYChart.Data<>(month.format(date), new Random().nextInt(1001)));
        series.setName("Expenses");
        lncProjectsExpense.getData().add(series);
    }

    @Override
    public void refreshView() {
        loadDatabase();
        displayWelcomeMessage();
        displayPicProjectStatus();
        displayLncProjectsExpense();
        displayNextEvent();
        displayRecents();
    }

    @Override
    public void onNavigate() {
        displayPicProjectStatus();
        displayLncProjectsExpense();
    }
    private void displayWelcomeMessage() {
        if (RuntimeData.USER == null) return;
        lblWelcome.setText("Welcome " + RuntimeData.USER.getFirstName() + "!");
    }
    private void displayPicProjectStatus() {
        int proposed = 0;
        int approved = 0;
        for (Object e: expenses) {
            Expense expense = (Expense) e;
            if (expense.getStatus() == 0) proposed++;
            else approved++;
        }
        ObservableList<PieChart.Data> picProjectStatusData =
                FXCollections.observableArrayList(
                        new PieChart.Data("Proposed", proposed),
                        new PieChart.Data("On Progress", approved)
                );
        picProjectStatus.setData(picProjectStatusData);
    }
    private void displayLncProjectsExpense() {
        ArrayList<Date> lastMonths = ProgramUtils.getLastMonths(lineChartMonthsNumber);
        for (int dateIndex = 0; dateIndex < lineChartMonthsNumber; dateIndex++ ) {
            Date date = lastMonths.get(dateIndex);
            String monthName = month.format(date);
            double monthExpense = 0;
            for (Object e: expenses) {
                Expense expense = (Expense) e;
                if (!yearMonth.format(date).equals(yearMonth.format(expense.getExpenseDate_cd()))) continue;
                monthExpense += expense.getTotalPrice();
            }
            series.getData().get(dateIndex).setXValue(monthName);
            // series.getData().get(dateIndex).setYValue(monthExpense);
            // TODO Set to `monthExpense`. Remove code under and uncomment above
            int randomExpense = new Random().nextInt(1001);
            series.getData().get(dateIndex).setYValue(randomExpense);
        }
    }
    private void displayNextEvent() {
        Date currentDate = new Date(System.currentTimeMillis());
        Date nearestEventDate = null;
        Project nearestProject = (Project) projects.get(0);
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
            else if (d instanceof Officer officer)  updateTime = officer.getUpdatetime();
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
            if (recentObjects.size() >= 3) break;
        }

        // Display recent objects in the UI
        ObservableList<Object> recent = FXCollections.observableArrayList(recentObjects);
        for (int x = 1; x <= recent.size(); x++) {
            Object r = recent.get(x - 1);
            String objectName, objectType;
            if (r instanceof Project project) {
                objectName = project.getTitle();
                objectType = "Project";
            } else if (r instanceof Officer officer) {
                objectName = officer.getShortName();
                objectType = "Officer";
            } else continue;
            String pathname = "assets/icons/" + objectType.toLowerCase() + ".png";
            Image image = new Image(Objects.requireNonNull(MainActivity.class.getResource(pathname)).toString());
            ((ImageView) vbxRecentList.lookup("#imvRecent" + x)).setImage(image);
            ((Label) vbxRecentList.lookup("#lblRecentTitle" + x)).setText(objectName);
            ((Label) vbxRecentList.lookup("#lblRecentSubtitle" + x)).setText(objectType);
        }
    }

}
