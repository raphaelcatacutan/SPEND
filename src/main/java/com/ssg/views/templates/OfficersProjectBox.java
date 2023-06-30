package com.ssg.views.templates;

import com.ssg.database.models.Project;
import com.ssg.utils.DateUtils;
import com.ssg.utils.ProgramUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class OfficersProjectBox {
    @FXML Label projectTitle;
    @FXML Label projectDescription;
    @FXML Label projectDate;
    @FXML Label projectExpense;

    public void setData(Project project, Double expenses) {
        projectTitle.setText(project.getTitle());
        projectDescription.setText(project.getDescription());
        projectDate.setText("Started: " + DateUtils.formatDate("1", project.getEventdate()));
        projectExpense.setText("Expenses: " + ProgramUtils.formatCurrency(expenses));
    }
}
