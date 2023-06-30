package com.ssg.database.models;

import java.sql.Blob;
import java.sql.Timestamp;

public class SchoolData {
    private int data_id;
    private Timestamp updateTime;
    private int schoolYear;
    private Blob schoolLogo;
    private Blob ssgLogo;
    private String reportExportLocation;
    private boolean viewPDF;
    private boolean currentSchoolYear;
    private String ssgAdviser;
    private String principal;
    private String proposalParagraph;

    public SchoolData(int data_id, Timestamp updateTime, int schoolYear, Blob schoolLogo, Blob ssgLogo, String reportExportLocation, boolean viewPDF, boolean currentSchoolYear, String ssgAdviser, String principal, String proposalParagraph) {
        this.data_id = data_id;
        this.updateTime = updateTime;
        this.schoolYear = schoolYear;
        this.schoolLogo = schoolLogo;
        this.ssgLogo = ssgLogo;
        this.reportExportLocation = reportExportLocation;
        this.viewPDF = viewPDF;
        this.currentSchoolYear = currentSchoolYear;
        this.ssgAdviser = ssgAdviser;
        this.principal = principal;
        this.proposalParagraph = proposalParagraph;
    }

    public Blob getSchoolLogo() {
        return schoolLogo;
    }

    public Blob getSsgLogo() {
        return ssgLogo;
    }

    public String getReportExportLocation() {
        return reportExportLocation;
    }

    public void setReportExportLocation(String reportExportLocation) {
        this.reportExportLocation = reportExportLocation;
    }

    public boolean isViewPDF() {
        return viewPDF;
    }

    public void setViewPDF(boolean viewPDF) {
        this.viewPDF = viewPDF;
    }

    public boolean isCurrentSchoolYear() {
        return currentSchoolYear;
    }

    public void setCurrentSchoolYear(boolean currentSchoolYear) {
        this.currentSchoolYear = currentSchoolYear;
    }

    public String getSsgAdviser() {
        return ssgAdviser;
    }

    public void setSsgAdviser(String ssgAdviser) {
        this.ssgAdviser = ssgAdviser;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getProposalParagraph() {
        return proposalParagraph;
    }

    public void setProposalParagraph(String proposalParagraph) {
        this.proposalParagraph = proposalParagraph;
    }

    public void setSchoolLogo(Blob schoolLogo) {
        this.schoolLogo = schoolLogo;
    }

    public void setSsgLogo(Blob ssgLogo) {
        this.ssgLogo = ssgLogo;
    }

    public int getData_id() {
        return data_id;
    }

    public void setData_id(int data_id) {
        this.data_id = data_id;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public int getSchoolYear() {
        return schoolYear;
    }

    public void setSchoolYear(int schoolYear) {
        this.schoolYear = schoolYear;
    }
}
