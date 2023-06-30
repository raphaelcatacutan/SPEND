package com.ssg.database;

import com.ssg.MainActivity;
import com.ssg.database.models.ModelValues;
import com.ssg.utils.ProgramUtils;

import java.io.File;
import java.sql.SQLException;
import java.time.Year;
import java.util.Objects;


public class SpendBPrefill {
    public static final String lorem200 = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed ut orci nec quam auctor vestibulum. Duis vel massa vel sem pretium bibendum. Suspendisse potenti. Nunc euismod, quam id gravida consequat, turpis urna vestibulum lorem, ac pharetra veliteee";

    public static void generate() throws SQLException {
        Object[][] usersData = {
                {"Admin", "A", "Admin", "admin", "admin", 1}
        };
        Object[][] officerData = {
        };
        Object[][] projectsData = {
        };
        Object[][] expensesData = {
        };
        Object[][] contributorsData = {
        };
        Object[][] schoolData = {
                ModelValues.newSchoolData(
                        Year.now().getValue() - 1,
                        new File(Objects.requireNonNull(MainActivity.class.getResource("assets/icons/school-logo.png")).getFile()).getAbsolutePath().replace("%20", " "),
                        new File(Objects.requireNonNull(MainActivity.class.getResource("assets/icons/ssg-logo.png")).getFile()).getAbsolutePath().replace("%20", " "),
                        ProgramUtils.USERDOCS,
                        1,
                        0,
                        "Ms. Lily Beth Galang",
                        "Mr. Ronald R. Drio",
                        "We are writing to request your support in approving an expense " +
                                " for an SSG Project at SHS in San Nicholas III. This " +
                                "project aims to enhance our educational experience and foster innovation within our " +
                                "school community. We have attached a detailed breakdown of the expenses, and we are " +
                                "open to discussing alternative solutions or fundraising options to effectively manage " +
                                "the costs. Your approval will greatly contribute to the success of the project and our " +
                                "overall learning. Thank you for considering this proposal, and we look forward to your " +
                                "response."
                )
        };
        Object[][] fundsData = {
        };

        try {
            for (Object[] x : usersData) SpendBCreate.createUser(x, false);
            for (Object[] x : officerData) SpendBCreate.createOfficer(x, false);
            for (Object[] x : projectsData) SpendBCreate.createProject(x, false);
            for (Object[] x : expensesData) SpendBCreate.createExpenses(x, false);
            for (Object[] x : contributorsData) SpendBCreate.createContributors(x, false);
            for (Object[] x : schoolData) SpendBCreate.createSchoolData(x, false);
            for (Object[] x : fundsData) SpendBCreate.createFundsData(x, false);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}