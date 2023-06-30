package com.ssg.views;

import com.google.common.eventbus.Subscribe;
import com.ssg.database.SpendBCreate;
import com.ssg.database.SpendBRead;
import com.ssg.database.models.User;
import com.ssg.utils.RuntimeData;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.util.Objects;

public class MainLogin {
    @FXML private AnchorPane login;

    // Sign In
    @FXML private AnchorPane anpSigninForm;
    @FXML private TextField txfSigninUsername;
    @FXML private TextField txfSigninPassword;
    @FXML private Button btnSignIn;
    @FXML private Button btnToRegister;
    @FXML private Label lblSigninOutput;

    // Register
    @FXML private AnchorPane anpRegisterForm;
    @FXML private TextField txfRegisterFirstName;
    @FXML private TextField txfRegisterMiddleInitial;
    @FXML private TextField txfRegisterLastName;
    @FXML private TextField txfRegisterUsername;
    @FXML private TextField txfRegisterPassword;
    @FXML private Button btnRegister;
    @FXML private Button btnToSignin;


    public void initialize() {
        ControllerUtils.EVENTBUS.register(this);
        btnSignIn.setOnAction(event -> signIn());
        btnRegister.setOnAction(event -> register());
        btnToSignin.setOnAction(event -> toSignIn());
        btnToRegister.setOnAction(event -> toRegister());

        txfSigninPassword.setTextFormatter(ControllerUtils.textFormatter(30, false));
        txfSigninUsername.setTextFormatter(ControllerUtils.textFormatter(30, false));
        txfRegisterFirstName.setTextFormatter(ControllerUtils.textFormatter(15, false));
        txfRegisterMiddleInitial.setTextFormatter(ControllerUtils.textFormatter(5, false));
        txfRegisterLastName.setTextFormatter(ControllerUtils.textFormatter(15, false));
        txfRegisterUsername.setTextFormatter(ControllerUtils.textFormatter(30, false));
        txfRegisterPassword.setTextFormatter(ControllerUtils.textFormatter(30, false));

        txfSigninPassword.setOnContextMenuRequested(Event::consume);
        txfSigninUsername.setOnContextMenuRequested(Event::consume);
        txfRegisterFirstName.setOnContextMenuRequested(Event::consume);
        txfRegisterMiddleInitial.setOnContextMenuRequested(Event::consume);
        txfRegisterLastName.setOnContextMenuRequested(Event::consume);
        txfRegisterUsername.setOnContextMenuRequested(Event::consume);
        txfRegisterPassword.setOnContextMenuRequested(Event::consume);
    }
    private void register() {
        ObservableList<Object> users = SpendBRead.readTableData("USERS");
        String firstName = txfRegisterFirstName.getText();
        String middleInitial = txfRegisterMiddleInitial.getText();
        String lastName = txfRegisterLastName.getText();
        String username = txfRegisterUsername.getText();
        String password = txfRegisterPassword.getText();

        for (Object u: users) {
            User user = (User) u;
            if (!Objects.equals(user.getUsername(), username)) continue;
            MainEvents.showDialogMessage("Username Taken", "The username you entered is already taken. Please choose a different username to proceed.");
            return;
        }
        try {
            String[] newUser = {firstName, middleInitial, lastName, username, password};
            SpendBCreate.createUser(newUser);
            txfSigninUsername.setText(username);
            toSignIn();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void signIn() {
        ObservableList<Object> users = SpendBRead.readTableData("USERS");
        String username = txfSigninUsername.getText();
        String password = txfSigninPassword.getText();
        assert users != null;
        for (Object u : users) {
            User user = (User) u;
            if (!username.equals(user.getUsername()) || !password.equals(user.getPassword())) continue;
            RuntimeData.USER = user;
            login.setVisible(false);
            login.toBack();
            MainEvents.signIn();
            lblSigninOutput.setText(""); // Resets in case the user logs out
            return;
        }
        lblSigninOutput.setText("Invalid credentials");
    }
    private void toSignIn() {
        resetView();
        anpRegisterForm.setVisible(false);
        anpSigninForm.setVisible(true);
    }
    private void toRegister() {
        resetView();
        anpRegisterForm.setVisible(true);
        anpSigninForm.setVisible(false);
    }

    // Resetter
    private void resetView() {
        txfRegisterFirstName.setText("");
        txfRegisterMiddleInitial.setText("");
        txfRegisterLastName.setText("");
        txfRegisterUsername.setText("");
        txfRegisterPassword.setText("");
        lblSigninOutput.setText("");
        txfSigninUsername.setText("");
        txfSigninPassword.setText("");
    }

    // Public Events
    @Subscribe public void signOut(ControllerEvent event) {
        if (event.notEvent("signOut")) return;
        RuntimeData.USER = null;
        resetView();
        login.setVisible(true);
        login.toFront();

    }
}