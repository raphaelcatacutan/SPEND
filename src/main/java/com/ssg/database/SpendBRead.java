package com.ssg.database;

import com.ssg.database.models.*;
import com.ssg.utils.ProgramUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class SpendBRead {

    /**
     * Executes a table query to retrieve all rows from the specified table.
     *
     * @param table  the name of the table from which to retrieve data
     * @return a ResultSet object containing the result of the query, or null if an error occurs
     */
    private static ResultSet tableQuery(String table) {
        try {
            String query = "SELECT * FROM " + table + " ORDER BY UPDATETIME DESC";
            Statement stmt = SpendBConnection.getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            return stmt.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Reads and retrieves data from the specified table.
     *
     * @param table  the name of the table from which to read data
     * @return an ObservableList containing the retrieved data as objects, or an empty list if an error occurs
     */

    public static ObservableList<Object> readTableData(String table) {
        ResultSet rs = tableQuery(table);
        return getTableData(rs, table);
    }

    /**
     * Reads and retrieves data from all SpendB tables.
     *
     * @return a Map containing table names as keys and corresponding ObservableList objects with table data as values
     */
    public static Map<String, ObservableList<Object>> readTablesData() {
        Map<String, ObservableList<Object>> tableDataMap = new HashMap<>();
        for (String x : SpendBUtils.SPENDBTABLES) {
            // if (!SpendBUtils.spendBHasChanges(x)) continue;
            /**
             * FIXME
             * Commenting this will not result to any change however it will load all database table, even the ones
             * that doesn't have changes. The problem is, when deleting a project, project table will be registered
             * as having change but not the expense table.
             */
            tableDataMap.put(x, readTableData(x));
        }
        return tableDataMap;
    }

    /**
     * Checks if a specific ID exists in the specified table and column.
     *
     * @param tableName    the name of the table to check for ID existence
     * @param columnName   the name of the column in which to search for the ID
     * @param id           the ID value to check for existence
     * @return true if the ID exists in the specified table and column, false otherwise
     * @throws SQLException if an error occurs while accessing the database
     */
    public static boolean checkIfIdExists(String tableName, String columnName, int id) throws SQLException {
        String query = "SELECT COUNT(*) FROM " + tableName + " WHERE " + columnName + " = " + id;
        try (PreparedStatement stmt = SpendBConnection.getConnection().prepareStatement(query); ResultSet rs = stmt.executeQuery(); ) {
            rs.next();
            if (rs.getInt(1) != 0) return true;
            ProgramUtils.print(2, id + " does not exist in " + columnName + " of " + tableName);
            return false;
        }
    }

    // Getters
    @SafeVarargs public static Project getProject(int id, ObservableList<Object>... projects) {
        ObservableList<Object> projectList;
        if (projects.length == 0) projectList = readTableData("PROJECTS");
        else projectList = projects[0];
        for (Object p : projectList) {
            Project project = (Project) p;
            if (project.getProject_id() == id) return project;
        }
        return null;
    }
    @SafeVarargs public static SchoolData getSchoolData(ObservableList<Object>... schoolData) {
        ObservableList<Object> schoolDataList;
        if (schoolData.length == 0) schoolDataList = readTableData("SCHOOLDATA");
        else schoolDataList = schoolData[0];
        return (SchoolData) schoolDataList.get(0);
    }
    @SafeVarargs public static User getUser(int id, ObservableList<Object>... users) {
        ObservableList<Object> userList;
        if (users.length == 0) userList = readTableData("USERS");
        else userList = users[0];
        for (Object u : userList) {
            User user = (User) u;
            if (user.getUser_id() == id) return user;
        }
        return null;
    }
    @SafeVarargs public static Officer getOfficer(int id, ObservableList<Object>... officers) {
        ObservableList<Object> officerList;
        if (officers.length == 0) officerList = readTableData("PROJECTS");
        else officerList = officers[0];
        for (Object o : officerList) {
            Officer officer = (Officer) o;
            if (officer.getOfficer_id() == id) return officer;
        }
        return null;
    }

    // Editable
    private static ObservableList<Object> getTableData(ResultSet rs, String table) {
        ObservableList<Object> resultList = FXCollections.observableArrayList();
        if (rs == null) return resultList;
        try {
            while (rs.next()) {
                switch (table) {
                    case "USERS" -> {
                        User user = new User(
                                rs.getInt("USER_ID"),
                                rs.getString("FIRSTNAME"),
                                rs.getString("MIDDLEINITIAL"),
                                rs.getString("LASTNAME"),
                                rs.getString("USERNAME"),
                                rs.getString("PASSWORD"),
                                rs.getBoolean("ISADMIN"),
                                rs.getDate("USER_CD")
                        );
                        resultList.add(user);
                    }
                    case "OFFICERS" -> {
                        Officer officer = new Officer(
                                rs.getInt("OFFICER_ID"),
                                rs.getString("FIRSTNAME"),
                                rs.getString("MIDDLEINITIAL"),
                                rs.getString("LASTNAME"),
                                rs.getString("DESCRIPTION"),
                                rs.getString(("POSITION")),
                                rs.getString("STRAND"),
                                rs.getInt("USER_ID"),
                                rs.getInt("TERM"),
                                rs.getTimestamp("UPDATETIME"),
                                rs.getBlob("AVATAR"),
                                rs.getDate("OFFICER_CD")
                        );
                        resultList.add(officer);
                    }
                    case "EXPENSES" -> {
                        Expense expense = new Expense(
                                rs.getInt("EXPENSE_ID"),
                                rs.getInt("PROJECT_ID"),
                                rs.getString("ITEMNAME"),
                                rs.getDouble("TOTALPRICE"),
                                rs.getDate("EXPENSEDATE_CD"),
                                rs.getDouble("QUANTITY"),
                                rs.getDouble("UNITPRICE"),
                                rs.getInt("STATUS"),
                                rs.getTimestamp("UPDATETIME")
                        );
                        resultList.add(expense);
                    }
                    case "PROJECTS" -> {
                        Project project = new Project(
                                rs.getInt("PROJECT_ID"),
                                rs.getString("TITLE"),
                                rs.getString("DESCRIPTION"),
                                rs.getInt("USER_ID"),
                                rs.getDate("PROJECT_CD"),
                                rs.getDate("EVENTDATE"),
                                rs.getTimestamp("UPDATETIME")
                        );
                        resultList.add(project);
                    }
                    case "CONTRIBUTORS" -> {
                        Contributors contributors = new Contributors(
                                rs.getInt("PROJECT_ID"),
                                rs.getInt("OFFICER_ID")
                        );
                        resultList.add(contributors);
                    }
                    case "FUNDS" -> {
                        Fund fund = new Fund(
                                rs.getInt("FUND_ID"),
                                rs.getDouble("AMOUNT"),
                                rs.getTimestamp("UPDATETIME"),
                                rs.getDate("FUND_CD"),
                                rs.getString("DESCRIPTION")
                        );
                        resultList.add(fund);
                    }
                    case "SCHOOLDATA" -> {
                        SchoolData schoolData = new SchoolData(
                                rs.getInt("DATA_ID"),
                                rs.getTimestamp("UPDATETIME"),
                                rs.getInt("SCHOOLYEAR"),
                                rs.getBlob("SCHOOLLOGO"),
                                rs.getBlob("SSGLOGO"),
                                rs.getString("REPORTEXPORTLOCATION"),
                                rs.getBoolean("VIEWPDF"),
                                rs.getBoolean("CURRENTSCHOOLYEAR"),
                                rs.getString("SSGADVISER"),
                                rs.getString("PRINCIPAL"),
                                rs.getString("PROPOSALPARAGRAPH")
                        );
                        resultList.add(schoolData);
                    }
                }
            }
            return resultList;
        } catch (SQLException e) {
            ProgramUtils.print(2, e.getMessage());
            return null;
        }
    }

}
