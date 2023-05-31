package com.ssg.views.templates;

import com.ssg.database.models.Project;
import com.ssg.utils.ProgramUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ProjectsItemBox {
    @FXML private Label lblProjectDate;
    @FXML private Label lblProjectDescription;
    @FXML private Label lblProjectTitle;

    public void setData(Project project) {
        lblProjectDate.setText(ProgramUtils.formatDate("2", project.getEventdate()));
        lblProjectTitle.setText(project.getTitle());
        lblProjectDescription.setText(project.getDescription());
    }
}
