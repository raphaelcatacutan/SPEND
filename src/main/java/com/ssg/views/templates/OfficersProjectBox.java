package com.ssg.views.templates;

import com.ssg.database.models.Project;
import com.ssg.utils.ProgramUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.awt.geom.Ellipse2D;

public class OfficersProjectBox {
    @FXML Label projectTitle;
    @FXML Label projectDescription;
    @FXML Label projectDate;
    @FXML Label projectStatus;
    @FXML Label projectExpense;

    public void setData(Project project, Double expenses) {
        projectTitle.setText(project.getTitle());
        projectDescription.setText(project.getDescription());
        projectDate.setText("Started: " + ProgramUtils.formatDate("1", project.getEventdate()));
        // TODO projectStatus.setText("Status: " + Project.projectStatus[project.get()]);
        projectExpense.setText("Expenses: " + ProgramUtils.formatCurrency(expenses));
    }
}
