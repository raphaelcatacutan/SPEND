package com.ssg.views.dialogs;

import com.ssg.database.models.Officer;
import com.ssg.database.models.Project;
import com.ssg.database.models.User;
import com.ssg.views.ControllerUtils;
import io.github.palexdev.materialfx.controls.MFXCheckbox;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Arrays;

public class DialogChoice {

    @FXML private AnchorPane anpDialogChoice;
    @FXML private Button btnDialogChoiceCancel;
    @FXML private Button btnDialogChoiceConfirm;
    @FXML private Label lblDialogChoiceTitle;
    @FXML private VBox vbxDialogChoiceList;

    private String clickEvent = "noClick";
    public void initialize() {
        btnDialogChoiceCancel.setOnMouseClicked(e -> ControllerUtils.triggerEvent("hideDialog"));
        btnDialogChoiceConfirm.setOnMouseClicked(this::confirmSelection);
    }

    private void confirmSelection(MouseEvent mouseEvent) {
        ObservableList<Node> choices = vbxDialogChoiceList.getChildren();
        ArrayList<Integer> selectedChoices = new ArrayList<>();
        for (Node n: choices) if (((MFXCheckbox) n).isSelected()) selectedChoices.add((int) n.getUserData());
        ControllerUtils.triggerEvent(clickEvent, selectedChoices.toArray());
        ControllerUtils.triggerEvent("hideDialog");
    }

    public void setData(int[] selectedID, String choiceType, Object[] choices) {
        vbxDialogChoiceList.getChildren().clear();
        clickEvent = choiceType;
        switch (choiceType) {
            case "officersChoices" -> {
                lblDialogChoiceTitle.setText("Officer List");
                for (Object o: choices) {
                    Officer officer = (Officer) o;
                    MFXCheckbox choice = new MFXCheckbox();
                    choice.setText(officer.getShortName());
                    boolean selected = Arrays.stream(selectedID).anyMatch(element -> element == officer.getOfficer_id());
                    choice.setSelected(selected);
                    choice.setUserData(officer.getOfficer_id());
                    choice.getStyleClass().add("whiteText");
                    choice.getStyleClass().add("fontregular");
                    if (selected) vbxDialogChoiceList.getChildren().add(0, choice);
                    else vbxDialogChoiceList.getChildren().add(choice);
                }
                StackPane.setMargin(anpDialogChoice, new Insets(0, 0, 112.0, 530.0));
            }
            case "projectsChoices" -> {
                lblDialogChoiceTitle.setText("Projects List");
                for (Object p: choices) {
                    Project project = (Project) p;
                    MFXCheckbox choice = new MFXCheckbox();
                    choice.setText(project.getTitle());
                    boolean selected = Arrays.stream(selectedID).anyMatch(element -> element == project.getProject_id());
                    choice.setSelected(selected);
                    choice.setUserData(project.getProject_id());
                    choice.getStyleClass().add("whiteText");
                    choice.getStyleClass().add("fontregular");
                    if (selected) vbxDialogChoiceList.getChildren().add(0, choice);
                    else vbxDialogChoiceList.getChildren().add(choice);
                }
                StackPane.setMargin(anpDialogChoice, new Insets(0, 0, 138.0, 535.0));
            }
            case "usersChoices" -> {
                lblDialogChoiceTitle.setText("Users List");
                for (Object u: choices) {
                    User user = (User) u;
                    MFXCheckbox choice = new MFXCheckbox();
                    choice.setText(user.getShortName());
                    choice.setSelected(user.isAdmin());
                    choice.setUserData(user.getUser_id());
                    choice.getStyleClass().add("whiteText");
                    choice.getStyleClass().add("fontregular");
                    if (user.isAdmin()) vbxDialogChoiceList.getChildren().add(0, choice);
                    else vbxDialogChoiceList.getChildren().add(choice);
                }
                StackPane.setMargin(anpDialogChoice, new Insets(0, 40, 5, 660));
            }
        }
    }

    public void clear() {
        vbxDialogChoiceList.getChildren().clear();
    }
}
