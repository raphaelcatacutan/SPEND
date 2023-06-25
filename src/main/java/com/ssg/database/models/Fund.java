package com.ssg.database.models;

import java.sql.Date;
import java.sql.Timestamp;

public class Fund {
    private int fund_id;
    private double amount;
    private Timestamp updateTime;
    private Date fund_cd;

    public Fund(int fund_id, double amount, Timestamp updateTime, Date fund_cd, String description) {
        this.fund_id = fund_id;
        this.amount = amount;
        this.updateTime = updateTime;
        this.fund_cd = fund_cd;
        this.description = description;
    }

    public int getFund_id() {
        return fund_id;
    }

    public void setFund_id(int fund_id) {
        this.fund_id = fund_id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public Date getFund_cd() {
        return fund_cd;
    }

    public void setFund_cd(Date fund_cd) {
        this.fund_cd = fund_cd;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String description;
}
