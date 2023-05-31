package com.ssg.views;

import com.ssg.database.SpendBCreate;
import com.ssg.database.SpendBDelete;
import com.ssg.database.SpendBRead;
import com.ssg.database.SpendBUpdate;
import com.ssg.utils.ProgramUtils;
import com.ssg.utils.RuntimeData;
import com.ssg.views.templates.OfficersProfileBox;
import com.ssg.views.templates.OfficersProjectBox;
import com.ssg.database.models.Contributors;
import com.ssg.database.models.Expense;
import com.ssg.database.models.Officer;
import com.ssg.database.models.Project;
import javafx.collections.ObservableList;
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
import javafx.scene.layout.VBox;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

public class ViewOfficers extends ViewController {
    @FXML AnchorPane anpOfficerProfile;
    @FXML Pane pneViewOfficerNoOfficer;
    @FXML Pane pneViewOfficerNoProject;
    @FXML GridPane gpnOfficersList;
    @FXML VBox vbxProjectsList;
    @FXML TextField txfSearchOfficer;
    @FXML TextField txfOfficerProfileName;
    @FXML TextField txfOfficerProfilePosition;
    @FXML private Button btnOfficerListClearSearch;
    @FXML private Button btnOfficerListAddNew;
    @FXML private Button btnOfficerListRefresh;
    @FXML TextArea txaOfficerProfileDescription;
    @FXML ImageView imvOfficerProfileBack;
    @FXML ImageView imvOfficerProfileAvatar;

    @FXML private Button btnOfficerDetailsEditDetails;
    @FXML private Button btnOfficerDetailsGenerateReport;
    @FXML private Button btnOfficerDetailsAddProject;
    @FXML private Button btnOfficerDetailsDeleteOfficer;

    @FXML private AnchorPane anpOfficerEditOfficer;
    @FXML private Label lblEditOfficerDialogTitle;
    @FXML private TextField txfEditOfficerName;
    @FXML private TextArea txaEditOfficerDescription;
    @FXML private TextField txfEditOfficerPosition;
    @FXML private TextField txfEditOfficerTerm;
    @FXML private ComboBox<String> cbxEditOfficerStrand;
    @FXML private Button btnEditOfficerAction1;
    @FXML private Button btnEditOfficerAction2;
    @FXML private Button btnEditOfficerAction3;
    @FXML private Button btnEditOfficerAction4;

    private final String[] DBNEEDED = {"OFFICERS", "CONTRIBUTORS", "EXPENSES", "projects"};
    private String[] filters = new String[0];
    private boolean filterAll = false;
    private String selectedImage = null;
    private Officer focusedOfficer = null;

    @Override
    public void initialize() {
        ControllerUtils.EVENTBUS.register(this);
        txfSearchOfficer.setOnKeyReleased(this::searchOfficers);
        imvOfficerProfileBack.setOnMouseClicked(event -> officerProfileBack());
        btnOfficerListAddNew.setOnMouseClicked(event -> officerDialogEditor("add"));
        btnOfficerDetailsEditDetails.setOnMouseClicked(event -> officerDialogEditor("edit"));
        btnOfficerDetailsDeleteOfficer.setOnMouseClicked(event -> deleteOfficer());

        // TODO Make the cbx strand editable
        cbxEditOfficerStrand.getItems().addAll("GAS", "HUMSS", "ICT", "HE");
        officerDialogEditor("hide");
        refreshView();
    }
    @Override
    public void refreshView() {
        loadDatabase();
        displayOfficers();
    }
    @Override
    public void onNavigate() {
    }

    public void displayOfficers() {
        // TODO Default Year on Launch
        int column = 0;
        int row = 0;
        final int COLUMNLIMIT = 3;
        gpnOfficersList.getChildren().clear();
        pneViewOfficerNoOfficer.setVisible(officers.isEmpty());
        try {
            for (Object o : officers) {
                Officer officer = (Officer) o;
                FXMLLoader officerProfileLoader = ControllerUtils.getLoader("templates/officers-profile-box");
                AnchorPane officerProfileBox = officerProfileLoader.load();
                OfficersProfileBox officerProfileController = officerProfileLoader.getController();

                Predicate<Object> filterProject = (c) -> ((Contributors) c).getOfficer_id() == officer.getOfficer_id();
                ObservableList<Object> officerProjects = contributors.filtered(filterProject);
                officerProfileController.setData(officer, officerProjects.size());

                officerProfileBox.setOnMouseClicked(event -> {
                    focusedOfficer = officer;
                    displayOfficerDetails();
                });
                gpnOfficersList.add(officerProfileBox, column++, row);
                GridPane.setMargin(officerProfileBox, new Insets(5));
                if (column != COLUMNLIMIT) continue;
                column = 0;
                ++row;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void displayOfficerDetails() {
        if (focusedOfficer == null) {
            officerProfileBack();
            return;
        }
        anpOfficerProfile.setVisible(true);
        txfOfficerProfileName.setText(focusedOfficer.getFullName());
        txfOfficerProfilePosition.setText(focusedOfficer.getPosition());
        txaOfficerProfileDescription.setText(focusedOfficer.getDescription());
        imvOfficerProfileAvatar.setImage(ControllerUtils.loadBlob(focusedOfficer.getAvatar()));

        Predicate<Object> filterContributions = (c) -> ((Contributors) c).getOfficer_id() == focusedOfficer.getOfficer_id();
        ObservableList<Object> contributorsID = contributors.filtered(filterContributions);
        Predicate<Object> filterProject = (p) -> contributorsID.stream().anyMatch(c -> ((Contributors) c).getProject_id() == ((Project) p).getProject_id());
        ObservableList<Object> officerProjects = projects.filtered(filterProject);
        pneViewOfficerNoProject.setVisible(officerProjects.isEmpty());

        try {
            for (Object p : officerProjects) {
                Project project = (Project) p;
                FXMLLoader officerProjectLoader = ControllerUtils.getLoader("templates/officers-project-box");
                AnchorPane officerProjectBox = officerProjectLoader.load();
                OfficersProjectBox officerProfileController = officerProjectLoader.getController();

                Predicate<Object> filterExpenses = (c) -> ((Expense) c).getProject_id() == project.getProject_id();
                AtomicReference<Double> expense = new AtomicReference<>(0.0);
                expenses.filtered(filterExpenses).forEach(e -> expense.updateAndGet(v -> v + ((Expense) e).getTotalPrice()));
                officerProfileController.setData(project, expense.get());
                VBox.setMargin(officerProjectBox, new Insets(15));
                vbxProjectsList.getChildren().add(officerProjectBox);
                officerProjectBox.setOnMouseClicked(event -> ControllerUtils.triggerEvent("focusProject", project));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void searchOfficers(KeyEvent event) {
        // FIXME Change to filters
        if (event.getCode() != KeyCode.ENTER) return;
        String searchOfficer = "'%" + txfSearchOfficer.getText() + "%'";
        filters = new String[]{
                "FIRSTNAME LIKE " + searchOfficer,
                "MIDDLEINITIAL LIKE " + searchOfficer,
                "LASTNAME LIKE " + searchOfficer,
                "DESCRIPTION LIKE " + searchOfficer,
                "POSITION LIKE " + searchOfficer,
                "STRAND LIKE " + searchOfficer,
                "TERM LIKE " + searchOfficer
        };
        refreshView();
    }
    public void officerProfileBack() {
        vbxProjectsList.getChildren().clear();
        anpOfficerProfile.setVisible(false);
    }
    public void deleteOfficer() {
        SpendBDelete.deleteTableData("OFFICERS", true, "OFFICER_ID = " + focusedOfficer.getOfficer_id() );
        officerProfileBack();
    }

    // Officer Editor
    public void officerDialogEditor(String mode, Object... args) {
        anpOfficerEditOfficer.setVisible(!Objects.equals(mode, "hide"));
        switch (mode) {
            case "add" -> {
                lblEditOfficerDialogTitle.setText("Add Officer");
                btnEditOfficerAction1.setVisible(true);
                btnEditOfficerAction2.setVisible(true);
                btnEditOfficerAction3.setVisible(true);

                btnEditOfficerAction1.setText("Add");
                btnEditOfficerAction2.setText("Cancel");
                btnEditOfficerAction3.setText("Add Image");

                btnEditOfficerAction1.setOnMouseClicked(event -> createOfficer());
                btnEditOfficerAction2.setOnMouseClicked(event -> officerDialogEditor("hide"));
                btnEditOfficerAction3.setOnMouseClicked(event -> selectedImage = ProgramUtils.storeImage(ControllerUtils.getStage(btnEditOfficerAction1), true));
            }
            case "edit" -> {
                lblEditOfficerDialogTitle.setText("Edit Officer");
                btnEditOfficerAction1.setVisible(true);
                btnEditOfficerAction2.setVisible(true);
                btnEditOfficerAction3.setVisible(true);

                txfEditOfficerName.setText(focusedOfficer.getFullName());
                txaEditOfficerDescription.setText(focusedOfficer.getDescription());
                txfEditOfficerPosition.setText(focusedOfficer.getPosition());
                txfEditOfficerTerm.setText(String.valueOf(focusedOfficer.getYear()));
                cbxEditOfficerStrand.setValue(focusedOfficer.getStrand());

                btnEditOfficerAction1.setText("Update");
                btnEditOfficerAction2.setText("Cancel");
                btnEditOfficerAction3.setText("Edit Image");

                btnEditOfficerAction1.setOnMouseClicked(event -> updateOfficer());
                btnEditOfficerAction2.setOnMouseClicked(event -> officerDialogEditor("hide"));
                btnEditOfficerAction3.setOnMouseClicked(event -> selectedImage = ProgramUtils.storeImage(ControllerUtils.getStage(btnEditOfficerAction1), true));
                selectedImage = null;
            }
            case "hide" -> {
                lblEditOfficerDialogTitle.setText("");
                btnEditOfficerAction1.setVisible(false);
                btnEditOfficerAction2.setVisible(false);
                btnEditOfficerAction3.setVisible(false);
                btnEditOfficerAction4.setVisible(false);

                txfEditOfficerName.setText("");
                txaEditOfficerDescription.setText("");
                txfEditOfficerPosition.setText("");
                txfEditOfficerTerm.setText("");
                cbxEditOfficerStrand.setValue(null);

                btnEditOfficerAction1.setText("");
                btnEditOfficerAction2.setText("");
                btnEditOfficerAction3.setText("");
                btnEditOfficerAction4.setText("");

                btnEditOfficerAction1.setOnMouseClicked(null);
                btnEditOfficerAction2.setOnMouseClicked(null);
                btnEditOfficerAction3.setOnMouseClicked(null);
                btnEditOfficerAction4.setOnMouseClicked(null);
            }
        }
    }
    // TODO Delete temp files on exit
    public Object[] getEditOfficerInput(boolean allowNull) {
        Object[] userInput = {
                txfEditOfficerName.getText(),
                txaEditOfficerDescription.getText(),
                txfEditOfficerPosition.getText(),
                ProgramUtils.parseInt(txfEditOfficerTerm.getText()),
                cbxEditOfficerStrand.getValue()
        };
        if (Arrays.stream(userInput).anyMatch(Objects::isNull) && !allowNull) {
            ControllerUtils.triggerEvent("showDialog", "message", "Invalid Input", "Invalid Input", "Back");
            return null;
        } else {
            officerDialogEditor("hide");
            return userInput;
        }
    }
    public void createOfficer() {
        try {
            Object[] userInput = getEditOfficerInput(false);
            if (userInput == null) return;
            Object[] newProject = {
                    ProgramUtils.parseName((String) userInput[0])[0],
                    ProgramUtils.parseName((String) userInput[0])[1],
                    ProgramUtils.parseName((String) userInput[0])[2],
                    userInput[1],
                    userInput[2],
                    userInput[4],
                    RuntimeData.USER.getUser_id(),
                    userInput[3],
                    selectedImage
            };
            SpendBCreate.createOfficer(newProject);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void updateOfficer() {
        try {
            Object[] userInput = getEditOfficerInput(false);
            if (userInput == null) return;
            Object[] newOfficer = {
                    ProgramUtils.parseName((String) userInput[0])[0],
                    ProgramUtils.parseName((String) userInput[0])[1],
                    ProgramUtils.parseName((String) userInput[0])[2],
                    userInput[1],
                    userInput[2],
                    userInput[4],
                    RuntimeData.USER.getUser_id(),
                    userInput[3],
                    selectedImage
            };
            SpendBUpdate.updateOfficer(newOfficer, true, "OFFICER_ID = " + focusedOfficer.getOfficer_id());
            focusedOfficer = SpendBRead.getOfficer(focusedOfficer.getOfficer_id(), officers);
            displayOfficerDetails(); // Updates the details on the view
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
