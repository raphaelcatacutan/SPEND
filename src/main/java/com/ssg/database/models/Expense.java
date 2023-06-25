package com.ssg.database.models;

import java.sql.Date;
import java.sql.Timestamp;

public class Expense {
    private int expense_id;
    private int project_id;
    private String title;
    private double totalPrice;
    private Date expenseDate_cd;
    private double quantity;
    private double unitPrice;
    private int status;
    private Timestamp updateTime;

    public Expense(int expense_id, int project_id, String title, double totalPrice, Date expenseDate_cd, double quantity, double unitPrice, int status, Timestamp updateTime) {
        this.expense_id = expense_id;
        this.project_id = project_id;
        this.title = title;
        this.totalPrice = totalPrice;
        this.expenseDate_cd = expenseDate_cd;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.status = status;
        this.updateTime = updateTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
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

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Date getExpenseDate_cd() {
        return expenseDate_cd;
    }

    public void setExpenseDate_cd(Date expenseDate_cd) {
        this.expenseDate_cd = expenseDate_cd;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getExpense_id() {
        return expense_id;
    }

    public void setExpense_id(int expense_id) {
        this.expense_id = expense_id;
    }
}
