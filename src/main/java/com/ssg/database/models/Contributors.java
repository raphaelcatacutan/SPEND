package com.ssg.database.models;

public class Contributors {
    private int project_id;
    private int officer_id;

    public Contributors(int project_id, int officer_id) {
        this.project_id = project_id;
        this.officer_id = officer_id;
    }

    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }

    public int getOfficer_id() {
        return officer_id;
    }

    public void setOfficer_id(int officer_id) {
        this.officer_id = officer_id;
    }
}
