package com.ssg.database.models;

import java.util.Objects;

import static com.ssg.database.SpendBPrefill.lorem200;

public class ModelValues {
    public static Object[] newUser(
            String firstName,
            String middleInitial,
            String lastName,
            String userName,
            String password,
            Integer isAdmin
    ) {
        return new Object[] {
                firstName,
                middleInitial,
                lastName,
                // middleInitial, I will not remove this because this bug took me three days to find.
                userName,
                password,
                isAdmin
        };
    }
    public static Object[] newOfficer(
            String firstName,
            String middleInitial,
            String lastName,
            String description,
            String position,
            String strand,
            String userId,
            Integer term,
            String avatarPath
    ) {
        return new Object[] {
                firstName,
                middleInitial,
                lastName,
                Objects.equals(description, "$lorem") ? lorem200: description,
                position,
                strand,
                userId,
                term,
                avatarPath
        };
    }
    public static Object[] newProject(
            String projectName,
            String description,
            Integer userId,
            String eventDate
    ) {
        return new Object[] {
                projectName,
                Objects.equals(description, "$lorem") ? lorem200: description,
                userId,
                eventDate
        };
    }
    public static Object[] newExpense(
            Integer projectId,
            String itemName,
            double totalPrice,
            double quantity,
            double unitPrice,
            Integer status
    ) {
        return new Object[] {
                projectId,
                itemName,
                totalPrice,
                quantity,
                unitPrice,
                status
        };
    }
    public static Object[] newContributor(
            Integer projectId,
            Integer officerId
    ) {
        return new Object[] {
                projectId,
                officerId
        };
    }
    public static Object[] newSchoolData(
            Integer schoolYear,
            String schoolLogo,
            String ssgLogo,
            String reportExportLocation,
            Integer viewPDF,
            Integer currentSchoolYear,
            String ssgAdviser,
            String principal,
            String proposalParagraph
    ) {
        return new Object[] {
                schoolYear,
                schoolLogo,
                ssgLogo,
                reportExportLocation,
                viewPDF,
                currentSchoolYear,
                ssgAdviser,
                principal,
                proposalParagraph
        };
    }
    public static Object[] newFundData (
            double fundData,
            String description
    ){
        return new Object[] {
                fundData,
                description
        };
    }
}
