package com.ssg.database;

import com.ssg.database.models.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class SpendBRead {
    private static final boolean defaultBool = false;

    // TODO Remove Filters
    private static ResultSet tableQuery(String table, String[] filters, boolean all) {
        try {
            StringBuilder query = new StringBuilder("SELECT * FROM " + table);
            if (filters.length > 0) query.append(" WHERE ");
            for (int i = 0; i < filters.length; i++) query.append(filters[i]).append(i == filters.length - 1 ? "" : all ? " AND " : " OR ");
            Statement stmt = SpendBConnection.getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            return stmt.executeQuery(query.toString());
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static ObservableList<Object> readTableData(String table) {
        ResultSet rs = tableQuery(table, SpendBUtils.EMPTY, defaultBool);
        return getTableData(rs, table);
    }
    public static ObservableList<Object> readTableData(String table, boolean all, String... filters) {
        ResultSet rs = tableQuery(table, filters, all);
        return getTableData(rs, table);
    }
    public static Map<String, ObservableList<Object>> readTablesData() {
        Map<String, ObservableList<Object>> tableDataMap = new HashMap<>();
        for (String x : SpendBUtils.SPENDBTABLES) {
            if (!SpendBUtils.spendBHasChanges(x)) continue;
            tableDataMap.put(x, readTableData(x));
        }
        return tableDataMap;
    }

    public static String generateQuery(String table, boolean all, String... filters) {
        StringBuilder query = new StringBuilder("SELECT * FROM " + table);
        if (filters.length > 0) query.append(" WHERE ");
        for (int i = 0; i < filters.length; i++) query.append(filters[i]).append(i == filters.length - 1 ? "" : all ? " AND " : " OR ");
        return query.toString();
    }

    public static void checkIfIdExists(String tableName, String columnName, int id) throws Exception {
        String query = "SELECT COUNT(*) FROM " + tableName + " WHERE " + columnName + " = " + id;
        PreparedStatement stmt = SpendBConnection.getConnection().prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        int count = rs.getInt(1);
        rs.close();
        stmt.close();
        if (count != 0) return;
        System.out.println("Error: " + id + " does not exist in " + columnName + " of " + tableName);
        throw new Exception();
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
    @SafeVarargs public static User getUser(int id, ObservableList<Object>... users) {
        ObservableList<Object> userList;
        if (users.length == 0) userList = readTableData("PROJECTS");
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
    // TODO Add more getters

    // Editable
    private static ObservableList<Object> getTableData (ResultSet rs, String table) {
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
                                rs.getBoolean("ISADMIN")
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
                                rs.getBlob("AVATAR")
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
                                rs.getInt("STATUS")
                        );
                        resultList.add(expense);
                    }
                    case "PROJECTS" -> {
                        Project project = new Project(
                                rs.getInt("PROJECT_ID"),
                                rs.getString("TITLE"),
                                rs.getString("DESCRIPTION"),
                                rs.getInt("USER_ID"),
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
                }
            }
            return resultList;
        } catch (SQLException e) {
            System.out.println("Error in Getting Data: " + e.getMessage());
            return null;
        }
    }

}
