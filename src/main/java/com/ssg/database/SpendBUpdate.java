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

public class SpendBUpdate {
    private static void updateTableData(String table, Object[] values, LinkedHashMap<String, String> column, String[] filters, boolean skipNull) throws SQLException, FileNotFoundException {
        Connection conn = SpendBConnection.getConnection();
        StringBuilder sql = new StringBuilder("UPDATE " + table + " SET ");
        List<String> columnsToUpdate = new ArrayList<>(column.keySet());
        for (int i = 0; i < values.length; i++) {
            if (values[i] == null && skipNull) continue;
            if (i > 0) sql.append(", ");
            sql.append(columnsToUpdate.get(i)).append(" = ?");
        }
        sql.append(" WHERE ");
        for (int i = 0; i < filters.length; i++) {
            if (i > 0) sql.append(" AND ");
            sql.append(filters[i]);
        }
        PreparedStatement stmt = conn.prepareStatement(sql.toString());
        for (int i = 0; i < values.length; i++) {
            Object value = values[i];
            if (value == null && skipNull) continue;
            int index = i + 1;
            String dataType = column.get(columnsToUpdate.get(i));
            SpendBUtils.analyzeValue(stmt, value, index, dataType);
        }
        stmt.executeUpdate();
        SpendBUtils.spendBUpdate(true, table);
        ControllerUtils.triggerEvent("refreshViews");
    }

    // Array Parameter Implementation
    // TODO Make this Automatic ID without filters
    public static void updateUser(Object[] values, boolean skipNull, String... filters) throws Exception {
        updateTableData("USERS", values, SpendBConnection.getColumnNames("USERS"), filters, skipNull);
    }
    public static void updateOfficer(Object[] values, boolean skipNull, String... filters) throws Exception {
        checkIfIdExists("USERS", "USER_ID", (Integer) values[6]);
        updateTableData("OFFICERS", values, SpendBConnection.getColumnNames("OFFICERS"), filters, skipNull);
    }
    public static void updateProject(Object[] values, boolean skipNull, String... filters) throws Exception {
        checkIfIdExists("USERS", "USER_ID", (Integer) values[2]);
        updateTableData("PROJECTS", values, SpendBConnection.getColumnNames("PROJECTS"), filters, skipNull);
    }
    public static void updateExpense(Object[] values, boolean skipNull, String... filters) throws Exception {
        checkIfIdExists("PROJECTS", "PROJECT_ID", (Integer) values[0]);
        updateTableData("EXPENSES", values, SpendBConnection.getColumnNames("EXPENSES"), filters, skipNull);
    }
    public static void updateContributor(Object[] values, boolean skipNull, String... filters) throws Exception {
        checkIfIdExists("PROJECTS", "PROJECT_ID", (Integer) values[0]);
        checkIfIdExists("OFFICERS", "OFFICER_ID", (Integer) values[1]);
        updateTableData("CONTRIBUTORS", values, SpendBConnection.getColumnNames("CONTRIBUTORS"), filters, skipNull);
    }
}
