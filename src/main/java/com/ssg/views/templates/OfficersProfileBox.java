package com.ssg.views.templates;

import com.ssg.database.models.Officer;
import com.ssg.views.ControllerUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.sql.SQLException;

public class OfficersProfileBox {

    @FXML private ImageView officerAvatar;
    @FXML private Label officerName;
    @FXML private Label officerDescription;
    @FXML private Button officerYear;
    @FXML private Button officerStrand;

    public void setData(Officer officer, int projects) {
        // TODO Avatar
        officerName.setText(officer.getFullName());
        officerDescription.setText(officer.getPosition() + " | " + projects + " Projects");
        officerStrand.setText(officer.getStrand());
        officerYear.setText(officer.getYear() + "-" + (officer.getYear() + 1));
        officerAvatar.setImage(ControllerUtils.loadBlob(officer.getAvatar()));
    }

}
