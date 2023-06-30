package com.ssg.database.models;

import java.sql.Date;
import java.sql.Timestamp;

public class Project {
    private int project_id;
    private String title;
    private String description;
    private int user_id;
    private Date eventdate;
    private Timestamp updatetime;
    private Date project_cd;

    public Project(int project_id, String title, String description, int user_id, Date project_cd, Date eventdate, Timestamp updatetime) {
        this.project_id = project_id;
        this.title = title;
        this.description = description;
        this.user_id = user_id;
        this.eventdate = eventdate;
        this.project_cd = project_cd;
        this.updatetime = updatetime;
    }

    // Getters and Setters

    public Date getProject_cd() {
        return project_cd;
    }

    public void setProject_cd(Date project_cd) {
        this.project_cd = project_cd;
    }

    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public Date getEventdate() {
        return eventdate;
    }

    public void setEventdate(Date eventdate) {
        this.eventdate = eventdate;
    }

    public Timestamp getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Timestamp updatetime) {
        this.updatetime = updatetime;
    }
}
