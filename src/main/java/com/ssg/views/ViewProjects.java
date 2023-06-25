package com.ssg.views;

import com.google.common.eventbus.Subscribe;
import com.ssg.database.*;
import com.ssg.database.models.*;
import com.ssg.utils.DateUtils;
import com.ssg.utils.ProgramUtils;
import com.ssg.utils.RuntimeData;
import com.ssg.views.templates.ProjectsItemBox;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.sql.Date;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class ViewProjects extends ViewController {
    @FXML private AnchorPane anpView;

    @FXML private Pane pneViewProjectsNoProject;

    // Project List
    @FXML private GridPane gnpProjectsList;
    @FXML private TextField txfProjectListSearchProject;
    @FXML private Button btnProjectListClearSearch;
    @FXML private Button btnProjectListAddNew;
    @FXML private Button btnProjectListGenerateReport;

    // Project Details
    @FXML private AnchorPane anpProjectDetails;
    @FXML private ImageView imvProjectDetailsBack;
    @FXML private MFXTableView<Expense> tbvProjectDetailsExpenses;
    @FXML private Label lblProjectDetailsItemName;
    @FXML private Label lblProjectDetailsItemDescription;
    @FXML private Button btnProjectDetailsEditDetails;
    @FXML private Button btnProjectDetailsGenerateReport;
    @FXML private Button btnProjectDetailsDeleteProject;
    @FXML private Button btnProjectDetailsAddExpense;
    @FXML private Button btnProjectDetailsEditContributors;
    @FXML private HBox hbxProjectDetailsContributor;

    // Project Details Editor
    @FXML private AnchorPane anpProjectEditProject;
    @FXML private Label lblEditProjectDialogTitle;
    @FXML private TextField txfEditProjectName;
    @FXML private TextArea txaEditProjectDescription;
    @FXML private DatePicker dpkEditProjectDate;
    @FXML private Button btnEditProjectAction1;
    @FXML private Button btnEditProjectAction2;
    @FXML private Button btnEditProjectAction3;
    @FXML private Button btnEditProjectAction4;

    // Project Expenses Editor
    @FXML private AnchorPane anpProjectEditExpense;
    @FXML private Label lblEditExpenseDialogTitle;
    @FXML private Button btnEditExpenseAction1;
    @FXML private Button btnEditExpenseAction2;
    @FXML private Button btnEditExpenseAction3;
    @FXML private Button btnEditExpenseAction4;
    @FXML private ToggleGroup rbgEditExpenseRadioStatus;
    @FXML private RadioButton rbtEditExpenseIsApproved;
    @FXML private RadioButton rbtEditExpenseIsProposed;
    @FXML private TextField txfEditExpenseName;
    @FXML private TextField txfEditExpenseQuantity;
    @FXML private TextField txfEditExpenseTotalPrice;
    @FXML private TextField txfEditExpenseUnitPrice;

    private final String[] DBNEEDED = {"OFFICERS", "CONTRIBUTORS", "EXPENSES", "PROJECTS", "SCHOOLDATA"};
    private final ArrayList<Integer> searchedProjects = new ArrayList<>();
    private String searchProjectPattern = "%";
    private Project focusedProject;
    private List<Expense> selectedExpense;

    @Override
    public void initialize() {
        ControllerUtils.EVENTBUS.register(this);

        txfProjectListSearchProject.setOnKeyReleased(this::searchProject);
        btnProjectListAddNew.setOnMouseClicked(event -> projectDialogEditor("add"));
        btnProjectListClearSearch.setOnMouseClicked(this::clearSearches);
        btnProjectListGenerateReport.setOnMouseClicked(this::generateProjectsReport);

        imvProjectDetailsBack.setOnMouseClicked(event -> projectsListBack());
        btnProjectDetailsAddExpense.setOnMouseClicked(event -> expenseDialogEditor("add"));
        btnProjectDetailsDeleteProject.setOnMouseClicked(this::deleteProjectConfirm);
        btnProjectDetailsGenerateReport.setOnMouseClicked(this::generateProjectReport);
        btnProjectDetailsEditDetails.setOnMouseClicked(event -> projectDialogEditor("edit"));
        btnProjectDetailsEditContributors.setOnMouseClicked(this::showProjectDialogChoices);

        tbvProjectDetailsExpenses.autosizeColumnsOnInitialization();

        txfEditProjectName.setTextFormatter(ControllerUtils.textFormatter(30, false));
        txaEditProjectDescription.setTextFormatter(ControllerUtils.textFormatter(400, false));
        txfEditExpenseName.setTextFormatter(ControllerUtils.textFormatter(30, false));
        txfEditExpenseQuantity.setTextFormatter(ControllerUtils.textFormatter(10, true));
        txfEditExpenseTotalPrice.setTextFormatter(ControllerUtils.textFormatter(10, true));
        txfEditExpenseUnitPrice.setTextFormatter(ControllerUtils.textFormatter(10, true));

        txfEditProjectName.setOnContextMenuRequested(Event::consume);
        txaEditProjectDescription.setOnContextMenuRequested(Event::consume);
        txfEditExpenseName.setOnContextMenuRequested(Event::consume);
        txfEditExpenseQuantity.setOnContextMenuRequested(Event::consume);
        txfEditExpenseTotalPrice.setOnContextMenuRequested(Event::consume);
        txfEditExpenseUnitPrice.setOnContextMenuRequested(Event::consume);
        txfProjectListSearchProject.setOnContextMenuRequested(Event::consume);

        projectDialogEditor("hide");
        expenseDialogEditor("hide");
        setupTable();
        refreshView(true);
    }

    @Override
    public void refreshView(boolean loadDB) {
        if (loadDB) loadDatabase();
        if (focusedProject != null) focusedProject = SpendBRead.getProject(focusedProject.getProject_id(), projects);
        displayProjects();
        displayProjectDetails();
        tbvProjectDetailsExpenses.getSelectionModel().clearSelection();
    }

    @Override
    public void resetAll() {
        clearSearches(null);
        projectDialogEditor("hide");
        expenseDialogEditor("hide");
        projectsListBack();
        tbvProjectDetailsExpenses.getSelectionModel().clearSelection();
    }

    // Setups
    public void setupTable() {
        MFXTableColumn<Expense> quantityColumn = new MFXTableColumn<>("Quantity", false, Comparator.comparing(Expense::getQuantity));
        MFXTableColumn<Expense> titleColumn = new MFXTableColumn<>("Title", false, Comparator.comparing(Expense::getTitle));
        MFXTableColumn<Expense> unitPriceColumn = new MFXTableColumn<>("Unit Price", false, Comparator.comparing(Expense::getUnitPrice));
        MFXTableColumn<Expense> statusColumn = new MFXTableColumn<>("Status", false, Comparator.comparing(Expense::getStatus));
        MFXTableColumn<Expense> expenseDateColumn = new MFXTableColumn<>("Date", false, Comparator.comparing(Expense::getExpenseDate_cd));
        MFXTableColumn<Expense> totalPriceColumn = new MFXTableColumn<>("Total Price", false, Comparator.comparing(Expense::getTotalPrice));

        quantityColumn.setRowCellFactory(expense -> new MFXTableRowCell<>(Expense::getQuantity) {
            {
                setAlignment(Pos.CENTER);
                setOnMouseClicked(event -> {
                    selectedExpense = tbvProjectDetailsExpenses.getSelectionModel().getSelectedValues();
                    if (selectedExpense != null) expenseDialogEditor("edit", selectedExpense);
                });
            }
        });
        titleColumn.setRowCellFactory(expense -> new MFXTableRowCell<>(Expense::getTitle){
            {
                setAlignment(Pos.CENTER);
                setOnMouseClicked(event -> {
                    selectedExpense = tbvProjectDetailsExpenses.getSelectionModel().getSelectedValues();
                    if (selectedExpense != null) expenseDialogEditor("edit", selectedExpense);
                });
            }
        });
        unitPriceColumn.setRowCellFactory(expense -> new MFXTableRowCell<>(Expense::getUnitPrice){
            {
                setAlignment(Pos.CENTER);
                setOnMouseClicked(event -> {
                    selectedExpense = tbvProjectDetailsExpenses.getSelectionModel().getSelectedValues();
                    if (selectedExpense != null) expenseDialogEditor("edit", selectedExpense);
                });
            }
        });
        statusColumn.setRowCellFactory(expense -> new MFXTableRowCell<>(factoryExpense -> factoryExpense.getStatus() == 0 ? "Proposed" : "Approved"){
            {
                setAlignment(Pos.CENTER);
                setOnMouseClicked(event -> {
                    selectedExpense = tbvProjectDetailsExpenses.getSelectionModel().getSelectedValues();
                    if (selectedExpense != null) expenseDialogEditor("edit", selectedExpense);
                });
            }
        });
        expenseDateColumn.setRowCellFactory(expense -> new MFXTableRowCell<>(Expense::getExpenseDate_cd){
            {
                setAlignment(Pos.CENTER);
                setOnMouseClicked(event -> {
                    selectedExpense = tbvProjectDetailsExpenses.getSelectionModel().getSelectedValues();
                    if (selectedExpense != null) expenseDialogEditor("edit", selectedExpense);
                });
            }
        });
        totalPriceColumn.setRowCellFactory(expense -> new MFXTableRowCell<>(Expense::getTotalPrice){
            {
                setAlignment(Pos.CENTER);
                setOnMouseClicked(event -> {
                    selectedExpense = tbvProjectDetailsExpenses.getSelectionModel().getSelectedValues();
                    if (selectedExpense != null) expenseDialogEditor("edit", selectedExpense);
                });
            }
        });

        quantityColumn.setAlignment(Pos.CENTER_RIGHT);
        titleColumn.setAlignment(Pos.CENTER);
        unitPriceColumn.setAlignment(Pos.CENTER);
        statusColumn.setAlignment(Pos.CENTER);
        expenseDateColumn.setAlignment(Pos.CENTER);
        totalPriceColumn.setAlignment(Pos.CENTER);

        tbvProjectDetailsExpenses.getTableColumns().addAll(quantityColumn, titleColumn, unitPriceColumn, statusColumn, expenseDateColumn, totalPriceColumn);
        /* tbvProjectDetailsExpenses.getFilters().addAll(
                new DoubleFilter<>("Quantity", Expense::getQuantity),
                new StringFilter<>("Title", Expense::getTitle),
                new DoubleFilter<>("Unit Price", Expense::getUnitPrice),
                new StringFilter<>("Status", expense -> expense.getStatus() == 0 ? "Proposed" : "Approved"),
                new DoubleFilter<>("Total Price", Expense::getTotalPrice)
        ); */
        tbvProjectDetailsExpenses.setFooterVisible(false);
    }

    // Project List
    public void displayProjects() {
        int column = 0;
        int row = 0;
        final int COLUMNLIMIT = 2;
        gnpProjectsList.getChildren().clear();
        searchedProjects.clear();
        try {
            ArrayList<Project> filteredProject = new ArrayList<>();
            ArrayList<Project> strongFilterProject = new ArrayList<>();
            ArrayList<Project> moderateFilterProject = new ArrayList<>();
            ArrayList<Project> weakFilterProject = new ArrayList<>();

            for (Object p : projects) {
                Project project = (Project) p;
                if (schoolData.isCurrentSchoolYear() && schoolData.getSchoolYear() > DateUtils.getYear(project.getProject_cd())) continue;
                int matchStrength = ProgramUtils.lowestNumber(
                        ProgramUtils.stringMatch(DateUtils.formatDate("5", project.getEventdate()), searchProjectPattern), // Event Year
                        ProgramUtils.stringMatch(DateUtils.formatDate("6", project.getEventdate()), searchProjectPattern), // Event Month
                        ProgramUtils.stringMatch(project.getTitle(), "%" + searchProjectPattern + "%"),
                        ProgramUtils.stringMatch(project.getDescription(), "%" + searchProjectPattern + "%")
                );
                switch (matchStrength) {
                    case 1 -> strongFilterProject.add(project);
                    case 2 -> moderateFilterProject.add(project);
                    case 3 -> weakFilterProject.add(project);
                }
                if (matchStrength != 4) searchedProjects.add(project.getProject_id());
            }
            filteredProject.addAll(strongFilterProject);
            filteredProject.addAll(moderateFilterProject);
            filteredProject.addAll(weakFilterProject);

            pneViewProjectsNoProject.setVisible(filteredProject.isEmpty());
            for (Project project : filteredProject) {
                FXMLLoader projectItemLoader = ControllerUtils.getLoader("templates/projects-item-box");
                AnchorPane projectItemBox = projectItemLoader.load();
                ProjectsItemBox projectItemController = projectItemLoader.getController();

                Predicate<Object> filterExpenses = (e) -> ((Expense) e).getProject_id() == project.getProject_id();
                ObservableList<Expense> expenseList = expenses.filtered(filterExpenses).stream().filter(obj -> obj instanceof Expense).map(obj -> (Expense) obj).collect(Collectors.toCollection(FXCollections::observableArrayList));

                Predicate<Expense> filterApproved = (e) -> e.getStatus() == 1;
                double expenseApprovalRate = ((double) expenseList.filtered(filterApproved).size()) / expenseList.size();

                projectItemController.setData(project, expenseApprovalRate);
                projectItemBox.setOnMouseClicked(event -> {
                    focusedProject = project;
                    displayProjectDetails();
                });
                gnpProjectsList.add(projectItemBox, column++, row);
                GridPane.setMargin(projectItemBox, new Insets(10));
                if (column != COLUMNLIMIT) continue;
                column = 0;
                ++row;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void searchProject(KeyEvent event) {
        if (event.getCode() != KeyCode.ENTER) return;
        String searchValue = txfProjectListSearchProject.getText();
        searchProjectPattern = searchValue.isEmpty() ? "%" : searchValue;
        refreshView(false);
    }
    private void clearSearches(MouseEvent mouseEvent) {
        txfProjectListSearchProject.setText("");
        txfProjectListSearchProject.getParent().requestFocus();
        searchProjectPattern = "%";
        refreshView(false);
    }
    private void generateProjectsReport(MouseEvent mouseEvent) {
        MainEvents.startLoading();
        String filter = SpendBUtils.spendBFilterID("P.PROJECT_ID", true, searchedProjects.stream().mapToInt(Integer::intValue).toArray());
        Map<String, String> queries = new HashMap<>();

        String query = "SELECT\n" +
                "    SD.DATA_ID,\n" +
                "    SD.UPDATETIME,\n" +
                "    SD.SCHOOLYEAR,\n" +
                "    SD.SCHOOLLOGO,\n" +
                "    SD.SSGLOGO,\n" +
                "    P.PROJECT_ID,\n" +
                "    P.TITLE,\n" +
                "    P.DESCRIPTION,\n" +
                "    P.USER_ID,\n" +
                "    P.PROJECT_CD,\n" +
                "    P.EVENTDATE,\n" +
                "    E.TOTAL_EXPENSE,\n" +
                "    C.CONTRIBUTOR_COUNT,\n" +
                "    ROW_NUMBER() OVER (ORDER BY P.PROJECT_ID) AS PROJECT_NUMBER\n" +
                "FROM\n" +
                "    SCHOOLDATA SD\n" +
                "JOIN\n" +
                "    PROJECTS P \n" +
                "LEFT JOIN\n" +
                "    (\n" +
                "        SELECT\n" +
                "            PROJECT_ID,\n" +
                "            SUM(TOTALPRICE) AS TOTAL_EXPENSE\n" +
                "        FROM\n" +
                "            EXPENSES\n" +
                "        GROUP BY\n" +
                "            PROJECT_ID\n" +
                "    ) E ON P.PROJECT_ID = E.PROJECT_ID\n" +
                "LEFT JOIN\n" +
                "    (\n" +
                "        SELECT\n" +
                "            PROJECT_ID,\n" +
                "            COUNT(OFFICER_ID) AS CONTRIBUTOR_COUNT\n" +
                "        FROM\n" +
                "            CONTRIBUTORS\n" +
                "        GROUP BY\n" +
                "            PROJECT_ID\n" +
                "    ) C ON P.PROJECT_ID = C.PROJECT_ID\n" +
                "WHERE\n" +
                filter;

        queries.put("main", query);
        queries.put("Projects", query);
        SpendBUtils.generateReport(5, queries);
        MainEvents.stopLoading();
    }

    // Project Details
    public void displayProjectDetails() {
        if (focusedProject == null) return;
        anpProjectDetails.setVisible(true);
        lblProjectDetailsItemName.setText(focusedProject.getTitle());
        lblProjectDetailsItemDescription.setText("Program Date: " + DateUtils.formatDate("2", focusedProject.getEventdate()) + "\n" + focusedProject.getDescription());
        hbxProjectDetailsContributor.getChildren().clear();

        Predicate<Object> filterExpenses = (e) -> ((Expense) e).getProject_id() == focusedProject.getProject_id();
        ObservableList<Expense> expenseList = expenses.filtered(filterExpenses).stream().filter(obj -> obj instanceof Expense).map(obj -> (Expense) obj).collect(Collectors.toCollection(FXCollections::observableArrayList));

        Predicate<Object> filterContributions = (c) -> ((Contributors) c).getProject_id() == focusedProject.getProject_id();
        ObservableList<Object> contributorsID = contributors.filtered(filterContributions);
        Predicate<Object> filterOfficers = (o) -> contributorsID.stream().anyMatch(c -> ((Contributors) c).getOfficer_id() == ((Officer) o).getOfficer_id());
        ObservableList<Object> officerList = officers.filtered(filterOfficers);

        for (int i = 0; i < officerList.size(); i++) {
            Object o = officerList.get(i);
            Officer officer = (Officer) o;
            Image officerImage = SpendBUtils.loadBlob(officer.getAvatar());
            ImageView imvOfficerImage = new ImageView(officerImage);
            imvOfficerImage.setFitHeight(36);
            imvOfficerImage.setFitWidth(36);
            hbxProjectDetailsContributor.getChildren().add(imvOfficerImage);
            imvOfficerImage.setOnMouseClicked(event -> MainEvents.focusOfficer(officer));
            imvOfficerImage.setCursor(Cursor.HAND);
            if (i == 0) continue;
            HBox.setMargin(imvOfficerImage, new Insets(0, 0, 0, -10 - Math.max(0, officerList.size() - 10)));
        }
        tbvProjectDetailsExpenses.setItems(expenseList);
    }
    public void projectsListBack() {
        focusedProject = null;
        anpProjectDetails.setVisible(false);
        projectDialogEditor("hide");
        expenseDialogEditor("hide");
        tbvProjectDetailsExpenses.getSelectionModel().clearSelection();
    }
    private void showProjectDialogChoices(MouseEvent mouseEvent) {
        if (notAdmin()) return;
        // FIXME Not showing when officer doens't have a project
        // Filter the Contributors of this Project
        Predicate<Object> filterContributions = (c) -> ((Contributors) c).getProject_id() == focusedProject.getProject_id();
        ObservableList<Object> contributorsID = contributors.filtered(filterContributions);
        // Filter the Officers that Contributes to this project
        Predicate<Object> filterProject = (o) -> contributorsID.stream().anyMatch(c -> ((Contributors) c).getOfficer_id() == ((Officer) o).getOfficer_id());
        ObservableList<Object> projectOfficers = officers.filtered(filterProject);
        Object[] selectedIDs = projectOfficers.stream().map(o -> ((Officer) o).getOfficer_id()).toArray();
        Object[][] aArgs = {selectedIDs, officers.toArray(new Object[0])};
        Object[] sArgs = {"choice", "officersChoices"};
        ControllerUtils.triggerEvent("showDialog", sArgs, aArgs);
    }
    private void deleteProjectConfirm(MouseEvent event) {
        if (notAdmin()) return;
        MainEvents.showDialogMessage("Edit Project", "Are you sure you want to delete this project", "Delete Project", "Back");
    }

    // Project Editor
    public void projectDialogEditor(String mode) {
        if (!Objects.equals(mode, "hide") && notAdmin()) return;
        anpProjectEditProject.setVisible(!Objects.equals(mode, "hide"));
        switch (mode) {
            case "add" -> {
                lblEditProjectDialogTitle.setText("Add Project");
                btnEditProjectAction1.setVisible(true);
                btnEditProjectAction2.setVisible(true);

                btnEditProjectAction1.setText("Add");
                btnEditProjectAction2.setText("Cancel");

                btnEditProjectAction1.setOnMouseClicked(event -> createProject());
                btnEditProjectAction2.setOnMouseClicked(event -> projectDialogEditor("hide"));
            }
            case "edit" -> {
                lblEditProjectDialogTitle.setText("Edit Project");
                btnEditProjectAction1.setVisible(true);
                btnEditProjectAction2.setVisible(true);

                txfEditProjectName.setText(focusedProject.getTitle());
                txaEditProjectDescription.setText(focusedProject.getDescription());
                dpkEditProjectDate.setValue(focusedProject.getEventdate().toLocalDate());

                btnEditProjectAction1.setText("Update");
                btnEditProjectAction2.setText("Cancel");

                btnEditProjectAction1.setOnMouseClicked(event -> MainEvents.showDialogMessage("Edit Project", "Are you sure you want to edit the details of this project", "Edit Project", "Back"));
                btnEditProjectAction2.setOnMouseClicked(event -> projectDialogEditor("hide"));

                txfEditProjectName.setDisable(false);
            }
            case "hide" -> {
                lblEditProjectDialogTitle.setText("");
                btnEditProjectAction1.setVisible(false);
                btnEditProjectAction2.setVisible(false);
                btnEditProjectAction3.setVisible(false);
                btnEditProjectAction4.setVisible(false);

                txfEditProjectName.setText("");
                txaEditProjectDescription.setText("");
                dpkEditProjectDate.setValue(null);

                btnEditProjectAction1.setText("");
                btnEditProjectAction2.setText("");
                btnEditProjectAction3.setText("");
                btnEditProjectAction4.setText("");

                btnEditProjectAction1.setOnMouseClicked(null);
                btnEditProjectAction2.setOnMouseClicked(null);
                btnEditProjectAction3.setOnMouseClicked(null);
                btnEditProjectAction4.setOnMouseClicked(null);

                txfEditProjectName.setDisable(false);
            }
        }
    }
    public Object[] getEditProjectInput(boolean allowNull) {
        dpkEditProjectDate.setValue(dpkEditProjectDate.getConverter().fromString(dpkEditProjectDate.getEditor().getText()));
        Object[] userInput = {
                txfEditProjectName.getText().isEmpty() ? null : txfEditProjectName.getText(),
                txaEditProjectDescription.getText().isEmpty() ? null : txaEditProjectDescription.getText(),
                DateUtils.formatDate("3", Date.valueOf(dpkEditProjectDate.getValue()))
        };
        if (Arrays.stream(userInput).anyMatch(Objects::isNull) && !allowNull) {
            MainEvents.showDialogMessage("Invalid User Input", "The input provided is invalid. Please make sure to enter a valid value or format to proceed. Input", "Back");
            return null;
        } else {
            projectDialogEditor("hide");
            return userInput;
        }
    }
    public void createProject() {
        try {
            MainEvents.startLoading();
            Object[] userInput = getEditProjectInput(false);
            if (userInput == null) return;
            Object[] newProject = {
                    userInput[0],
                    userInput[1],
                    RuntimeData.USER.getUser_id(),
                    userInput[2]
            };
            SpendBCreate.createProject(newProject, true);
            MainEvents.stopLoading();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void generateProjectReport(Event ignored) {
        MainEvents.startLoading();
        Map<String, String> queries = new HashMap<>();
        String query = "SELECT\n" +
                "    SD.DATA_ID,\n" +
                "    SD.UPDATETIME,\n" +
                "    SD.SCHOOLYEAR,\n" +
                "    SD.SCHOOLLOGO,\n" +
                "    SD.SSGLOGO,\n" +
                "    P.PROJECT_ID,\n" +
                "    P.TITLE,\n" +
                "    P.DESCRIPTION,\n" +
                "    P.USER_ID,\n" +
                "    P.PROJECT_CD,\n" +
                "    P.EVENTDATE,\n" +
                "    E.EXPENSE_ID,\n" +
                "    E.ITEMNAME,\n" +
                "    E.TOTALPRICE,\n" +
                "    E.EXPENSEDATE_CD,\n" +
                "    E.QUANTITY,\n" +
                "    E.UNITPRICE,\n" +
                "    E.STATUS,\n" +
                "    E.UPDATETIME AS EXPENSE_UPDATETIME\n" +
                "FROM\n" +
                "    schooldata SD\n" +
                "    LEFT JOIN projects P ON 1 = 1\n" +
                "    LEFT JOIN expenses E ON P.PROJECT_ID = E.PROJECT_ID\n" +
                "WHERE\n" +
                " \tP.PROJECT_ID = '" + focusedProject.getProject_id() + "'\n" +
                "    AND E.PROJECT_ID = '" + focusedProject.getProject_id() + "'";

        queries.put("main", query);
        queries.put("Expenses", query);
        SpendBUtils.generateReport(4, queries);
        MainEvents.stopLoading();
    }

    // Expense Editor
    private void expenseDialogEditor(String mode, Object... args) {
        if (!Objects.equals(mode, "hide") && notAdmin()) return;
        anpProjectEditExpense.setVisible(!Objects.equals(mode, "hide"));
        switch (mode) {
            case "add" -> {
                lblEditExpenseDialogTitle.setText("Add Expense");
                btnEditExpenseAction1.setVisible(true);
                btnEditExpenseAction2.setVisible(true);

                btnEditExpenseAction1.setText("Add");
                btnEditExpenseAction2.setText("Cancel");

                btnEditExpenseAction1.setOnMouseClicked(event -> createExpense());
                btnEditExpenseAction2.setOnMouseClicked(event -> expenseDialogEditor("hide"));
            }
            case "edit" -> {
                List<Expense> selectedExpenses = (List<Expense>) args[0];
                lblEditExpenseDialogTitle.setText("Edit Expense");
                if (selectedExpenses.size() == 1) {
                    Expense updatingExpense = selectedExpenses.get(0);
                    txfEditExpenseName.setText(updatingExpense.getTitle());
                    txfEditExpenseQuantity.setText(String.valueOf(updatingExpense.getQuantity()));
                    txfEditExpenseUnitPrice.setText(String.valueOf(updatingExpense.getUnitPrice()));
                    txfEditExpenseTotalPrice.setText(String.valueOf(updatingExpense.getTotalPrice()));
                    rbgEditExpenseRadioStatus.selectToggle(updatingExpense.getStatus() == 0 ? rbtEditExpenseIsProposed : rbtEditExpenseIsApproved);
                } else {
                    txfEditExpenseName.setText("Selected Multiple");
                    txfEditExpenseQuantity.setText("0.0");
                    txfEditExpenseUnitPrice.setText("0.0");
                    txfEditExpenseTotalPrice.setText("0.0");
                    rbgEditExpenseRadioStatus.selectToggle(rbtEditExpenseIsProposed);

                    txfEditExpenseName.setDisable(true);
                    txfEditExpenseQuantity.setDisable(true);
                    txfEditExpenseUnitPrice.setDisable(true);
                    txfEditExpenseTotalPrice.setDisable(true);
                }
                btnEditExpenseAction1.setVisible(true);
                btnEditExpenseAction2.setVisible(true);
                btnEditExpenseAction3.setVisible(true);
                btnEditExpenseAction4.setVisible(true);

                btnEditExpenseAction1.setText("Update");
                btnEditExpenseAction2.setText("Delete");
                btnEditExpenseAction3.setText("Propose");
                btnEditExpenseAction4.setText("Cancel");

                btnEditExpenseAction1.setOnMouseClicked(this::editExpenseConfirm);
                btnEditExpenseAction2.setOnMouseClicked(this::deleteExpenseConfirm);
                btnEditExpenseAction3.setOnMouseClicked(event -> proposeExpense(selectedExpenses));
                btnEditExpenseAction4.setOnMouseClicked(event -> expenseDialogEditor("hide"));
            }
            case "hide" -> {
                lblEditExpenseDialogTitle.setText("");
                btnEditExpenseAction1.setVisible(false);
                btnEditExpenseAction2.setVisible(false);
                btnEditExpenseAction3.setVisible(false);
                btnEditExpenseAction4.setVisible(false);

                txfEditExpenseName.setText("");
                txfEditExpenseQuantity.setText("");
                txfEditExpenseUnitPrice.setText("");
                txfEditExpenseTotalPrice.setText("");
                rbgEditExpenseRadioStatus.selectToggle(null);

                txfEditExpenseName.setDisable(false);
                txfEditExpenseQuantity.setDisable(false);
                txfEditExpenseUnitPrice.setDisable(false);
                txfEditExpenseTotalPrice.setDisable(false);

                btnEditExpenseAction1.setText("");
                btnEditExpenseAction2.setText("");
                btnEditExpenseAction3.setText("");
                btnEditExpenseAction4.setText("");

                btnEditExpenseAction1.setOnMouseClicked(null);
                btnEditExpenseAction2.setOnMouseClicked(null);
                btnEditExpenseAction3.setOnMouseClicked(null);
                btnEditExpenseAction4.setOnMouseClicked(null);

                txfEditExpenseName.setDisable(false);
            }
        }
    }
    private Object[] getEditExpenseInput(boolean allowNull) {
        System.out.println(rbtEditExpenseIsProposed.isSelected());
        Object[] userInput = {
                txfEditExpenseName.getText().isEmpty() ? null: txfEditExpenseName.getText(),
                ProgramUtils.parseDouble(txfEditExpenseTotalPrice.getText()),
                ProgramUtils.parseDouble(txfEditExpenseQuantity.getText()),
                ProgramUtils.parseDouble(txfEditExpenseUnitPrice.getText()),
                rbtEditExpenseIsProposed.isSelected() ? 0 : 1
        };
        if (Arrays.stream(userInput).anyMatch(Objects::isNull) && !allowNull) {
            ControllerUtils.triggerEvent("showDialog", "message", "Invalid Input", "Invalid Input", "Back");
            return null;
        } else {
            expenseDialogEditor("hide");
            return userInput;
        }
    }
    private void createExpense() {
        try {
            MainEvents.startLoading();
            Object[] userInput = getEditExpenseInput(false);
            if (userInput == null) return;
            Object[] newExpense = {
                    focusedProject.getProject_id(),
                    userInput[0],
                    userInput[1],
                    userInput[2],
                    userInput[3],
                    userInput[4]
            };
            SpendBCreate.createExpenses(newExpense, true);
            MainEvents.stopLoading();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void proposeExpense(List<Expense> expenses) {
        MainEvents.startLoading();
        String filter = SpendBUtils.spendBFilterID("e.EXPENSE_ID", true, expenses.stream().mapToInt(Expense::getExpense_id).toArray());
        String query = "SELECT\n" +
                "  sd.DATA_ID,\n" +
                "  sd.SCHOOLYEAR,\n" +
                "  sd.SCHOOLLOGO,\n" +
                "  sd.SSGLOGO,\n" +
                "  p.PROJECT_ID,\n" +
                "  p.TITLE,\n" +
                "  p.DESCRIPTION,\n" +
                "  p.USER_ID,\n" +
                "  p.PROJECT_CD,\n" +
                "  p.EVENTDATE,\n" +
                "  e.EXPENSE_ID,\n" +
                "  e.ITEMNAME,\n" +
                "  e.TOTALPRICE,\n" +
                "  e.EXPENSEDATE_CD,\n" +
                "  e.QUANTITY,\n" +
                "  e.UNITPRICE,\n" +
                "  e.STATUS,\n" +
                "  e.UPDATETIME,\n" +
                "  ROW_NUMBER() OVER (PARTITION BY e.PROJECT_ID ORDER BY e.EXPENSE_ID) AS EXPENSES_NO\n" +
                "FROM\n" +
                "  schooldata sd\n" +
                "  LEFT JOIN projects p ON p.PROJECT_ID = " + focusedProject.getProject_id() + "\n" +
                "  LEFT JOIN expenses e ON " + filter;
        Map<String, String> queries = new HashMap<>();
        queries.put("EXPENSES", query);
        queries.put("main", query);
        SpendBUtils.generateReport(1, queries);
        MainEvents.stopLoading();
    }
    private void editExpenseConfirm(MouseEvent event) {
        if (notAdmin()) return;
        MainEvents.showDialogMessage("Update Expense", "Are you sure you want to update the data of the selected expenses", "Update Expenses", "Cancel");
    }
    private void deleteExpenseConfirm(MouseEvent event) {
        if (notAdmin()) return;
        MainEvents.showDialogMessage("Delete Expense", "Are you sure you want to delete the selected expenses", "Delete Expenses", "Cancel");
    }

    // Public Events
    @Subscribe
    public void updateProject(ControllerEvent event) {
        if (!Objects.equals(event.getEventId(), "Edit Project")) return;
        MainEvents.startLoading();
        try {
            Object[] userInput = getEditProjectInput(true);
            if (userInput == null) return;
            Object[] newProject = {
                    userInput[0],
                    userInput[1],
                    RuntimeData.USER.getUser_id(),
                    userInput[2]
            };
            SpendBUpdate.updateProject(newProject, true, true, "PROJECT_ID = " + focusedProject.getProject_id());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Subscribe
    public void deleteProject(ControllerEvent event) {
        if (!Objects.equals(event.getEventId(), "Delete Project")) return;
        MainEvents.startLoading();
        SpendBDelete.deleteTableData("PROJECTS", true, "PROJECT_ID = " + focusedProject.getProject_id());
        projectsListBack();
    }

    @Subscribe
    public void updateOfficer(ControllerEvent event) {
        if (event.notEvent("officersChoices")) return;
        MainEvents.startLoading();
        String projectFilter = "PROJECT_ID = " + focusedProject.getProject_id();
        int[] selectedID = Arrays.stream(event.getSimpleArgs()).mapToInt(obj -> Integer.parseInt(obj.toString())).toArray();
        String deleteIDS = SpendBUtils.spendBFilterID("OFFICER_ID", false, selectedID);
        SpendBDelete.deleteTableData("CONTRIBUTORS", true, projectFilter, deleteIDS);
        for (int x : selectedID) {
            try {
                SpendBCreate.createContributors(new Object[]{focusedProject.getProject_id(), x}, false);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        ControllerUtils.triggerEvent("refreshViews");
    }

    @Subscribe
    public void deleteExpense(ControllerEvent event) {
        if (event.notEvent("Delete Expenses")) return;
        MainEvents.startLoading();
        expenseDialogEditor("hide");
        int[] expenseIDs = Arrays.stream(selectedExpense.stream().mapToInt(Expense::getExpense_id).toArray()).toArray();
        String filter = SpendBUtils.spendBFilterID("EXPENSE_ID", true, expenseIDs);
        SpendBDelete.deleteTableData("EXPENSES", true, filter);
    }

    @Subscribe
    public void updateExpense(ControllerEvent event) {
        if (event.notEvent("Update Expenses")) return;
        try {
            MainEvents.startLoading();
            Object[] userInput = getEditExpenseInput(true);
            if (userInput == null) return;
            int[] expenseIDs = Arrays.stream(selectedExpense.stream().mapToInt(Expense::getExpense_id).toArray()).toArray();
            boolean multipleExpense = selectedExpense.size() > 1;
            String filter = SpendBUtils.spendBFilterID("EXPENSE_ID", true, expenseIDs);
            Object[] newExpense = {
                    focusedProject.getProject_id(),
                    multipleExpense ? null : userInput[0],
                    multipleExpense ? null : userInput[1],
                    multipleExpense ? null : userInput[2],
                    multipleExpense ? null : userInput[3],
                    userInput[4]
            };
            SpendBUpdate.updateExpense(newExpense, true, true, filter);
            MainEvents.stopLoading();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Setters and Getters
    public Project getFocusedProject() {
        return focusedProject;
    }

    public void setFocusedProject(Project focusedProject) {
        this.focusedProject = focusedProject;
    }
}
