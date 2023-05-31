package com.ssg.views;

import com.ssg.database.SpendBCreate;
import com.ssg.database.SpendBRead;
import com.ssg.database.models.User;
import com.ssg.utils.RuntimeData;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

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
        btnSignIn.setOnAction(event -> signIn());
        btnRegister.setOnAction(event -> register());
        btnToSignin.setOnAction(event -> toSignIn());
        btnToRegister.setOnAction(event -> toRegister());
    }
    private void register() {
        String firstName = txfRegisterFirstName.getText();
        String middleInitial = txfRegisterMiddleInitial.getText();
        String lastName = txfRegisterLastName.getText();
        String username = txfRegisterUsername.getText();
        String password = txfRegisterPassword.getText();

        try {
            String[] newUser = {firstName, middleInitial, lastName, username, password};
            SpendBCreate.createUser(newUser);
            txfSigninUsername.setText(username);
            toSignIn();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
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
            byPassLogin(user);
            lblSigninOutput.setText(""); // Resets in case the user logs out
            return;
        }
        lblSigninOutput.setText("Invalid username or password");
    }
    private void toSignIn() {
        txfRegisterFirstName.setText("");
        txfRegisterMiddleInitial.setText("");
        txfRegisterLastName.setText("");
        txfRegisterUsername.setText("");
        txfRegisterPassword.setText("");
        lblSigninOutput.setText("");
        anpRegisterForm.setVisible(false);
        anpSigninForm.setVisible(true);
    }
    private void toRegister() {
        anpRegisterForm.setVisible(true);
        anpSigninForm.setVisible(false);
    }
    private void byPassLogin(User user) {
        RuntimeData.USER = user;
        login.setVisible(false);
        login.toBack();

        // Set the username
        ControllerUtils.triggerEvent("byPassLogin");
    }
}