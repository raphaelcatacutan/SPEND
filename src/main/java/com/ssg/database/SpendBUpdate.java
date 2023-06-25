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
    private static void updateTableData(String table, Object[] values, LinkedHashMap<String, String> column, String[] filters, boolean skipNull, boolean reload) throws SQLException, FileNotFoundException {
        Connection conn = SpendBConnection.getConnection();
        StringBuilder sql = new StringBuilder("UPDATE " + table + " SET ");
        List<String> columnsToUpdate = new ArrayList<>(column.keySet());
        for (int i = 0; i < values.length; i++) {
            if (values[i] == null && skipNull) continue;
            sql.append(columnsToUpdate.get(i)).append(" = ?, ");
        }
        sql.append("UPDATETIME = CURRENT_TIMESTAMP");
        sql.append(" WHERE ");
        for (int i = 0; i < filters.length; i++) {
            if (i > 0) sql.append(" AND ");
            sql.append(filters[i]);
        }
        PreparedStatement stmt = conn.prepareStatement(sql.toString());
        int setColumns = 0;
        for (int i = 0; i < values.length; i++) {
            Object value = values[i];
            if (value == null && skipNull) continue;
            String dataType = column.get(columnsToUpdate.get(i));
            SpendBUtils.analyzeValue(stmt, value, setColumns + 1, dataType);
            setColumns++;
        }
        stmt.executeUpdate();
        if (!reload) return;
        SpendBUtils.spendBUpdate(true, table);
        ControllerUtils.triggerEvent("refreshViews");
    }

    // Array Parameter Implementation
    public static void updateUser(Object[] values, boolean skipNull, boolean reload, String... filters) throws Exception {
        updateTableData("USERS", values, SpendBConnection.getColumnNames("USERS"), filters, skipNull, reload);
    }
    public static void updateOfficer(Object[] values, boolean skipNull, boolean reload, String... filters) throws Exception {
        checkIfIdExists("USERS", "USER_ID", (Integer) values[6]);
        updateTableData("OFFICERS", values, SpendBConnection.getColumnNames("OFFICERS"), filters, skipNull, reload);
    }
    public static void updateProject(Object[] values, boolean skipNull, boolean reload, String... filters) throws Exception {
        checkIfIdExists("USERS", "USER_ID", (Integer) values[2]);
        updateTableData("PROJECTS", values, SpendBConnection.getColumnNames("PROJECTS"), filters, skipNull, reload);
    }
    public static void updateExpense(Object[] values, boolean skipNull, boolean reload, String... filters) throws Exception {
        checkIfIdExists("PROJECTS", "PROJECT_ID", (Integer) values[0]);
        updateTableData("EXPENSES", values, SpendBConnection.getColumnNames("EXPENSES"), filters, skipNull, reload);
    }
    public static void updateContributor(Object[] values, boolean skipNull, boolean reload, String... filters) throws Exception {
        checkIfIdExists("PROJECTS", "PROJECT_ID", (Integer) values[0]);
        checkIfIdExists("OFFICERS", "OFFICER_ID", (Integer) values[1]);
        updateTableData("CONTRIBUTORS", values, SpendBConnection.getColumnNames("CONTRIBUTORS"), filters, skipNull, reload);
    }
    public static void updateFund(Object[] values, boolean skipNull, boolean reload, String... filters) throws Exception {
        updateTableData("FUNDS", values, SpendBConnection.getColumnNames("FUNDS"), filters, skipNull, reload);
    }
    public static void updateSchoolData(Object[] values, boolean skipNull, boolean reload) throws Exception {
        String[] filters = {"DATA_ID = 1"};
        updateTableData("SCHOOLDATA", values, SpendBConnection.getColumnNames("SCHOOLDATA"), filters, skipNull, reload);
    }
}
