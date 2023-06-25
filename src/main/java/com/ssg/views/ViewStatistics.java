package com.ssg.views;

import com.ssg.database.SpendBCreate;
import com.ssg.database.SpendBUtils;
import com.ssg.database.models.*;
import com.ssg.utils.DateUtils;
import com.ssg.utils.ProgramUtils;
import eu.hansolo.fx.charts.ChartType;
import eu.hansolo.fx.charts.NestedBarChart;
import eu.hansolo.fx.charts.data.ChartItem;
import eu.hansolo.fx.charts.series.ChartItemSeries;
import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.chart.ChartData;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class ViewStatistics extends ViewController {
    @FXML private AnchorPane anpView;

    @FXML private Label lblStatistics;

    @FXML private AnchorPane anpProjectNumber;
    @FXML private Label lblProjectNumber;
    @FXML private Label lblProjectAdded;

    @FXML private AnchorPane anpExpenseNumber;
    @FXML private Label lblExpenseNumber;
    @FXML private Label lblExpenseAdded;

    @FXML private AnchorPane anpOfficerNumber;
    @FXML private Label lblOfficerNumber;
    @FXML private Label lblOfficerAdded;

    @FXML private AnchorPane anpRemainingFundsNumber;
    @FXML private Label lblRemainingFundsValue;
    @FXML private Label lblRemainingFundsProposed;
    @FXML private Label lblRemainingFundsApproved;
    @FXML private Label lblRemainingFundsAugmented;
    @FXML private TextField txfStatisticsFundValue;
    @FXML private TextField txfStatisticsFundDescription;
    @FXML private Button btnStatisticsRegisterFund;
    @FXML private Button btnStatisticsGenerateReport;

    @FXML private AnchorPane anpExpenseStats;
    @FXML private Label lblNoDatatoDisplay;
    @FXML private Label lblBarChartTitle;

    private static final Random RND = new Random();
    private static final String[] expenseStatus = {"Proposed", "Approved"};
    private static final String[] chartDataTime = {"day", "week", "month"};
    private int chartDataType = 0;

    // Colors
    private final Color numberChartsColor = Tile.BLUE;
    private final Color fundsChartColor = Color.web("#223f6E");
    private final Color[] expenseChartBarSubs = {
            Color.web("#063A92"),
            Color.web("#0754B6"),
            Color.web("#3A8ADE")
    };
    private final Color expenseChartBar = Color.web("#AAC0DE");

    private Tile remainingFundsChart;
    private final NestedBarChart expenseStatsChart = new NestedBarChart();

    // Chart Data
    // Numbers
    private ChartData projectNumberChartData1;
    private ChartData projectNumberChartData2;
    private ChartData projectNumberChartData3;
    private ChartData projectNumberChartData4;
    private ChartData expenseNumberChartData1;
    private ChartData expenseNumberChartData2;
    private ChartData expenseNumberChartData3;
    private ChartData expenseNumberChartData4;
    private ChartData officerNumberChartData1;
    private ChartData officerNumberChartData2;
    private ChartData officerNumberChartData3;
    private ChartData officerNumberChartData4;

    public void initialize() {
        ControllerUtils.EVENTBUS.register(this);
        // Event Handlers
        btnStatisticsRegisterFund.setOnMouseClicked(this::registerFund);
        btnStatisticsGenerateReport.setOnMouseClicked(this::generateReport);

        txfStatisticsFundValue.setTextFormatter(ControllerUtils.textFormatter(10, true));
        txfStatisticsFundDescription.setTextFormatter(ControllerUtils.textFormatter(400, false));

        // Setup
        setupProjectNumbers();
        setupExpenseNumber();
        setupOfficerNumber();
        setupRemainingFundsNumber();
        setupProjectsStats();

        refreshView(true);
    }

    @Override public void refreshView(boolean loadDB) {
        if (loadDB) loadDatabase();
        if (chartDataType >= 2) chartDataType = 0;
        else chartDataType++;

        displayProjectNumbers();
        displayExpenseNumber();
        displayOfficerNumber();
        displayRemainingFundsNumber();
        displayBarChart();
    }

    @Override public void onNavigate() {
        refreshView(false);
    }

    // Setup
    private void setupProjectNumbers() {
        double width = anpProjectNumber.getPrefWidth();
        double height = anpProjectNumber.getPrefHeight();
        Rectangle newRect = new Rectangle(width, height);

        projectNumberChartData1 = new ChartData("Item 1", RND.nextDouble() * 25, Tile.BLUE);
        projectNumberChartData2 = new ChartData("Item 2", RND.nextDouble() * 25, Tile.BLUE);
        projectNumberChartData3 = new ChartData("Item 3", RND.nextDouble() * 25, Tile.BLUE);
        projectNumberChartData4 = new ChartData("Item 4", RND.nextDouble() * 25, Tile.BLUE);

        // Charts
        Tile projectNumbersChart = TileBuilder.create().skinType(Tile.SkinType.SMOOTH_AREA_CHART)
                .prefSize(width, height)
                .minValue(0)
                .maxValue(40)
                .textVisible(false)
                .valueVisible(false)
                .backgroundColor(null)
                .chartData(projectNumberChartData1, projectNumberChartData2, projectNumberChartData3, projectNumberChartData4)
                .tooltipText("")
                .animated(true)
                .barColor(numberChartsColor)
                .build();

        newRect.setArcHeight(40.0);
        newRect.setArcWidth(40.0);
        projectNumbersChart.setClip(newRect);
        anpProjectNumber.getChildren().add(0, projectNumbersChart);
    }
    private void setupExpenseNumber() {
        double width = anpExpenseNumber.getPrefWidth();
        double height = anpExpenseNumber.getPrefHeight();
        Rectangle newRect = new Rectangle(width, height);

        expenseNumberChartData1 = new ChartData("Item 1", RND.nextDouble() * 25, Tile.BLUE);
        expenseNumberChartData2 = new ChartData("Item 2", RND.nextDouble() * 25, Tile.BLUE);
        expenseNumberChartData3 = new ChartData("Item 3", RND.nextDouble() * 25, Tile.BLUE);
        expenseNumberChartData4 = new ChartData("Item 4", RND.nextDouble() * 25, Tile.BLUE);

        Tile expenseNumberChart = TileBuilder.create().skinType(Tile.SkinType.SMOOTH_AREA_CHART)
                .prefSize(width, height)
                .minValue(0)
                .maxValue(40)
                .textVisible(false)
                .valueVisible(false)
                .backgroundColor(null)
                .chartData(projectNumberChartData1, expenseNumberChartData2, projectNumberChartData3, projectNumberChartData4)
                .tooltipText("")
                .animated(true)
                .barColor(numberChartsColor)
                .build();

        newRect.setArcHeight(40.0);
        newRect.setArcWidth(40.0);
        expenseNumberChart.setClip(newRect);
        anpExpenseNumber.getChildren().add(0, expenseNumberChart);
    }
    private void setupOfficerNumber() {
        double width = anpOfficerNumber.getPrefWidth();
        double height = anpOfficerNumber.getPrefHeight();
        Rectangle newRect = new Rectangle(width, height);

        officerNumberChartData1 = new ChartData("Item 1", RND.nextDouble() * 25, Tile.BLUE);
        officerNumberChartData2 = new ChartData("Item 2", RND.nextDouble() * 25, Tile.BLUE);
        officerNumberChartData3 = new ChartData("Item 3", RND.nextDouble() * 25, Tile.BLUE);
        officerNumberChartData4 = new ChartData("Item 4", RND.nextDouble() * 25, Tile.BLUE);

        Tile officerNumberChart = TileBuilder.create().skinType(Tile.SkinType.SMOOTH_AREA_CHART)
                .prefSize(width, height)
                .minValue(0)
                .maxValue(40)
                .textVisible(false)
                .valueVisible(false)
                .backgroundColor(null)
                .chartData(officerNumberChartData1, officerNumberChartData2, officerNumberChartData3, officerNumberChartData4)
                .tooltipText("")
                .animated(true)
                .barColor(numberChartsColor)
                .build();

        newRect.setArcHeight(40.0);
        newRect.setArcWidth(40.0);
        officerNumberChart.setClip(newRect);
        anpOfficerNumber.getChildren().add(0, officerNumberChart);
    }
    private void setupRemainingFundsNumber() {
        double width = anpRemainingFundsNumber.getPrefWidth();
        double height = anpRemainingFundsNumber.getPrefHeight();
        Rectangle newRect = new Rectangle(width, height);

        remainingFundsChart = TileBuilder.create().skinType(Tile.SkinType.FLUID)
                .prefSize(width, 509)
                .decimals(0)
                .animated(true)
                .backgroundColor(Color.TRANSPARENT)
                .valueVisible(false)
                .textVisible(false)
                .barColor(fundsChartColor)
                .build();

        remainingFundsChart.setValue(0);
        newRect.setArcHeight(40.0);
        newRect.setArcWidth(40.0);
        remainingFundsChart.setClip(newRect);
        anpRemainingFundsNumber.getChildren().add(0, remainingFundsChart);
    }
    private void setupProjectsStats() {
        double width = anpExpenseStats.getPrefWidth() - anpExpenseStats.getPadding().getLeft() - anpExpenseStats.getPadding().getRight();
        double height = anpExpenseStats.getPrefHeight() - anpExpenseStats.getPadding().getBottom() - anpExpenseStats.getPadding().getTop();
        int projectSize = 8;
        ChartItemSeries[] projectsSeries = new ChartItemSeries[projectSize];
        for (int i = projectSize - 1; i >= 0; i--) {
            ChartItem[] items = new ChartItem[3];
            for (int x = 0; x < 3; x++) items[x] = new ChartItem("Product " + x, RND.nextInt(1, 30), expenseChartBarSubs[x]);
            projectsSeries[i] = new ChartItemSeries<>(ChartType.NESTED_BAR, "Quarter " + i, expenseChartBar, Color.TRANSPARENT, items);
        }
        expenseStatsChart.setSeries(projectsSeries);
        expenseStatsChart.setPrefSize(width, height);
        // expenseStatsChart.addChartEvtObserver(ChartEvt.ANY, System.out::println);

        AnchorPane.setRightAnchor(expenseStatsChart, 0.0);
        AnchorPane.setBottomAnchor(expenseStatsChart, 0.0);
        AnchorPane.setLeftAnchor(expenseStatsChart, 0.0);
        anpExpenseStats.getChildren().add(0, expenseStatsChart);
    }

    // Display
    private void displayProjectNumbers() {
        int[] recentlyAdded = {1, 1, 1, 1};
        for (Object p: projects) {
            Project project = (Project) p;
            int timeAgo = switch (chartDataType) {
                case 0 -> DateUtils.calculateDaysAgo(project.getProject_cd());
                case 1 -> DateUtils.calculateWeeksAgo(project.getProject_cd());
                case 2 -> DateUtils.calculateMonthsAgo(project.getProject_cd());
                default -> throw new IllegalStateException("Unexpected value: " + chartDataType);
            };
            if (timeAgo > 3) continue;
            recentlyAdded[timeAgo]++;
        }
        projectNumberChartData1.setValue(recentlyAdded[3]);
        projectNumberChartData2.setValue(recentlyAdded[2]);
        projectNumberChartData3.setValue(recentlyAdded[1]);
        projectNumberChartData4.setValue(recentlyAdded[0]);
        lblProjectNumber.setText(ProgramUtils.shortenNumber(projects.size()));
        lblProjectAdded.setText(recentlyAdded[0] - 1 + " added last " + chartDataTime[chartDataType]);
    }
    private void displayExpenseNumber() {
        int[] recentlyAdded = {1, 1, 1, 1};
        for (Object e: expenses) {
            Expense expense = (Expense) e;
            int timeAgo = switch (chartDataType) {
                case 0 -> DateUtils.calculateDaysAgo(expense.getExpenseDate_cd());
                case 1 -> DateUtils.calculateWeeksAgo(expense.getExpenseDate_cd());
                case 2 -> DateUtils.calculateMonthsAgo(expense.getExpenseDate_cd());
                default -> throw new IllegalStateException("Unexpected value: " + chartDataType);
            };
            if (timeAgo > 3) continue;
            recentlyAdded[timeAgo]++;
        }
        expenseNumberChartData1.setValue(recentlyAdded[3]);
        expenseNumberChartData2.setValue(recentlyAdded[2]);
        expenseNumberChartData3.setValue(recentlyAdded[1]);
        expenseNumberChartData4.setValue(recentlyAdded[0]);

        lblExpenseNumber.setText(String.valueOf(expenses.size()));
        lblExpenseAdded.setText(recentlyAdded[0] - 1 + " added this " + chartDataTime[chartDataType]);
    }
    private void displayOfficerNumber() {
        int[] recentlyAdded = {1, 1, 1, 1};
        for (Object o: officers) {
            Officer officer = (Officer) o;
            int timeAgo = switch (chartDataType) {
                case 0 -> DateUtils.calculateDaysAgo(officer.getOfficer_cd());
                case 1 -> DateUtils.calculateWeeksAgo(officer.getOfficer_cd());
                case 2 -> DateUtils.calculateMonthsAgo(officer.getOfficer_cd());
                default -> throw new IllegalStateException("Unexpected value: " + chartDataType);
            };
            if (timeAgo > 3) continue;
            recentlyAdded[timeAgo]++;
        }
        officerNumberChartData1.setValue(recentlyAdded[3]);
        officerNumberChartData2.setValue(recentlyAdded[2]);
        officerNumberChartData3.setValue(recentlyAdded[1]);
        officerNumberChartData4.setValue(recentlyAdded[0]);

        lblOfficerNumber.setText(String.valueOf(officers.size()));
        lblOfficerAdded.setText(recentlyAdded[0] - 1 + " added last " + chartDataTime[chartDataType]);
    }

    private void displayRemainingFundsNumber() {
        final double[] expenseFunds = {
                0.0,
                0.0
        };
        final double[] augmentedFunds = {0.0};
        expenses.stream().map(e -> (Expense) e).forEach(expense -> expenseFunds[expense.getStatus()] += expense.getTotalPrice());
        funds.stream().map(f -> (Fund) f).forEach(fund -> augmentedFunds[0] += fund.getAmount());
        double remainingFunds = augmentedFunds[0] - (expenseFunds[0] + expenseFunds[1]);
        double percentageFunds = Math.max(remainingFunds / augmentedFunds[0] * 100, 1);
        lblRemainingFundsValue.setText((remainingFunds < 0 ? "-" : "") + "₱" + ProgramUtils.shortenNumber(remainingFunds));
        lblRemainingFundsProposed.setText("-₱" + ProgramUtils.shortenNumber(expenseFunds[0]));
        lblRemainingFundsApproved.setText("-₱" + ProgramUtils.shortenNumber(expenseFunds[1]));
        lblRemainingFundsAugmented.setText("₱" + ProgramUtils.shortenNumber(augmentedFunds[0]));
        remainingFundsChart.setValue((2 * percentageFunds) - 100);
    }
    private void displayBarChart() {
        lblNoDatatoDisplay.setVisible(projects.size() == 0);
        int projectSize = 10;
        ChartItemSeries[] projectsBar = new ChartItemSeries[projectSize];
        for (int i = 0; i < projectSize; i++) {
            Object p = projects.get(i);
            Project project = (Project) p;
            ChartItem[] items = new ChartItem[2];
            for (int x = 0; x < 2; x++) items[x] = new ChartItem(expenseStatus[x], 1.0, expenseChartBarSubs[x]);
            projectsBar[projectSize - 1 - i] = new ChartItemSeries<>(ChartType.NESTED_BAR, project.getTitle(), expenseChartBar, Color.TRANSPARENT, items);
            for (Object e : expenses) {
                Expense expense = (Expense) e;
                if (expense.getProject_id() != project.getProject_id()) continue;
                int status = expense.getStatus();
                ChartItem item = (ChartItem) projectsBar[projectSize - i - 1].getItems().get(status);
                item.setValue(expense.getTotalPrice() + item.getValue());
            }
        }
        expenseStatsChart.setSeries(projectsBar);
    }

    // Methods
    private void registerFund(MouseEvent mouseEvent) {
        try {
            double fundValue = Double.parseDouble(txfStatisticsFundValue.getText());
            String description = txfStatisticsFundDescription.getText();

            if (fundValue <= 0) {
                MainEvents.showDialogMessage("Invalid Funds", "You can't add a negative fund. Make sure you input correctly in the text fields");
                return;
            }

            Object[] newFund = ModelValues.newFundData(
                    fundValue,
                    description
            );
            txfStatisticsFundDescription.setText("");
            txfStatisticsFundValue.setText("");
            lblStatistics.requestFocus();
            SpendBCreate.createFundsData(newFund, true);
            MainEvents.showDialogMessage("Fund Registered", "You successfully registered a new amount in the funds database.");
        } catch (Exception e) {
            MainEvents.showDialogMessage("Fund value should be numeric", "Error! Make sure you input correctly in the text fields", "Okay");
        }
    }
    private void generateReport(MouseEvent mouseEvent) {
        MainEvents.startLoading();
        Map<String, String> queries = new HashMap<>();

        String query = "SELECT\n" +
                "  sd.DATA_ID AS DATAID,\n" +
                "  sd.UPDATETIME AS UPDATETIME,\n" +
                "  sd.SCHOOLYEAR,\n" +
                "  sd.SCHOOLLOGO AS SCHOOLLOGO,\n" +
                "  sd.SSGLOGO AS SSGLOGO,\n" +
                "  f.FUND_ID AS FUNDID,\n" +
                "  f.AMOUNT AS AMOUNT,\n" +
                "  f.DESCRIPTION AS DESCRIPTION,\n" +
                "  f.FUND_CD\n" +
                "FROM\n" +
                "  schooldata sd\n" +
                "JOIN\n" +
                "  funds f ON 1 = 1";

        queries.put("main", query);
        queries.put("Funds", query);
        SpendBUtils.generateReport(6, queries);
        MainEvents.stopLoading();

    }

}
