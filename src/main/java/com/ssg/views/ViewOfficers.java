package com.ssg.views;

import com.google.common.eventbus.Subscribe;
import com.ssg.database.*;
import com.ssg.database.models.*;
import com.ssg.utils.DateUtils;
import com.ssg.utils.ProgramUtils;
import com.ssg.utils.RuntimeData;
import com.ssg.views.animations.AnimationUtils;
import com.ssg.views.templates.OfficersProfileBox;
import com.ssg.views.templates.OfficersProjectBox;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.event.Event;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

public class ViewOfficers extends ViewController {
    @FXML AnchorPane anpView;

    @FXML private AnchorPane anpOfficerProfile;
    @FXML private Pane pneViewOfficerNoOfficer;
    @FXML private Pane pneViewOfficerNoProject;
    @FXML private GridPane gpnOfficersList;
    @FXML private VBox vbxProjectsList;
    @FXML private TextField txfOfficerListSearchOfficer;
    @FXML private Label txfOfficerProfileName;
    @FXML private Label txfOfficerProfilePosition;
    @FXML private Button btnOfficerListClearSearch;
    @FXML private Button btnOfficerListAddNew;
    @FXML private Button btnOfficerListGenerateReport;
    @FXML private Label txaOfficerProfileDescription;
    @FXML private ImageView imvOfficerProfileBack;
    @FXML private ImageView imvOfficerProfileAvatar;

    @FXML private Button btnOfficerDetailsEditDetails;
    @FXML private Button btnOfficerDetailsGenerateReport;
    @FXML private Button btnOfficerDetailsEditProjects;
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

    private final String[] DBNEEDED = {"OFFICERS", "CONTRIBUTORS", "EXPENSES", "PROJECTS", "SCHOOLDATA"};
    private final ArrayList<Integer> searchedOfficers = new ArrayList<>();
    private String searchOfficerPattern = "%";
    private String selectedImage = null;
    private Officer focusedOfficer = null;

    @Override
    public void initialize() {
        ControllerUtils.EVENTBUS.register(this);
        txfOfficerListSearchOfficer.setOnKeyReleased(this::searchOfficers);
        btnOfficerListGenerateReport.setOnMouseClicked(this::generateOfficersReport);
        btnOfficerListClearSearch.setOnMouseClicked(this::clearSearches);
        btnOfficerListAddNew.setOnMouseClicked(event -> officerDialogEditor("add"));

        imvOfficerProfileBack.setOnMouseClicked(event -> officerProfileBack());
        btnOfficerDetailsEditDetails.setOnMouseClicked(event -> officerDialogEditor("edit"));
        btnOfficerDetailsDeleteOfficer.setOnMouseClicked(this::deleteOfficerConfirm);
        btnOfficerDetailsEditProjects.setOnMouseClicked(this::showOfficerDialogChoices);
        btnOfficerDetailsGenerateReport.setOnMouseClicked(this::generateOfficerReport);

        txfEditOfficerName.setTextFormatter(ControllerUtils.textFormatter(30, false));
        txfEditOfficerPosition.setTextFormatter(ControllerUtils.textFormatter(30, false));
        txaEditOfficerDescription.setTextFormatter(ControllerUtils.textFormatter(400, false));
        txfEditOfficerTerm.setTextFormatter(ControllerUtils.textFormatter(4, true));

        txfOfficerListSearchOfficer.setOnContextMenuRequested(Event::consume);
        txfEditOfficerName.setOnContextMenuRequested(Event::consume);
        txfEditOfficerPosition.setOnContextMenuRequested(Event::consume);
        txaEditOfficerDescription.setOnContextMenuRequested(Event::consume);
        txfEditOfficerTerm.setOnContextMenuRequested(Event::consume);

        cbxEditOfficerStrand.getItems().addAll("GAS", "HUMSS", "ICT", "HE");
        officerDialogEditor("hide");
        refreshView(true);
    }

    @Override
    public void refreshView(boolean loadDB) {
        if (loadDB) loadDatabase();
        if (focusedOfficer != null) focusedOfficer = SpendBRead.getOfficer(focusedOfficer.getOfficer_id(), officers); // NOTE This reload the data of the focused officer
        displayOfficers();
        displayOfficerDetails();
    }

    @Override
    public void resetAll() {
        clearSearches(null);
        officerDialogEditor("hide");
        officerProfileBack();
    }

    // Officer List
    public void displayOfficers() {
        int column = 0;
        int row = 0;
        final int COLUMNLIMIT = 3;
        gpnOfficersList.getChildren().clear();
        searchedOfficers.clear();
        try {
            ArrayList<Officer> filteredOfficer = new ArrayList<>();
            ArrayList<Officer> strongFilterOfficer = new ArrayList<>();
            ArrayList<Officer> moderateFilterOfficer = new ArrayList<>();
            ArrayList<Officer> weakFilterOfficer = new ArrayList<>();

            for (Object o: officers) {
                Officer officer = (Officer) o;
                if (schoolData.isCurrentSchoolYear() && schoolData.getSchoolYear() > DateUtils.getYear(officer.getOfficer_cd())) continue;
                int matchStrength = ProgramUtils.lowestNumber(
                        ProgramUtils.stringMatch(officer.getLastName(), "%" + searchOfficerPattern + "%"), // Last Name
                        ProgramUtils.stringMatch(officer.getFirstname(), "%" + searchOfficerPattern + "%"), // First Name
                        ProgramUtils.stringMatch(officer.getPosition(), searchOfficerPattern + "%"), // Position
                        ProgramUtils.stringMatch(officer.getStrand(), searchOfficerPattern), // Strand
                        ProgramUtils.stringMatch(String.valueOf(officer.getYear()), searchOfficerPattern) // Term
                );
                switch (matchStrength) {
                    case 1 -> strongFilterOfficer.add(officer);
                    case 2 -> moderateFilterOfficer.add(officer);
                    case 3 -> weakFilterOfficer.add(officer);
                }
                if (matchStrength != 4) searchedOfficers.add(officer.getOfficer_id());
            }
            filteredOfficer.addAll(strongFilterOfficer);
            filteredOfficer.addAll(moderateFilterOfficer);
            filteredOfficer.addAll(weakFilterOfficer);

            pneViewOfficerNoOfficer.setVisible(filteredOfficer.isEmpty());
            for (Officer officer : filteredOfficer) {
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
    public void searchOfficers(KeyEvent event) {
        if (event.getCode() != KeyCode.ENTER) return;
        String searchValue = txfOfficerListSearchOfficer.getText();
        searchOfficerPattern = searchValue.isEmpty() ? "%" : searchValue;
        refreshView(false);
    }
    public void generateOfficersReport(MouseEvent mouseEvent) {
        MainEvents.startLoading();
        String filter = SpendBUtils.spendBFilterID("O.OFFICER_ID", true, searchedOfficers.stream().mapToInt(Integer::intValue).toArray());
        Map<String, String> queries = new HashMap<>();

        String query = "SELECT\n" +
                "    SD.*,\n" +
                "    O.OFFICER_ID,\n" +
                "    O.FIRSTNAME,\n" +
                "    O.MIDDLEINITIAL,\n" +
                "    O.LASTNAME,\n" +
                "    O.DESCRIPTION,\n" +
                "    O.POSITION,\n" +
                "    O.STRAND,\n" +
                "    O.USER_ID,\n" +
                "    O.AVATAR,\n" +
                "    O.OFFICER_CD,\n" +
                "    O.TERM,\n" +
                "    COUNT(C.PROJECT_ID) AS CONTRIBUTION_COUNT,\n" +
                "     ROW_NUMBER() OVER (ORDER BY O.OFFICER_ID) AS CURRENT_OFFICER_NUMBER\n" +
                "FROM\n" +
                "    SCHOOLDATA SD\n" +
                "LEFT JOIN\n" +
                "    OFFICERS O ON " + filter + " \n" +
                "LEFT JOIN\n" +
                "    CONTRIBUTORS C ON O.OFFICER_ID = C.OFFICER_ID\n" +
                "GROUP BY\n" +
                "    O.OFFICER_ID";

        queries.put("main", query);
        queries.put("Officers", query);
        SpendBUtils.generateReport(3, queries);
        MainEvents.stopLoading();
    }
    public void clearSearches(MouseEvent ignored) {
        txfOfficerListSearchOfficer.setText("");
        txfOfficerListSearchOfficer.getParent().requestFocus();
        searchOfficerPattern = "%";
        refreshView(false);
    }

    // Officer Details
    public void displayOfficerDetails() {
        if (focusedOfficer == null) {
            officerProfileBack();
            return;
        }
        anpOfficerProfile.setVisible(true);
        vbxProjectsList.getChildren().clear();
        txfOfficerProfileName.setText(focusedOfficer.getFullName());
        txfOfficerProfilePosition.setText(focusedOfficer.getPosition());
        txaOfficerProfileDescription.setText(focusedOfficer.getDescription());
        imvOfficerProfileAvatar.setImage(SpendBUtils.loadBlob(focusedOfficer.getAvatar()));

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
                officerProjectBox.setOnMouseClicked(event -> MainEvents.focusProject(project));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void officerProfileBack() {
        vbxProjectsList.getChildren().clear();
        anpOfficerProfile.setVisible(false);
        officerDialogEditor("hide");
        focusedOfficer = null;
    }
    public void generateOfficerReport(MouseEvent event) {
        MainEvents.startLoading();
        Map<String, String> queries = new HashMap<>();

        String query = "SELECT\n" +
                "  sd.DATA_ID AS SCHOOLDATA_ID,\n" +
                "  sd.UPDATETIME AS SCHOOLDATA_UPDATETIME,\n" +
                "  sd.SCHOOLYEAR,\n" +
                "  sd.SCHOOLLOGO,\n" +
                "  sd.SSGLOGO,\n" +
                "  o.OFFICER_ID,\n" +
                "  o.FIRSTNAME AS OFFICER_FIRSTNAME,\n" +
                "  o.MIDDLEINITIAL AS OFFICER_MIDDLEINITIAL,\n" +
                "  o.LASTNAME AS OFFICER_LASTNAME,\n" +
                "  o.DESCRIPTION AS OFFICER_DESCRIPTION,\n" +
                "  o.POSITION,\n" +
                "  o.STRAND,\n" +
                "  o.USER_ID,\n" +
                "  o.OFFICER_CD,\n" +
                "  o.TERM,\n" +
                "  o.UPDATETIME AS OFFICER_UPDATETIME,\n" +
                "  o.AVATAR,\n" +
                "  p.PROJECT_ID,\n" +
                "  ROW_NUMBER() OVER (ORDER BY p.PROJECT_ID) AS PROJECT_NO,\n" +
                "  p.TITLE AS PROJECT_TITLE,\n" +
                "  p.DESCRIPTION AS PROJECT_DESCRIPTION,\n" +
                "  p.USER_ID AS PROJECT_USER_ID,\n" +
                "  p.PROJECT_CD,\n" +
                "  p.EVENTDATE,\n" +
                "  p.UPDATETIME AS PROJECT_UPDATETIME,\n" +
                "  SUM(e.TOTALPRICE) AS TOTAL_EXPENSE\n" +
                "FROM\n" +
                "  schooldata sd\n" +
                "JOIN\n" +
                "  officers o ON o.OFFICER_ID = " + focusedOfficer.getOfficer_id() + " \n" +
                "JOIN\n" +
                "  contributors c ON c.OFFICER_ID = o.OFFICER_ID\n" +
                "JOIN\n" +
                "  projects p ON p.PROJECT_ID = c.PROJECT_ID\n" +
                "LEFT JOIN\n" +
                "  expenses e ON e.PROJECT_ID = p.PROJECT_ID\n" +
                "GROUP BY\n" +
                "  sd.DATA_ID,\n" +
                "  sd.UPDATETIME,\n" +
                "  sd.SCHOOLYEAR,\n" +
                "  sd.SCHOOLLOGO,\n" +
                "  sd.SSGLOGO,\n" +
                "  o.OFFICER_ID,\n" +
                "  o.FIRSTNAME,\n" +
                "  o.MIDDLEINITIAL,\n" +
                "  o.LASTNAME,\n" +
                "  o.DESCRIPTION,\n" +
                "  o.POSITION,\n" +
                "  o.STRAND,\n" +
                "  o.USER_ID,\n" +
                "  o.OFFICER_CD,\n" +
                "  o.TERM,\n" +
                "  o.UPDATETIME,\n" +
                "  o.AVATAR,\n" +
                "  p.PROJECT_ID,\n" +
                "  p.TITLE,\n" +
                "  p.DESCRIPTION,\n" +
                "  p.USER_ID,\n" +
                "  p.PROJECT_CD,\n" +
                "  p.EVENTDATE,\n" +
                "  p.UPDATETIME";

        queries.put("main", query);
        queries.put("Officers", query);
        SpendBUtils.generateReport(2, queries);
        MainEvents.stopLoading();
    }
    private void deleteOfficerConfirm(MouseEvent event) {
        if (notAdmin()) return;
        MainEvents.showDialogMessage("Deletion Confirmation", "Are you sure you want to delete the officer? This action cannot be undone and will permanently remove the officer's information from the system. Please confirm your decision.", "Delete Officer", "Cancel");
    }


    // Officer Editor
    public void officerDialogEditor(String mode) {
        if (!Objects.equals(mode, "hide") && notAdmin()) return;
        anpOfficerEditOfficer.setVisible(!Objects.equals(mode, "hide"));
        switch (mode) {
            case "add" -> {
                btnEditOfficerAction1.setOnMouseClicked(event -> createOfficer());
                btnEditOfficerAction2.setOnMouseClicked(event -> officerDialogEditor("hide"));
                btnEditOfficerAction3.setOnMouseClicked(event -> selectedImage = ProgramUtils.chooseFile(ControllerUtils.getStage(btnEditOfficerAction1), "Choose an Image", "png", "jpg", "jpeg"));
            }
            case "edit" -> {
                lblEditOfficerDialogTitle.setText("Edit Officer");

                txfEditOfficerName.setText(focusedOfficer.getFormattedName());
                txaEditOfficerDescription.setText(focusedOfficer.getDescription());
                txfEditOfficerPosition.setText(focusedOfficer.getPosition());
                txfEditOfficerTerm.setText(String.valueOf(focusedOfficer.getYear()));
                cbxEditOfficerStrand.setValue(focusedOfficer.getStrand());

                btnEditOfficerAction1.setOnMouseClicked(event -> MainEvents.showDialogMessage("Edit Officer", "Are you sure you want to edit the details of this officer", "Edit Officer", "Back"));
                btnEditOfficerAction2.setOnMouseClicked(event -> officerDialogEditor("hide"));
                btnEditOfficerAction3.setOnMouseClicked(event -> selectedImage = ProgramUtils.chooseFile(ControllerUtils.getStage(btnEditOfficerAction1), "Choose an Image", "png", "jpg", "jpeg"));
            }
            case "hide" -> {
                cbxEditOfficerStrand.setValue(null);

                btnEditOfficerAction1.setOnMouseClicked(null);
                btnEditOfficerAction2.setOnMouseClicked(null);
                btnEditOfficerAction3.setOnMouseClicked(null);

                selectedImage = null;
            }
        }
    }
    public Object[] getEditOfficerInput(boolean allowNull) {
        Object[] userInput = {
                txfEditOfficerName.getText().isEmpty() ? null : txfEditOfficerName.getText(),
                txaEditOfficerDescription.getText().isEmpty() ? null : txaEditOfficerDescription.getText(),
                txfEditOfficerPosition.getText().isEmpty() ? null : txfEditOfficerPosition.getText(),
                ProgramUtils.parseInt(txfEditOfficerTerm.getText()),
                cbxEditOfficerStrand.getValue().isEmpty() ? null : cbxEditOfficerStrand.getValue()
        };
        if (Arrays.stream(userInput).anyMatch(Objects::isNull)&& !allowNull) {
            MainEvents.showDialogMessage("Invalid User Input", "The user input for editing officer information is invalid. Please ensure that the provided data is accurate and follows the required format.", "Back");
            return null;
        } else {
            officerDialogEditor("hide");
            return userInput;
        }
    }
    public void createOfficer() {
        try {
            MainEvents.startLoading();
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
            officerDialogEditor("hide");
            SpendBCreate.createOfficer(newOfficer, true);
            MainEvents.stopLoading();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void showOfficerDialogChoices(MouseEvent mouseEvent) {
        if (notAdmin()) return;
        // Filter Contributions of this Officer
        Predicate<Object> filterContributions = (c) -> ((Contributors) c).getOfficer_id() == focusedOfficer.getOfficer_id();
        ObservableList<Object> contributorsID = contributors.filtered(filterContributions);
        // Filter the Projects with the Contributions of this Officer
        Predicate<Object> filterProject = (p) -> contributorsID.stream().anyMatch(c -> ((Contributors) c).getProject_id() == ((Project) p).getProject_id());
        ObservableList<Object> officerProjects = projects.filtered(filterProject);
        Object[] selectedIDs = officerProjects.stream().map(e -> ((Project) e).getProject_id()).toArray();
        Object[][] aArgs = {selectedIDs, projects.toArray(new Object[0])};
        Object[] sArgs = {"choice", "projectsChoices"};
        ControllerUtils.triggerEvent("showDialog", sArgs, aArgs);
    }

    // Public Events
    @Subscribe public void updateOfficer(ControllerEvent event) {
        if (!Objects.equals(event.getEventId(), "Edit Officer")) return;
        MainEvents.startLoading();
        try {
            Object[] userInput = getEditOfficerInput(true);
            if (userInput == null) {
                MainEvents.stopLoading();
                return;
            }
            boolean noName = userInput[0] == null;
            Object[] newOfficer = {
                    noName ? null : ProgramUtils.parseName((String) userInput[0])[0].strip(),
                    noName ? null : ProgramUtils.parseName((String) userInput[0])[1].strip(),
                    noName ? null : ProgramUtils.parseName((String) userInput[0])[2].strip(),
                    userInput[1],
                    userInput[2],
                    userInput[4],
                    RuntimeData.USER.getUser_id(),
                    userInput[3],
                    selectedImage
            };
            officerDialogEditor("hide");
            SpendBUpdate.updateOfficer(newOfficer, true, true, "OFFICER_ID = " + focusedOfficer.getOfficer_id());
        } catch (Exception e) {
            MainEvents.stopLoading();
            throw new RuntimeException(e);
        }
    }
    @Subscribe public void editProjects(ControllerEvent event) throws Exception {
        if (!Objects.equals(event.getEventId(), "projectsChoices")) return;
        MainEvents.startLoading();
        String officerFilter = "OFFICER_ID = " + focusedOfficer.getOfficer_id();
        int[] selectedID = Arrays.stream(event.getSimpleArgs()).mapToInt(obj -> Integer.parseInt(obj.toString())).toArray();
        String deleteIDS = SpendBUtils.spendBFilterID("PROJECT_ID", false, selectedID);
        SpendBDelete.deleteTableData("CONTRIBUTORS", true, officerFilter, deleteIDS);
        System.out.println(focusedOfficer.getOfficer_id());
        for (int x: selectedID) SpendBCreate.createContributors(new Object[]{x, focusedOfficer.getOfficer_id()}, false);
        ControllerUtils.triggerEvent("refreshViews");
    }
    @Subscribe public void deleteOfficer(ControllerEvent event) {
        if (!Objects.equals(event.getEventId(), "Delete Officer")) return;
        MainEvents.startLoading();
        SpendBDelete.deleteTableData("OFFICERS", true, "OFFICER_ID = " + focusedOfficer.getOfficer_id());
        officerProfileBack();
    }

    // Setters and Getters

    public AnchorPane getAnpOfficerProfile() {
        return anpOfficerProfile;
    }

    public void setAnpOfficerProfile(AnchorPane anpOfficerProfile) {
        this.anpOfficerProfile = anpOfficerProfile;
    }

    public Pane getPneViewOfficerNoOfficer() {
        return pneViewOfficerNoOfficer;
    }

    public void setPneViewOfficerNoOfficer(Pane pneViewOfficerNoOfficer) {
        this.pneViewOfficerNoOfficer = pneViewOfficerNoOfficer;
    }

    public Pane getPneViewOfficerNoProject() {
        return pneViewOfficerNoProject;
    }

    public void setPneViewOfficerNoProject(Pane pneViewOfficerNoProject) {
        this.pneViewOfficerNoProject = pneViewOfficerNoProject;
    }

    public GridPane getGpnOfficersList() {
        return gpnOfficersList;
    }

    public void setGpnOfficersList(GridPane gpnOfficersList) {
        this.gpnOfficersList = gpnOfficersList;
    }

    public VBox getVbxProjectsList() {
        return vbxProjectsList;
    }

    public void setVbxProjectsList(VBox vbxProjectsList) {
        this.vbxProjectsList = vbxProjectsList;
    }

    public TextField getTxfOfficerListSearchOfficer() {
        return txfOfficerListSearchOfficer;
    }

    public void setTxfOfficerListSearchOfficer(TextField txfOfficerListSearchOfficer) {
        this.txfOfficerListSearchOfficer = txfOfficerListSearchOfficer;
    }

    public Label getTxfOfficerProfileName() {
        return txfOfficerProfileName;
    }

    public void setTxfOfficerProfileName(Label txfOfficerProfileName) {
        this.txfOfficerProfileName = txfOfficerProfileName;
    }

    public Label getTxfOfficerProfilePosition() {
        return txfOfficerProfilePosition;
    }

    public void setTxfOfficerProfilePosition(Label txfOfficerProfilePosition) {
        this.txfOfficerProfilePosition = txfOfficerProfilePosition;
    }

    public Button getBtnOfficerListClearSearch() {
        return btnOfficerListClearSearch;
    }

    public void setBtnOfficerListClearSearch(Button btnOfficerListClearSearch) {
        this.btnOfficerListClearSearch = btnOfficerListClearSearch;
    }

    public Button getBtnOfficerListAddNew() {
        return btnOfficerListAddNew;
    }

    public void setBtnOfficerListAddNew(Button btnOfficerListAddNew) {
        this.btnOfficerListAddNew = btnOfficerListAddNew;
    }

    public Button getBtnOfficerListGenerateReport() {
        return btnOfficerListGenerateReport;
    }

    public void setBtnOfficerListGenerateReport(Button btnOfficerListGenerateReport) {
        this.btnOfficerListGenerateReport = btnOfficerListGenerateReport;
    }

    public Label getTxaOfficerProfileDescription() {
        return txaOfficerProfileDescription;
    }

    public void setTxaOfficerProfileDescription(Label txaOfficerProfileDescription) {
        this.txaOfficerProfileDescription = txaOfficerProfileDescription;
    }

    public ImageView getImvOfficerProfileBack() {
        return imvOfficerProfileBack;
    }

    public void setImvOfficerProfileBack(ImageView imvOfficerProfileBack) {
        this.imvOfficerProfileBack = imvOfficerProfileBack;
    }

    public ImageView getImvOfficerProfileAvatar() {
        return imvOfficerProfileAvatar;
    }

    public void setImvOfficerProfileAvatar(ImageView imvOfficerProfileAvatar) {
        this.imvOfficerProfileAvatar = imvOfficerProfileAvatar;
    }

    public Button getBtnOfficerDetailsEditDetails() {
        return btnOfficerDetailsEditDetails;
    }

    public void setBtnOfficerDetailsEditDetails(Button btnOfficerDetailsEditDetails) {
        this.btnOfficerDetailsEditDetails = btnOfficerDetailsEditDetails;
    }

    public Button getBtnOfficerDetailsGenerateReport() {
        return btnOfficerDetailsGenerateReport;
    }

    public void setBtnOfficerDetailsGenerateReport(Button btnOfficerDetailsGenerateReport) {
        this.btnOfficerDetailsGenerateReport = btnOfficerDetailsGenerateReport;
    }

    public Button getBtnOfficerDetailsEditProjects() {
        return btnOfficerDetailsEditProjects;
    }

    public void setBtnOfficerDetailsEditProjects(Button btnOfficerDetailsEditProjects) {
        this.btnOfficerDetailsEditProjects = btnOfficerDetailsEditProjects;
    }

    public Button getBtnOfficerDetailsDeleteOfficer() {
        return btnOfficerDetailsDeleteOfficer;
    }

    public void setBtnOfficerDetailsDeleteOfficer(Button btnOfficerDetailsDeleteOfficer) {
        this.btnOfficerDetailsDeleteOfficer = btnOfficerDetailsDeleteOfficer;
    }

    public AnchorPane getAnpOfficerEditOfficer() {
        return anpOfficerEditOfficer;
    }

    public void setAnpOfficerEditOfficer(AnchorPane anpOfficerEditOfficer) {
        this.anpOfficerEditOfficer = anpOfficerEditOfficer;
    }

    public Label getLblEditOfficerDialogTitle() {
        return lblEditOfficerDialogTitle;
    }

    public void setLblEditOfficerDialogTitle(Label lblEditOfficerDialogTitle) {
        this.lblEditOfficerDialogTitle = lblEditOfficerDialogTitle;
    }

    public TextField getTxfEditOfficerName() {
        return txfEditOfficerName;
    }

    public void setTxfEditOfficerName(TextField txfEditOfficerName) {
        this.txfEditOfficerName = txfEditOfficerName;
    }

    public TextArea getTxaEditOfficerDescription() {
        return txaEditOfficerDescription;
    }

    public void setTxaEditOfficerDescription(TextArea txaEditOfficerDescription) {
        this.txaEditOfficerDescription = txaEditOfficerDescription;
    }

    public TextField getTxfEditOfficerPosition() {
        return txfEditOfficerPosition;
    }

    public void setTxfEditOfficerPosition(TextField txfEditOfficerPosition) {
        this.txfEditOfficerPosition = txfEditOfficerPosition;
    }

    public TextField getTxfEditOfficerTerm() {
        return txfEditOfficerTerm;
    }

    public void setTxfEditOfficerTerm(TextField txfEditOfficerTerm) {
        this.txfEditOfficerTerm = txfEditOfficerTerm;
    }

    public ComboBox<String> getCbxEditOfficerStrand() {
        return cbxEditOfficerStrand;
    }

    public void setCbxEditOfficerStrand(ComboBox<String> cbxEditOfficerStrand) {
        this.cbxEditOfficerStrand = cbxEditOfficerStrand;
    }

    public Button getBtnEditOfficerAction1() {
        return btnEditOfficerAction1;
    }

    public void setBtnEditOfficerAction1(Button btnEditOfficerAction1) {
        this.btnEditOfficerAction1 = btnEditOfficerAction1;
    }

    public Button getBtnEditOfficerAction2() {
        return btnEditOfficerAction2;
    }

    public void setBtnEditOfficerAction2(Button btnEditOfficerAction2) {
        this.btnEditOfficerAction2 = btnEditOfficerAction2;
    }

    public Button getBtnEditOfficerAction3() {
        return btnEditOfficerAction3;
    }

    public void setBtnEditOfficerAction3(Button btnEditOfficerAction3) {
        this.btnEditOfficerAction3 = btnEditOfficerAction3;
    }

    public String[] getDBNEEDED() {
        return DBNEEDED;
    }

    public ArrayList<Integer> getSearchedOfficers() {
        return searchedOfficers;
    }

    public String getSearchOfficerPattern() {
        return searchOfficerPattern;
    }

    public void setSearchOfficerPattern(String searchOfficerPattern) {
        this.searchOfficerPattern = searchOfficerPattern;
    }

    public String getSelectedImage() {
        return selectedImage;
    }

    public void setSelectedImage(String selectedImage) {
        this.selectedImage = selectedImage;
    }

    public Officer getFocusedOfficer() {
        return focusedOfficer;
    }

    public void setFocusedOfficer(Officer focusedOfficer) {
        this.focusedOfficer = focusedOfficer;
    }
}
