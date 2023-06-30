package com.ssg.views.templates;

import com.ssg.database.SpendBUtils;
import com.ssg.database.models.Officer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class OfficersProfileBox {

    @FXML private ImageView officerAvatar;
    @FXML private Label officerName;
    @FXML private Label officerDescription;
    @FXML private Button officerYear;
    @FXML private Button officerStrand;

    public void setData(Officer officer, int projects) {
        officerName.setText(officer.getFullName());
        officerDescription.setText(officer.getPosition() + " | " + projects + " Projects");
        officerStrand.setText(officer.getStrand());
        officerYear.setText(officer.getYear() + "-" + (officer.getYear() + 1));
        officerAvatar.setImage(SpendBUtils.loadBlob(officer.getAvatar()));
    }

}
