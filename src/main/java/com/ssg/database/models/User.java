package com.ssg.database.models;

import java.sql.Date;

public class User {
    private int user_id;
    private String firstName;
    private String middleInitial;
    private String lastName;
    private String username;
    private String password;
    private boolean isAdmin;
    private Date user_cd;

    public User(int user_id, String firstName, String middleInitial, String lastName, String username, String password, boolean isAdmin, Date user_cd) {
        this.user_id = user_id;
        this.firstName = firstName;
        this.middleInitial = middleInitial;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
        this.user_cd = user_cd;
    }

    public Date getUser_cd() {
        return user_cd;
    }
    public void setUser_cd(Date user_cd) {
        this.user_cd = user_cd;
    }
    public String getShortName() {
        return firstName.charAt(0) + ". " + lastName;
    }
    public String getFormattedName() {
        return lastName + ", " + firstName + " " + middleInitial + ".";
    }
    public String getFullName() {
        String mi = "";
        if (middleInitial.length() != 0) mi = middleInitial + (middleInitial.charAt(middleInitial.length() - 1) == '.' ? " " : ". ");
        return firstName + " " + mi + lastName;
    }
    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}