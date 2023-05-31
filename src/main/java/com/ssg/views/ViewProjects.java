package com.ssg.views;

import com.ssg.database.*;
import com.ssg.utils.ProgramUtils;
import com.ssg.utils.RuntimeData;
import com.ssg.views.templates.ProjectsItemBox;
import com.ssg.database.models.Expense;
import com.ssg.database.models.Project;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.DoubleFilter;
import io.github.palexdev.materialfx.filter.IntegerFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import io.github.palexdev.materialfx.utils.others.observables.When;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.sql.Date;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class ViewProjects extends ViewController {
    @FXML private Pane pneViewProjectsNoProject;

    @FXML private GridPane gnpProjectsList;
    @FXML private TextField txfProjectListSearchProject;
    @FXML private Button btnProjectListClearSearch;
    @FXML private Button btnProjectListAddNew;
    @FXML private Button btnProjectListRefresh;

    @FXML private AnchorPane anpProjectDetails;
    @FXML private ImageView imvProjectDetailsBack;
    @FXML private MFXPaginatedTableView<Expense> tbvProjectDetailsExpenses;
    @FXML private Label lblProjectDetailsItemName;
    @FXML private Label lblProjectDetailsItemDescription;
    @FXML private Button btnProjectDetailsEditDetails;
    @FXML private Button btnProjectDetailsGenerateReport;
    @FXML private Button btnProjectDetailsDeleteProject;
    @FXML private Button btnProjectDetailsAddExpense;

    @FXML private AnchorPane anpProjectEditProject;
    @FXML private Label lblEditProjectDialogTitle;
    @FXML private TextField txfEditProjectName;
    @FXML private TextArea txaEditProjectDescription;
    @FXML private DatePicker dpkEditProjectDate;
    @FXML private Button btnEditProjectAction1;
    @FXML private Button btnEditProjectAction2;
    @FXML private Button btnEditProjectAction3;
    @FXML private Button btnEditProjectAction4;


    @FXML private AnchorPane anpProjectEditExpense;
    @FXML private Label lblEditExpenseDialogTitle;
    @FXML private Button btnEditExpenseAction1;
    @FXML private Button btnEditExpenseAction2;
    @FXML private Button btnEditExpenseAction3;
    @FXML private Button btnEditExpenseAction4;
    @FXML private DatePicker dpkEditExpenseDate;
    @FXML private ToggleGroup rbgEditExpenseRadioStatus;
    @FXML private RadioButton rbtEditExpenseIsApproved;
    @FXML private RadioButton rbtEditExpenseIsProposed;
    @FXML private TextField txfEditExpenseName;
    @FXML private TextField txfEditExpenseQuantity;
    @FXML private TextField txfEditExpenseTotalPrice;
    @FXML private TextField txfEditExpenseUnitPrice;

    private final String[] DBNEEDED = {"OFFICERS", "CONTRIBUTORS", "EXPENSES", "PROJECTS"};
    private Project focusedProject;

    @Override
    public void initialize() {
        ControllerUtils.EVENTBUS.register(this);

        txfProjectListSearchProject.setOnKeyReleased(this::searchProject);
        btnProjectListAddNew.setOnMouseClicked(event -> projectDialogEditor("add"));
        imvProjectDetailsBack.setOnMouseClicked(event -> projectsListBack());
        btnProjectDetailsAddExpense.setOnMouseClicked(event -> expenseDialogEditor("add"));
        btnProjectDetailsDeleteProject.setOnMouseClicked(event -> deleteProject());
        btnProjectDetailsGenerateReport.setOnMouseClicked(this::reportProject);
        tbvProjectDetailsExpenses.autosizeColumnsOnInitialization();
        btnProjectListRefresh.setOnMouseClicked(event -> forceRefreshView());
        btnProjectDetailsEditDetails.setOnMouseClicked(event -> projectDialogEditor("edit"));


        When.onChanged(tbvProjectDetailsExpenses.currentPageProperty())
                .then((oldValue, newValue) -> tbvProjectDetailsExpenses.autosizeColumns())
                .listen();

        projectDialogEditor("hide");
        expenseDialogEditor("hide");
        setupTable();
        refreshView();
    }
    @Override
    public void refreshView() {
        loadDatabase();
        displayProjects();
        displayProjectDetails();
    }
    @Override
    public void onNavigate() {

    }

    public void setupTable() {
        // TODO Transfrom into normal table to select multiple expenses
        MFXTableColumn<Expense> quantityColumn = new MFXTableColumn<>("Quantity", false, Comparator.comparing(Expense::getQuantity));
        MFXTableColumn<Expense> titleColumn = new MFXTableColumn<>("Title", false, Comparator.comparing(Expense::getTitle));
        MFXTableColumn<Expense> unitPriceColumn = new MFXTableColumn<>("Unit Price", false, Comparator.comparing(Expense::getUnitPrice));
        MFXTableColumn<Expense> statusColumn = new MFXTableColumn<>("Status", false, Comparator.comparing(Expense::getStatus));
        MFXTableColumn<Expense> expenseDateColumn = new MFXTableColumn<>("ID", false, Comparator.comparing(Expense::getExpenseDate_cd));
        MFXTableColumn<Expense> totalPriceColumn = new MFXTableColumn<>("Total Price", false, Comparator.comparing(Expense::getTotalPrice));

        quantityColumn.setRowCellFactory(expense -> new MFXTableRowCell<>(Expense::getQuantity));
        titleColumn.setRowCellFactory(expense -> new MFXTableRowCell<>(Expense::getTitle));
        unitPriceColumn.setRowCellFactory(expense -> new MFXTableRowCell<>(Expense::getUnitPrice));
        statusColumn.setRowCellFactory(expense -> new MFXTableRowCell<>(Expense::getStatus));
        expenseDateColumn.setRowCellFactory(expense -> new MFXTableRowCell<>(Expense::getExpenseDate_cd));
        totalPriceColumn.setRowCellFactory(expense -> new MFXTableRowCell<>(Expense::getTotalPrice));

        tbvProjectDetailsExpenses.getTableColumns().addAll(quantityColumn, titleColumn, unitPriceColumn, statusColumn, expenseDateColumn, totalPriceColumn);
        tbvProjectDetailsExpenses.getFilters().addAll(
                new DoubleFilter<>("Quantity", Expense::getQuantity),
                new StringFilter<>("Title", Expense::getTitle),
                new DoubleFilter<>("Unit Price", Expense::getUnitPrice),
                new IntegerFilter<>("Status", Expense::getStatus),
                new DoubleFilter<>("Total Price", Expense::getTotalPrice)
        );
        tbvProjectDetailsExpenses.getSelectionModel().selectionProperty().addListener((v, a, e) -> {
            List<Expense> selectedExpense = tbvProjectDetailsExpenses.getSelectionModel().getSelectedValues();
            if (selectedExpense != null) expenseDialogEditor("edit", selectedExpense);
        });
        tbvProjectDetailsExpenses.setCurrentPage(1);
    }
    public void displayProjects() {
        int column = 0;
        int row = 0;
        final int COLUMNLIMIT = 2;
        gnpProjectsList.getChildren().clear();
        pneViewProjectsNoProject.setVisible(projects.isEmpty());

        try {
            for (Object p : projects) {
                Project project = (Project) p;
                FXMLLoader projectItemLoader = ControllerUtils.getLoader("templates/projects-item-box");
                AnchorPane projectItemBox = projectItemLoader.load();
                ProjectsItemBox projectItemController = projectItemLoader.getController();
                projectItemController.setData(project);

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
    public void displayProjectDetails() {
        if (focusedProject == null) return;
        anpProjectDetails.setVisible(true);
        lblProjectDetailsItemName.setText(focusedProject.getTitle());
        lblProjectDetailsItemDescription.setText("Program Date: " + ProgramUtils.formatDate("2", focusedProject.getEventdate()) + "\n" + focusedProject.getDescription());

        Predicate<Object> filterExpenses = (e) -> ((Expense) e).getProject_id() == focusedProject.getProject_id();
        ObservableList<Expense> expenseList = expenses.filtered(filterExpenses).stream().filter(obj -> obj instanceof Expense).map(obj -> (Expense) obj).collect(Collectors.toCollection(FXCollections::observableArrayList));

        tbvProjectDetailsExpenses.setCurrentPage(1);
        tbvProjectDetailsExpenses.setItems(expenseList);
    }
    public void searchProject(KeyEvent event) {
        if (event.getCode() != KeyCode.ENTER) return;
        String searchOfficer = "'%" + txfProjectListSearchProject.getText() + "%'";
        refreshView();
    }
    public void projectsListBack() {
        focusedProject = null;
        anpProjectDetails.setVisible(false);
    }

    // Project Editor
    public void projectDialogEditor(String mode, Object... args) {
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
                // TODO Add Contributor Editor

                btnEditProjectAction1.setText("Update");
                btnEditProjectAction2.setText("Cancel");

                btnEditProjectAction1.setOnMouseClicked(event -> updateProject());
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
        Object[] userInput = {
                txfEditProjectName.getText(),
                txaEditProjectDescription.getText(),
                ProgramUtils.formatDate("3", Date.valueOf(dpkEditProjectDate.getValue()))
        };
        if (Arrays.stream(userInput).anyMatch(Objects::isNull) && !allowNull) {
            ControllerUtils.triggerEvent("showDialog", "message", "Invalid Input", "Invalid Input", "Back");
            return null;
        } else {
            projectDialogEditor("hide");
            return userInput;
        }
    }
    public void createProject() {
        try {
            Object[] userInput = getEditProjectInput(false);
            if (userInput == null) return;
            Object[] newProject = {
                    userInput[0],
                    userInput[1],
                    RuntimeData.USER.getUser_id(),
                    userInput[2]
            };
            SpendBCreate.createProject(newProject);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void updateProject() {
        try {
            Object[] userInput = getEditProjectInput(true);
            if (userInput == null) return;
            Object[] newProject = {
                    userInput[0],
                    userInput[1],
                    RuntimeData.USER.getUser_id(),
                    userInput[2]
            };
            SpendBUpdate.updateProject(newProject, true, "PROJECT_ID = " + focusedProject.getProject_id());
            focusedProject = SpendBRead.getProject(focusedProject.getProject_id(), projects);
            displayProjectDetails(); // Updates the details on the view
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void deleteProject() {
        SpendBDelete.deleteTableData("PROJECTS", true, "PROJECT_ID = " + focusedProject.getProject_id() );
        projectsListBack();
    }
    public void reportProject(Event ignored) {
    }

    // Expense Editor
    private void expenseDialogEditor(String mode, Object... args) {
        anpProjectEditExpense.setVisible(!Objects.equals(mode, "hide"));
        switch (mode) {
            case "add" -> {
                lblEditExpenseDialogTitle.setText("New Expense");
                btnEditExpenseAction1.setVisible(true);
                btnEditExpenseAction2.setVisible(true);

                btnEditExpenseAction1.setText("Add");
                btnEditExpenseAction2.setText("Cancel");

                btnEditExpenseAction1.setOnMouseClicked(event -> createExpense());
                btnEditExpenseAction2.setOnMouseClicked(event -> expenseDialogEditor("hide"));
            }
            case "edit" -> {
                List<Expense> selectedExpenses = (List<Expense>) args[0];
                if (selectedExpenses.size() == 1) {
                    Expense updatingExpense = selectedExpenses.get(0);
                    lblEditExpenseDialogTitle.setText("Edit Expense");
                    txfEditExpenseName.setText(updatingExpense.getTitle());
                    txfEditExpenseQuantity.setText(String.valueOf(updatingExpense.getQuantity()));
                    txfEditExpenseUnitPrice.setText(String.valueOf(updatingExpense.getUnitPrice()));
                    txfEditExpenseTotalPrice.setText(String.valueOf(updatingExpense.getTotalPrice()));
                    dpkEditExpenseDate.setValue(updatingExpense.getExpenseDate_cd().toLocalDate());
                    rbgEditExpenseRadioStatus.selectToggle(updatingExpense.getStatus() == 0 ? rbtEditExpenseIsProposed : rbtEditExpenseIsApproved);
                } else {
                    lblEditExpenseDialogTitle.setText("Edit Multiple Expenses");
                    txfEditExpenseName.setDisable(true);
                }

                btnEditExpenseAction1.setVisible(true);
                btnEditExpenseAction2.setVisible(true);
                btnEditExpenseAction3.setVisible(true);
                btnEditExpenseAction4.setVisible(true);

                btnEditExpenseAction1.setText("Update");
                btnEditExpenseAction2.setText("Delete");
                btnEditExpenseAction3.setText("Propose");
                btnEditExpenseAction4.setText("Cancel");

                btnEditExpenseAction1.setOnMouseClicked(event -> updateExpense(selectedExpenses));
                btnEditExpenseAction2.setOnMouseClicked(event -> deleteExpense(selectedExpenses));
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
                dpkEditExpenseDate.setValue(null);
                rbgEditExpenseRadioStatus.selectToggle(null);

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
        Object[] userInput = {
                txfEditExpenseName.getText(),
                ProgramUtils.parseDouble(txfEditExpenseQuantity.getText()),
                ProgramUtils.parseDouble(txfEditExpenseUnitPrice.getText()),
                ProgramUtils.parseDouble(txfEditExpenseTotalPrice.getText()),
                dpkEditExpenseDate.getValue(),
                Objects.equals(((RadioButton) rbgEditExpenseRadioStatus.getSelectedToggle()).getText(), "Proposed") ? 0 : 1
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
            Object[] userInput = getEditExpenseInput(false);
            if (userInput == null) return;
            Object[] newExpense = {
                    focusedProject.getProject_id(),
                    userInput[0],
                    userInput[1],
                    userInput[2],
                    userInput[3],
                    // TODO Record the date too userInput[4],
                    userInput[5]
            };
            SpendBCreate.createExpenses(newExpense);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void updateExpense(List<Expense> expenses) {
        try {
            Object[] userInput = getEditExpenseInput(true);
            if (userInput == null) return;
            for (Expense expense: expenses) {
                Object[] newExpense = {
                        expense.getProject_id(),
                        userInput[0],
                        userInput[1],
                        userInput[2],
                        userInput[3],
                        // TODO Record the date too userInput[4],
                        userInput[5]
                };
                SpendBUpdate.updateExpense(newExpense, true, "EXPENSE_ID = " + expense.getExpense_id());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void deleteExpense(List<Expense> expenses) {
        expenseDialogEditor("hide");
        for (Expense expense: expenses) SpendBDelete.deleteTableData("EXPENSES", true, "EXPENSE_ID = " + expense.getExpense_id());
    }
    private void proposeExpense(List<Expense> expenses) {
        String[] expenseIDS = expenses.stream().map(expense -> "EXPENSE_ID = " + expense.getExpense_id()).toArray(String[]::new);
        String query = SpendBRead.generateQuery("EXPENSE", false, expenseIDS);
        SpendBUtils.generateReport(1, query, true, "2022-2023"); // TODO Get current school year
    }

    // Setters and Getters
    public Project getFocusedProject() {
        return focusedProject;
    }

    public void setFocusedProject(Project focusedProject) {
        this.focusedProject = focusedProject;
    }
}
