package com.ssg.database.models;

import java.sql.Blob;
import java.sql.Timestamp;

public class Officer {
    int officer_id;
    String firstname;
    String middleInitial;
    String lastName;
    String description;
    String position;
    String strand;
    int user_id;
    int year;
    Timestamp updatetime;
    Blob avatar;

    public String getFullName() {
        return firstname + " " + middleInitial + ". " + lastName;
    }
    public String getShortName() {
        return firstname.charAt(0) + ". " + lastName;
    }

    public Officer(int officer_id, String firstname, String middleInitial, String lastName, String description, String position, String strand, int user_id, int year, Timestamp updatetime, Blob avatar) {
        this.officer_id = officer_id;
        this.firstname = firstname;
        this.middleInitial = middleInitial;
        this.lastName = lastName;
        this.description = description;
        this.position = position;
        this.strand = strand;
        this.user_id = user_id;
        this.year = year;
        this.updatetime = updatetime;
        this.avatar = avatar;
    }

    public int getOfficer_id() {
        return officer_id;
    }

    public void setOfficer_id(int officer_id) {
        this.officer_id = officer_id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getMiddleInitial() {
        return middleInitial;
    }

    public void setMiddleInitial(String middleInitial) {
        this.middleInitial = middleInitial;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getStrand() {
        return strand;
    }

    public void setStrand(String strand) {
        this.strand = strand;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Timestamp getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Timestamp updatetime) {
        this.updatetime = updatetime;
    }

    public Blob getAvatar() {
        return avatar;
    }

    public void setAvatar(Blob avatar) {
        this.avatar = avatar;
    }
}
