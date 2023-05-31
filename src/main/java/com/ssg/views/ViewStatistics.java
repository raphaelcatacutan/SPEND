package com.ssg.views;

import eu.hansolo.fx.charts.*;
import eu.hansolo.fx.charts.data.ChartItem;
import eu.hansolo.fx.charts.data.ChartItemBuilder;
import eu.hansolo.fx.charts.data.Metadata;
import eu.hansolo.fx.charts.event.ChartEvt;
import eu.hansolo.fx.charts.series.ChartItemSeries;
import eu.hansolo.fx.charts.tools.NumberFormat;
import eu.hansolo.fx.charts.tools.Order;
import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.chart.ChartData;
import javafx.beans.property.StringProperty;
import javafx.beans.property.StringPropertyBase;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.List;
import java.util.Random;

import static eu.hansolo.fx.charts.color.MaterialDesignColors.*;


public class ViewStatistics extends ViewController {

    // Start of Temporary
    private static final Random RND = new Random();
    // End of Temporary

    @FXML private AnchorPane anpProjectNumber;
    @FXML private AnchorPane anpExpensesNumber;
    @FXML private AnchorPane anpOfficerNumber;
    @FXML private AnchorPane anpActivityLogsNumber;
    @FXML private AnchorPane anpRemainingFundsNumber;
    @FXML private AnchorPane anpOfficerStats;
    @FXML private AnchorPane anpExpenseStats;
    @FXML private AnchorPane anpProjectStats;

    private Tile projectNumbersChart;
    private Tile expenseNumberChart;
    private Tile officerNumberChart;
    private Tile activityNumberChart;
    private Tile remainingFundsChart;

    private ConcentricRingChart officerStatsChart;
    private NestedBarChart expenseStatsChart;
    private CoxcombChart projectStatsChart;

    @Override
    public void initialize() {
        ControllerUtils.EVENTBUS.register(this);

        setupProjectNumbers();
        setupExpenseNumber();
        setupOfficerNumber();
        setupActivityLogsNumber();

        setupRemainingFundsNumber();
        setupOfficerStats();
        setupExpenseStats();
        setupProjectStats();
    }

    @Override
    public void refreshView() {

    }

    @Override
    public void onNavigate() {

    }

    private void setupProjectNumbers() {
        double width = anpProjectNumber.getPrefWidth();
        double height = anpProjectNumber.getPrefHeight();
        Rectangle newRect = new Rectangle(width, height);

        ChartData smoothChartData1;
        ChartData smoothChartData2;
        ChartData smoothChartData3;
        ChartData smoothChartData4;
        smoothChartData1 = new ChartData("Item 1", RND.nextDouble() * 25, Tile.YELLOW);
        smoothChartData2 = new ChartData("Item 2", RND.nextDouble() * 25, Tile.BLUE);
        smoothChartData3 = new ChartData("Item 3", RND.nextDouble() * 25, Tile.BLUE);
        smoothChartData4 = new ChartData("Item 4", RND.nextDouble() * 25, Tile.BLUE);

       projectNumbersChart = TileBuilder.create().skinType(Tile.SkinType.SMOOTH_AREA_CHART)
               .prefSize(width, height)
               .minValue(0)
               .maxValue(40)
               .textVisible(false)
               .valueVisible(false)
               .backgroundColor(null)
               .chartData(smoothChartData1, smoothChartData2, smoothChartData3, smoothChartData4)
               .tooltipText("")
               .animated(true)
               .barColor(Color.rgb(129, 112, 0))
               .build();

        newRect.setArcHeight(40.0);
        newRect.setArcWidth(40.0);
        projectNumbersChart.setClip(newRect);
        anpProjectNumber.getChildren().add(0, projectNumbersChart);
    }

    private void setupExpenseNumber() {
        double width = anpExpensesNumber.getPrefWidth();
        double height = anpExpensesNumber.getPrefHeight();
        Rectangle newRect = new Rectangle(width, height);

        ChartData smoothChartData1;
        ChartData smoothChartData2;
        ChartData smoothChartData3;
        ChartData smoothChartData4;
        smoothChartData1 = new ChartData("Item 1", RND.nextDouble() * 25, Tile.BLUE);
        smoothChartData2 = new ChartData("Item 2", RND.nextDouble() * 25, Tile.BLUE);
        smoothChartData3 = new ChartData("Item 3", RND.nextDouble() * 25, Tile.BLUE);
        smoothChartData4 = new ChartData("Item 4", RND.nextDouble() * 25, Tile.BLUE);

        expenseNumberChart = TileBuilder.create().skinType(Tile.SkinType.SMOOTH_AREA_CHART)
                .prefSize(width, height)
                .minValue(0)
                .maxValue(40)
                .textVisible(false)
                .valueVisible(false)
                .backgroundColor(null)
                .chartData(smoothChartData1, smoothChartData2, smoothChartData3, smoothChartData4)
                .tooltipText("")
                .animated(true)
                .barColor(Color.rgb(129, 112, 0))
                .build();

        newRect.setArcHeight(40.0);
        newRect.setArcWidth(40.0);
        expenseNumberChart.setClip(newRect);
        anpExpensesNumber.getChildren().add(0, expenseNumberChart);
    }

    private void setupOfficerNumber() {
        double width = anpOfficerNumber.getPrefWidth();
        double height = anpOfficerNumber.getPrefHeight();
        Rectangle newRect = new Rectangle(width, height);

        ChartData smoothChartData1;
        ChartData smoothChartData2;
        ChartData smoothChartData3;
        ChartData smoothChartData4;
        smoothChartData1 = new ChartData("Item 1", RND.nextDouble() * 25, Tile.BLUE);
        smoothChartData2 = new ChartData("Item 2", RND.nextDouble() * 25, Tile.BLUE);
        smoothChartData3 = new ChartData("Item 3", RND.nextDouble() * 25, Tile.BLUE);
        smoothChartData4 = new ChartData("Item 4", RND.nextDouble() * 25, Tile.BLUE);

        officerNumberChart = TileBuilder.create().skinType(Tile.SkinType.SMOOTH_AREA_CHART)
                .prefSize(width, height)
                .minValue(0)
                .maxValue(40)
                .textVisible(false)
                .valueVisible(false)
                .backgroundColor(null)
                .chartData(smoothChartData1, smoothChartData2, smoothChartData3, smoothChartData4)
                .tooltipText("")
                .animated(true)
                .barColor(Color.rgb(129, 112, 0))
                .build();

        newRect.setArcHeight(40.0);
        newRect.setArcWidth(40.0);
        officerNumberChart.setClip(newRect);
        anpOfficerNumber.getChildren().add(0, officerNumberChart);
    }

    private void setupActivityLogsNumber() {
        double width = anpActivityLogsNumber.getPrefWidth();
        double height = anpActivityLogsNumber.getPrefHeight();
        Rectangle newRect = new Rectangle(width, height);

        ChartData smoothChartData1;
        ChartData smoothChartData2;
        ChartData smoothChartData3;
        ChartData smoothChartData4;
        smoothChartData1 = new ChartData("Item 1", RND.nextDouble() * 25, Tile.BLUE);
        smoothChartData2 = new ChartData("Item 2", RND.nextDouble() * 25, Tile.BLUE);
        smoothChartData3 = new ChartData("Item 3", RND.nextDouble() * 25, Tile.BLUE);
        smoothChartData4 = new ChartData("Item 4", RND.nextDouble() * 25, Tile.BLUE);

        activityNumberChart = TileBuilder.create().skinType(Tile.SkinType.SMOOTH_AREA_CHART)
                .prefSize(width, height)
                .minValue(0)
                .maxValue(40)
                .textVisible(false)
                .valueVisible(false)
                .backgroundColor(null)
                .chartData(smoothChartData1, smoothChartData2, smoothChartData3, smoothChartData4)
                .tooltipText("")
                .animated(true)
                .barColor(Color.rgb(129, 112, 0))
                .build();

        newRect.setArcHeight(40.0);
        newRect.setArcWidth(40.0);
        activityNumberChart.setClip(newRect);
        anpActivityLogsNumber.getChildren().add(0, activityNumberChart);
    }

    private void setupRemainingFundsNumber() {
        double width = anpRemainingFundsNumber.getPrefWidth();
        double height = anpRemainingFundsNumber.getPrefHeight();
        Rectangle newRect = new Rectangle(width, height);

        remainingFundsChart = TileBuilder.create().skinType(Tile.SkinType.FLUID)
                .prefSize(width, height)
                .decimals(0)
                .barColor(Tile.BLUE)
                .animated(true)
                .backgroundColor(null)
                .valueVisible(false)
                .textVisible(false)
                .barColor(Color.rgb(129, 112, 0, 0.6))
                .build();

        remainingFundsChart.setValue(0.3);

        newRect.setArcHeight(40.0);
        newRect.setArcWidth(40.0);
        remainingFundsChart.setClip(newRect);
        anpRemainingFundsNumber.getChildren().add(0, remainingFundsChart);
    }

    private void setupOfficerStats() {
        double size = anpProjectStats.getPrefHeight() - anpProjectStats.getPadding().getTop() - anpProjectStats.getPadding().getBottom();
        ChartItem chart1Data1;
        ChartItem chart1Data2;
        ChartItem chart1Data3;
        ChartItem chart1Data4;
        ChartItem chart1Data5;
        chart1Data1 = ChartItemBuilder.create().name("Item 1").fill(Color.web("#3552a0")).textFill(Color.WHITE).animated(true).build();
        chart1Data2 = ChartItemBuilder.create().name("Item 2").fill(Color.web("#45a1cf")).textFill(Color.WHITE).animated(true).build();
        chart1Data3 = ChartItemBuilder.create().name("Item 3").fill(Color.web("#45cf6d")).textFill(Color.WHITE).animated(true).build();
        chart1Data4 = ChartItemBuilder.create().name("Item 4").fill(Color.web("#e3eb4f")).textFill(Color.BLACK).animated(true).build();
        chart1Data5 = ChartItemBuilder.create().name("Item 5").fill(Color.web("#efb750")).textFill(Color.WHITE).animated(true).build();

        officerStatsChart = ConcentricRingChartBuilder.create()
                .prefSize(size, size)
                .maxSize(size, size)
                .items(chart1Data1, chart1Data2, chart1Data3, chart1Data4, chart1Data5)
                .sorted(false)
                .order(Order.DESCENDING)
                .numberFormat(NumberFormat.PERCENTAGE_1_DECIMAL)
                .itemLabelFill(Color.BLACK)
                .barBackgroundFill(Color.TRANSPARENT)
                .build();

        chart1Data1.setValue(100);
        chart1Data2.setValue(80);
        chart1Data3.setValue(60);
        chart1Data4.setValue(50);
        chart1Data5.setValue(20);

        AnchorPane.setRightAnchor(officerStatsChart, 0.0);
        AnchorPane.setBottomAnchor(officerStatsChart, 0.0);
        AnchorPane.setLeftAnchor(officerStatsChart, 0.0);
        anpProjectStats.getChildren().add(0, officerStatsChart);
    }

    private void setupExpenseStats() {
        double width = anpExpenseStats.getPrefWidth() - anpExpenseStats.getPadding().getLeft() - anpExpenseStats.getPadding().getRight();
        double height = anpExpenseStats.getPrefHeight() - anpExpenseStats.getPadding().getBottom() - anpExpenseStats.getPadding().getTop();
        ChartItem p1Q1 = new ChartItem("Product 1", 16, CYAN_700.get());
        ChartItem p2Q1 = new ChartItem("Product 2", 8, CYAN_500.get());
        ChartItem p3Q1 = new ChartItem("Product 3", 4, CYAN_300.get());
        ChartItem p4Q1 = new ChartItem("Product 4", 2, CYAN_100.get());

        ChartItem p1Q2 = new ChartItem("Product 1", 12, PURPLE_700.get());
        ChartItem p2Q2 = new ChartItem("Product 2", 5, PURPLE_500.get());
        ChartItem p3Q2 = new ChartItem("Product 3", 3, PURPLE_300.get());
        ChartItem p4Q2 = new ChartItem("Product 4", 1, PURPLE_100.get());

        ChartItem p1Q3 = new ChartItem("Product 1", 14, PINK_700.get());
        ChartItem p2Q3 = new ChartItem("Product 2", 7, PINK_500.get());
        ChartItem p3Q3 = new ChartItem("Product 3", 3.5, PINK_300.get());
        ChartItem p4Q3 = new ChartItem("Product 4", 1.75, PINK_100.get());

        ChartItem p1Q4 = new ChartItem("Product 1", 18, AMBER_700.get());
        ChartItem p2Q4 = new ChartItem("Product 2", 9, AMBER_500.get());
        ChartItem p3Q4 = new ChartItem("Product 3", 4.5, AMBER_300.get());
        ChartItem p4Q4 = new ChartItem("Product 4", 2.25, AMBER_100.get());

        ChartItemSeries<ChartItem> q1 = new ChartItemSeries<>(ChartType.NESTED_BAR, "1st Quarter", CYAN_900.get(), Color.TRANSPARENT, p1Q1, p2Q1, p3Q1, p4Q1);
        ChartItemSeries<ChartItem> q2 = new ChartItemSeries<>(ChartType.NESTED_BAR, "2nd Quarter", PURPLE_900.get(), Color.TRANSPARENT, p1Q2, p2Q2, p3Q2, p4Q2);
        ChartItemSeries<ChartItem> q3 = new ChartItemSeries<>(ChartType.NESTED_BAR, "3rd Quarter", PINK_900.get(), Color.TRANSPARENT, p1Q3, p2Q3, p3Q3, p4Q3);
        ChartItemSeries<ChartItem> q4 = new ChartItemSeries<>(ChartType.NESTED_BAR, "4th Quarter", AMBER_900.get(), Color.TRANSPARENT, p1Q4, p2Q4, p3Q4, p4Q4);


        expenseStatsChart = new NestedBarChart(q1, q2, q3, q4);

        expenseStatsChart.setPrefSize(width, height);
        expenseStatsChart.addChartEvtObserver(ChartEvt.ANY, System.out::println);

        AnchorPane.setRightAnchor(expenseStatsChart, 0.0);
        AnchorPane.setBottomAnchor(expenseStatsChart, 0.0);
        AnchorPane.setLeftAnchor(expenseStatsChart, 0.0);
        anpExpenseStats.getChildren().add(0, expenseStatsChart);

    }

    private void setupProjectStats() {
        double size = anpOfficerStats.getPrefWidth() - anpOfficerStats.getPadding().getLeft() - anpOfficerStats.getPadding().getRight();

        Metainfo     metainfo1;
        Metainfo     metainfo2;
        Metainfo     metainfo3;
        Metainfo     metainfo4;
        Metainfo     metainfo5;
        Metainfo     metainfo6;
        metainfo1 = new Metainfo("Text 1");
        metainfo2 = new Metainfo("Text 2");
        metainfo3 = new Metainfo("Text 3");
        metainfo4 = new Metainfo("Text 4");
        metainfo5 = new Metainfo("Text 5");
        metainfo6 = new Metainfo("Text 6");


        List<ChartItem> items = List.of(
                ChartItemBuilder.create().name("Item 1").value(27).fill(Color.web("#96AA3B")).metadata(metainfo1).build(),
                ChartItemBuilder.create().name("Item 2").value(27).fill(Color.web("#29A783")).metadata(metainfo2).build(),
                ChartItemBuilder.create().name("Item 3").value(27).fill(Color.web("#098AA9")).metadata(metainfo3).build(),
                ChartItemBuilder.create().name("Item 4").value(15).fill(Color.web("#62386F")).metadata(metainfo4).build(),
                ChartItemBuilder.create().name("Item 5").value(13).fill(Color.web("#89447B")).metadata(metainfo5).build(),
                ChartItemBuilder.create().name("Item 6").value(5).fill(Color.web("#EF5780")).metadata(metainfo6).build()
        );

        projectStatsChart = CoxcombChartBuilder.create()
                .items(items)
                .textColor(Color.WHITE)
                .autoTextColor(false)
                .useChartItemTextFill(false)
                .equalSegmentAngles(true)
                .order(Order.ASCENDING)
                .showPopup(false)
                .showItemName(true)
                .formatString("%.2f")
                .selectedItemFill(Color.MAGENTA)
                .build();


        projectStatsChart.setPrefSize(size, size);
        projectStatsChart.addChartEvtObserver(ChartEvt.ANY, System.out::println);

        AnchorPane.setRightAnchor(projectStatsChart, 0.0);
        AnchorPane.setBottomAnchor(projectStatsChart, 0.0);
        AnchorPane.setLeftAnchor(projectStatsChart, 0.0);
        anpOfficerStats.getChildren().add(0, projectStatsChart);

    }

    public class Metainfo implements Metadata {
        private StringProperty text;

        public Metainfo(final String text) {
            this.text = new StringPropertyBase(text) {
                @Override protected void invalidated() { super.invalidated(); }
                @Override public Object getBean() { return Metainfo.this; }
                @Override public String getName() { return "text"; }
            };
        }

        public String getText() { return text.get(); }
        public void setText(final String text) { this.text.set(text); }
        public StringProperty textProperty() { return text; }

        @Override public String toString() { return text.get(); }
    }
}
