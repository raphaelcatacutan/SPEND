package com.ssg.views.templates;

import com.ssg.database.models.Project;
import com.ssg.utils.DateUtils;
import com.ssg.utils.ProgramUtils;
import io.github.palexdev.materialfx.controls.MFXProgressBar;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ProjectsItemBox {
    @FXML private Label lblProjectDate;
    @FXML private Label lblProjectDescription;
    @FXML private Label lblProjectTitle;
    @FXML private MFXProgressBar pgbProjectExpensesApprovalRate;

    public void setData(Project project, double expenseApprovalRate) {
        lblProjectDate.setText(DateUtils.formatDate("2", project.getEventdate()));
        lblProjectTitle.setText(project.getTitle());
        lblProjectDescription.setText(project.getDescription());
        pgbProjectExpensesApprovalRate.setProgress(Math.max(expenseApprovalRate, 0.05));
    }
}
