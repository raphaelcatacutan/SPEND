package com.ssg.views;


import com.google.common.eventbus.Subscribe;
import com.ssg.database.*;
import com.ssg.database.models.*;
import com.ssg.utils.ProgramUtils;
import com.ssg.utils.RuntimeData;
import io.github.palexdev.materialfx.controls.MFXToggleButton;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.Predicate;

public class ViewSettings extends ViewController {
    @FXML private AnchorPane anpView;

    @FXML private Label lblSettingsTitle;

    @FXML private AnchorPane anpSettingsAbout;
    @FXML private AnchorPane anpSettingsAdministrator;
    @FXML private AnchorPane anpSettingsReportTemplates;
    @FXML private AnchorPane anpSettingsPreference;
    @FXML private AnchorPane anpSettingsYourAccount;

    @FXML private Label navSettingsAbout;
    @FXML private Label navSettingsAdministrator;
    @FXML private Label navSettingsReportTemplates;
    @FXML private Label navSettingsPreference;
    @FXML private Label navSettingsYourAccount;

    // Preference
    @FXML private Button btnSettingsPreferenceReportExportLocation;
    @FXML private Button btnSettingsPreferenceXAMPPLocation;
    @FXML private Label lblSettingsPreferenceReportExportLocation;
    @FXML private Label lblSettingsPreferenceXAMPPLocation;
    @FXML private MFXToggleButton tgbSettingsPreferenceXAMPPManager;
    @FXML private MFXToggleButton tgbSettingsPreferenceViewPDF;
    @FXML private MFXToggleButton tgbSettingsPreferenceCurrentSchoolYear;

    // Report Templates
    @FXML private TextField txfSettingsReportTemplatesPrincipal;
    @FXML private TextField txfSettingsReportTemplatesAdviser;
    @FXML private TextArea txfSettingsReportTemplatesProposal;

    // Your Account
    @FXML private TextField txfSettingsYourAccountName;
    @FXML private TextField txfSettingsYourAccountUserName;
    @FXML private TextField txfSettingsYourAccountPassword;

    // Administrator
    @FXML private Button btnSettingsAdministratorNewSY;
    @FXML private Button btnSettingsAdministratorSchoolLogo;
    @FXML private Button btnSettingsAdministratorSSGLogo;
    @FXML private Button btnSettingsAdministratorAccountAdmin;

    // About

    private final HashMap<Label, AnchorPane> settingsView = new HashMap<>();

    @Override
    public void initialize() {
        ControllerUtils.EVENTBUS.register(this);

        settingsView.put(navSettingsYourAccount, anpSettingsYourAccount);
        settingsView.put(navSettingsAbout, anpSettingsAbout);
        settingsView.put(navSettingsAdministrator, anpSettingsAdministrator);
        settingsView.put(navSettingsReportTemplates, anpSettingsReportTemplates);
        settingsView.put(navSettingsPreference, anpSettingsPreference);

        for (Label navigator: settingsView.keySet()) navigator.setOnMouseClicked(event -> navigate(navigator));

        // Preference
        btnSettingsPreferenceReportExportLocation.setOnMouseClicked(this::reportExportLocation);
        btnSettingsPreferenceXAMPPLocation.setOnMouseClicked(this::xamppLocation);
        tgbSettingsPreferenceXAMPPManager.setOnMouseClicked(this::manageXAMPP);
        tgbSettingsPreferenceViewPDF.setOnMouseClicked(this::viewPDF);
        tgbSettingsPreferenceCurrentSchoolYear.setOnMouseClicked(this::currentSY);
        // Report Templates
        txfSettingsReportTemplatesPrincipal.setOnKeyReleased(this::changePrincipal);
        txfSettingsReportTemplatesAdviser.setOnKeyReleased(this::changeAdviser);
        txfSettingsReportTemplatesProposal.setOnKeyReleased(this::changeProposal);
        // Your Account
        txfSettingsYourAccountName.setOnKeyReleased(this::changeName);
        txfSettingsYourAccountUserName.setOnKeyReleased(this::changeUserName);
        txfSettingsYourAccountPassword.setOnKeyReleased(this::changePassword);
        // Administrator
        btnSettingsAdministratorNewSY.setOnMouseClicked(e -> ControllerUtils.triggerEvent("newSY"));
        btnSettingsAdministratorSchoolLogo.setOnMouseClicked(this::schoolLogo);
        btnSettingsAdministratorSSGLogo.setOnMouseClicked(this::ssgLogo);
        btnSettingsAdministratorAccountAdmin.setOnMouseClicked(this::accountAdmin);

        txfSettingsYourAccountPassword.setTextFormatter(ControllerUtils.textFormatter(30, false));
        txfSettingsYourAccountUserName.setTextFormatter(ControllerUtils.textFormatter(30, false));
        txfSettingsYourAccountName.setTextFormatter(ControllerUtils.textFormatter(30, false));
        txfSettingsReportTemplatesProposal.setTextFormatter(ControllerUtils.textFormatter(500, false));
        txfSettingsReportTemplatesAdviser.setTextFormatter(ControllerUtils.textFormatter(30, false));
        txfSettingsReportTemplatesPrincipal.setTextFormatter(ControllerUtils.textFormatter(30, false));

        txfSettingsYourAccountPassword.setOnContextMenuRequested(Event::consume);
        txfSettingsYourAccountUserName.setOnContextMenuRequested(Event::consume);
        txfSettingsYourAccountName.setOnContextMenuRequested(Event::consume);
        txfSettingsReportTemplatesProposal.setOnContextMenuRequested(Event::consume);
        txfSettingsReportTemplatesAdviser.setOnContextMenuRequested(Event::consume);
        txfSettingsReportTemplatesPrincipal.setOnContextMenuRequested(Event::consume);


        navigate(navSettingsPreference);
    }

    public void refreshView(boolean loadDB) {
        if (loadDB) loadDatabase();
        SchoolData data = (SchoolData) schoolData.get(0);
        // Preference
        lblSettingsPreferenceReportExportLocation.setText(data.getReportExportLocation());
        // Report Templates
        txfSettingsReportTemplatesAdviser.setPromptText(data.getSsgAdviser());
        txfSettingsReportTemplatesPrincipal.setPromptText(data.getPrincipal());
        txfSettingsReportTemplatesProposal.setPromptText(data.getProposalParagraph());
        // Your Account
        txfSettingsYourAccountName.setPromptText(RuntimeData.USER.getFormattedName());
        txfSettingsYourAccountUserName.setPromptText(RuntimeData.USER.getUsername());
    }
    public void navigate(Label navigator) {
        AnchorPane viewer = settingsView.get(navigator);
        for (Label nav: settingsView.keySet()) {
            if (navigator == nav) continue;
            settingsView.get(nav).setVisible(false);
            nav.setTextFill(Color.web("#4f513a"));
        }
        navigator.setTextFill(Color.web("#eeff00"));
        viewer.setVisible(true);
    }
    public void resetAll() {
        txfSettingsReportTemplatesPrincipal.setText("");
        txfSettingsReportTemplatesAdviser.setText("");
        txfSettingsReportTemplatesProposal.setText("");
        txfSettingsYourAccountName.setText("");
        txfSettingsYourAccountUserName.setText("");
        txfSettingsYourAccountPassword.setText("");
        lblSettingsTitle.requestFocus();
    }

    // Preference
    private void reportExportLocation(MouseEvent mouseEvent) {
        try {
            String directory = ProgramUtils.chooseDirectory(ControllerUtils.getStage(lblSettingsTitle), "Choose a new directory");
            if (directory == null) return;
            Object[] newSettings = ModelValues.newSchoolData(
                    null,
                    null,
                    null,
                    directory,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );
            SpendBUpdate.updateSchoolData(newSettings, true, false);
            lblSettingsPreferenceReportExportLocation.setText(directory);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void xamppLocation(MouseEvent mouseEvent) {
        String directory = ProgramUtils.chooseDirectory(ControllerUtils.getStage(lblSettingsTitle), "Choose a new directory");
        if (directory == null) return;
        ProgramUtils.updateConfig("xamppLocation", directory);
        lblSettingsPreferenceXAMPPLocation.setText(directory);
    }
    private void manageXAMPP(MouseEvent mouseEvent) {
        try {
            ProgramUtils.updateConfig("startXAMPP", tgbSettingsPreferenceXAMPPManager.isSelected());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void currentSY(MouseEvent mouseEvent) {
        try {
            Object[] newSettings = ModelValues.newSchoolData(
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    tgbSettingsPreferenceCurrentSchoolYear.isSelected() ? 1 : 0,
                    null,
                    null,
                    null
            );
            SpendBUpdate.updateSchoolData(newSettings, true, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void viewPDF(MouseEvent mouseEvent) {
        try {
            Object[] newSettings = ModelValues.newSchoolData(
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    tgbSettingsPreferenceViewPDF.isSelected() ? 1 : 0,
                    null,
                    null,
                    null,
                    null
            );
            SpendBUpdate.updateSchoolData(newSettings, true, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Report Templates
    private void changeProposal(KeyEvent keyEvent) {
        if (keyEvent.getCode() != KeyCode.ENTER) return;
        String newValue = txfSettingsReportTemplatesProposal.getText();
        txfSettingsReportTemplatesProposal.setText("");
        lblSettingsTitle.requestFocus();
        boolean empty = newValue.isEmpty();
        if (empty) {
            MainEvents.showDialogMessage("Empty Input", "The proposal body field cannot be left empty. Please enter a valid proposal body to proceed.");
            return;
        }
        txfSettingsReportTemplatesProposal.setPromptText(newValue);
        try {
            Object[] newSettings = ModelValues.newSchoolData(
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    newValue
            );
            SpendBUpdate.updateSchoolData(newSettings, true, false);
            MainEvents.showDialogMessage("Update Successful", "The parameter \"Proposal Body\" has been successfully updated for generating reports. The new value will be reflected in the reports generated from now on.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void changeAdviser(KeyEvent keyEvent) {
        if (keyEvent.getCode() != KeyCode.ENTER) return;
        String newValue = txfSettingsReportTemplatesAdviser.getText();
        txfSettingsReportTemplatesAdviser.setText("");
        lblSettingsTitle.requestFocus();
        boolean empty = newValue.isEmpty();
        if (empty) {
            MainEvents.showDialogMessage("Empty Input", "The adviser name field cannot be left empty. Please enter a valid input to proceed.");
            return;
        }
        txfSettingsReportTemplatesAdviser.setPromptText(newValue);
        try {
            Object[] newSettings = ModelValues.newSchoolData(
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    newValue,
                    null,
                    null
            );
            SpendBUpdate.updateSchoolData(newSettings, true, false);
            MainEvents.showDialogMessage("Update Successful", "The parameter \"Adviser Name\" has been successfully updated for generating reports. The new value will be reflected in the reports generated from now on.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void changePrincipal(KeyEvent keyEvent) {
        if (keyEvent.getCode() != KeyCode.ENTER) return;
        String newValue = txfSettingsReportTemplatesPrincipal.getText();
        txfSettingsReportTemplatesPrincipal.setText("");
        lblSettingsTitle.requestFocus();
        boolean empty = newValue.isEmpty();
        if (empty) {
            MainEvents.showDialogMessage("Empty Input", "The principal name field cannot be left empty. Please enter a valid input to proceed.");
            return;
        }
        txfSettingsReportTemplatesPrincipal.setPromptText(newValue);
        try {
            Object[] newSettings = ModelValues.newSchoolData(
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    newValue,
                    null
            );
            SpendBUpdate.updateSchoolData(newSettings, true, false);
            MainEvents.showDialogMessage("Update Successful", "The parameter \"Principal Name\" has been successfully updated for generating reports. The new value will be reflected in the reports generated from now on.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Your Account
    private void changeName(KeyEvent keyEvent) {
        if (keyEvent.getCode() != KeyCode.ENTER) return;
        String newValue = txfSettingsYourAccountName.getText();
        txfSettingsYourAccountName.setText("");
        lblSettingsTitle.requestFocus();
        boolean empty = newValue.isEmpty();
        if (empty) {
            MainEvents.showDialogMessage("Empty Input", "The name field cannot be left empty. Please enter a valid input to proceed.");
            return;
        }
        txfSettingsYourAccountName.setPromptText(newValue);
        String[] parsedName = ProgramUtils.parseName(newValue);
        try {
            Object[] newAccount = ModelValues.newUser(
                    parsedName[0],
                    parsedName[1],
                    parsedName[2],
                    null,
                    null,
                    null
            );
            SpendBUpdate.updateUser(newAccount, true, false, "USER_ID = " + RuntimeData.USER.getUser_id());
            RuntimeData.USER = SpendBRead.getUser(RuntimeData.USER.getUser_id());
            refreshView(false);
            MainEvents.showDialogMessage("Update Successful", "Your account name has been successfully updated. The new value will be reflected in the reports and other places from now on.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void changeUserName(KeyEvent keyEvent) {
        if (keyEvent.getCode() != KeyCode.ENTER) return;
        try {
            String newValue = txfSettingsYourAccountUserName.getText();
            txfSettingsYourAccountUserName.setText("");
            lblSettingsTitle.requestFocus();
            boolean empty = newValue.isEmpty();
            if (empty) {
                MainEvents.showDialogMessage("Empty Input", "The username field cannot be left empty. Please enter a valid input to proceed.");
                return;
            }
            for (Object u: users) {
                User user = (User) u;
                if (!Objects.equals(user.getUsername(), newValue)) continue;
                MainEvents.showDialogMessage("Username Taken", "The username you entered is already taken. Please choose a different username to proceed.");
                return;
            }
            txfSettingsYourAccountUserName.setPromptText(newValue);
            Object[] newAccount = ModelValues.newUser(
                    null,
                    null,
                    null,
                    newValue,
                    null,
                    null
            );
            SpendBUpdate.updateUser(newAccount, true, false, "USER_ID = " + RuntimeData.USER.getUser_id());
            MainEvents.showDialogMessage("Update Successful", "Your account username has been successfully updated. Use this for signing in the next time");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void changePassword(KeyEvent keyEvent) {
        if (keyEvent.getCode() != KeyCode.ENTER) return;
        try {
            String newValue = txfSettingsYourAccountPassword.getText();
            txfSettingsYourAccountPassword.setText("");
            lblSettingsTitle.requestFocus();
            System.out.println();
            boolean empty = newValue.isEmpty();
            if (empty) {
                MainEvents.showDialogMessage("Empty Input", "The password field cannot be left empty. Please enter a valid input to proceed.");
                return;
            }
            Object[] newAccount = ModelValues.newUser(
                    null,
                    null,
                    null,
                    null,
                    newValue,
                    null
            );
            SpendBUpdate.updateUser(newAccount, true, false, "USER_ID = " + RuntimeData.USER.getUser_id());
            MainEvents.showDialogMessage("Update Successful", "Your account password has been successfully updated. Use this for signing in the next time");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Administrator
    private void accountAdmin(MouseEvent mouseEvent) {
        // Checks who are the admins
        // FIXME Can't fetch newly registered accounts
        if (notAdmin()) return;
        // Filters users that is the signed account
        Predicate<Object> filterUsers = (u) -> ((User) u).getUser_id() != RuntimeData.USER.getUser_id() && !Objects.equals(((User) u).getUsername(), "admin");
        ObservableList<Object> filteredUsers = users.filtered(filterUsers);
        Object[][] aArgs = {new Object[0], filteredUsers.toArray(new Object[0])};
        Object[] sArgs = {"choice", "usersChoices"};
        ControllerUtils.triggerEvent("showDialog", sArgs, aArgs);
    }
    private void ssgLogo(MouseEvent mouseEvent) {
        String selectedImage = ProgramUtils.chooseFile(ControllerUtils.getStage(lblSettingsTitle), "Choose an Image", "png", "jpg", "jpeg");
        if (notAdmin()) return;
        try {
            Object[] newSettings = ModelValues.newSchoolData(
                    null,
                    null,
                    selectedImage,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );
            SpendBUpdate.updateSchoolData(newSettings, true, false);
            MainEvents.showDialogMessage("Update Successful", "A new SSG Logo has been successfully updated. This will appear in reports or other places");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void schoolLogo(MouseEvent mouseEvent) {
        String selectedImage = ProgramUtils.chooseFile(ControllerUtils.getStage(lblSettingsTitle), "Choose an Image", "png", "jpg", "jpeg");
        if (notAdmin()) return;
        try {
            Object[] newSettings = ModelValues.newSchoolData(
                    null,
                    selectedImage,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );
            SpendBUpdate.updateSchoolData(newSettings, true, false);
            MainEvents.showDialogMessage("Update Successful", "A new School Logo has been successfully updated. This will appear in reports or other places");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // About
    public void aboutAppVersion() {
        ProgramUtils.browseURL("https://github.com/el-brauq/SPEND/releases");
    }
    public void aboutAppGithub() {
        ProgramUtils.browseURL("https://github.com/el-brauq/SPEND");
    }

    // Public Events
    @Subscribe public void newSY(ControllerEvent event) {
        if (event.notEvent("newSY")) return;
        if (notAdmin()) return;
        MainEvents.showDialogMessage("School Year", "Are you sure you want to start a new school year? This will add 1 year in your current school year", "New School Year", "Cancel");
    }
    @Subscribe public void newSchoolYear(ControllerEvent event) {
        if (event.notEvent("New School Year")) return;
        try {
            Object[] newSchoolData = ModelValues.newSchoolData(
                    ((SchoolData) schoolData.get(0)).getSchoolYear() + 1,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );
            SpendBUpdate.updateSchoolData(newSchoolData, true, true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Subscribe public void userChoices(ControllerEvent event) throws Exception {
        if (!Objects.equals(event.getEventId(), "usersChoices")) return;
        MainEvents.startLoading();

        // Filters users that is the signed account
        Predicate<Object> filterUsers = (u) -> ((User) u).getUser_id() != RuntimeData.USER.getUser_id() && !Objects.equals(((User) u).getUsername(), "admin");
        ObservableList<Object> filteredUsers = users.filtered(filterUsers);
        int[] selectedID = Arrays.stream(event.getSimpleArgs()).mapToInt(obj -> Integer.parseInt(obj.toString())).toArray();
        for (Object u: filteredUsers) {
            User user = (User) u;
            Object[] newUser = ModelValues.newUser(
                    null,
                    null,
                    null,
                    null,
                    null,
                    Arrays.stream(selectedID).anyMatch(value -> value == user.getUser_id()) ? 1 : 0
            );
            SpendBUpdate.updateUser(newUser, true, false, "USER_ID = " + user.getUser_id());
        }
        MainEvents.showDialogMessage("Update Successful", "Selected users now have admin privilege and can modify, add, or delete data in the database");
        ControllerUtils.triggerEvent("refreshViews");
    }

}