package com.ssg.database;

import com.ssg.views.ControllerUtils;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static com.ssg.database.SpendBRead.checkIfIdExists;

public class SpendBCreate {
    private static void createTableData(String table, Object[] values, LinkedHashMap<String, String> column) throws SQLException, FileNotFoundException {
        List<String> col = new ArrayList<>(column.keySet());
        Connection conn = SpendBConnection.getConnection();
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("INSERT INTO ").append(table).append("(");
        for (int i = 0; i < values.length; i++) {
            sqlBuilder.append(col.get(i));
            if (i < values.length - 1) sqlBuilder.append(", ");
        }
        sqlBuilder.append(") VALUES (");
        for (int i = 0; i < values.length; i++) {
            sqlBuilder.append("?");
            if (i < values.length - 1) sqlBuilder.append(", ");
        }
        sqlBuilder.append(")");
        PreparedStatement stmt = conn.prepareStatement(sqlBuilder.toString());
        for (int i = 0; i < values.length; i++) {
            Object value = values[i];
            String dataType = column.get(col.get(i));
            int index = i + 1;
            SpendBUtils.analyzeValue(stmt, value, index, dataType);
        }
        stmt.executeUpdate();
        SpendBUtils.spendBUpdate(true, table);
        ControllerUtils.triggerEvent("refreshViews");
    }

    // Array Parameter Implementation
    // TODO Make this in one method only
    public static void createUser(Object[] values) throws Exception {
        createTableData("USERS", values, SpendBConnection.getColumnNames("USERS"));
    }
    public static void createOfficer(Object[] values) throws Exception {
        checkIfIdExists("USERS", "USER_ID", (Integer) values[6]);
        createTableData("OFFICERS", values, SpendBConnection.getColumnNames("OFFICERS"));
    }
    public static void createProject(Object[] values) throws Exception {
        checkIfIdExists("USERS", "USER_ID", (Integer) values[2]);
        createTableData("PROJECTS", values, SpendBConnection.getColumnNames("PROJECTS"));
    }
    public static void createExpenses(Object[] values) throws Exception {
        checkIfIdExists("PROJECTS", "PROJECT_ID", (Integer) values[0]);
        createTableData("EXPENSES", values, SpendBConnection.getColumnNames("EXPENSES"));
    }
    public static void createContributors(Object[] values) throws Exception {
        checkIfIdExists("PROJECTS", "PROJECT_ID", (Integer) values[0]);
        checkIfIdExists("OFFICERS", "OFFICER_ID", (Integer) values[1]);
        createTableData("CONTRIBUTORS", values, SpendBConnection.getColumnNames("CONTRIBUTORS"));
    }
}
