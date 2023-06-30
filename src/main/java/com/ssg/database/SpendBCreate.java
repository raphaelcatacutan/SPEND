package com.ssg.database;

import com.ssg.views.ControllerUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static com.ssg.database.SpendBRead.checkIfIdExists;

public class SpendBCreate {
    /**
     * Creates a new record in the specified table with the provided values.
     *
     * @param table   the name of the table in which to insert the record
     * @param values  an array of values representing the data to be inserted
     * @param column  a LinkedHashMap containing the column names as keys and their corresponding data types as values
     * @param reload  a boolean flag indicating whether to reload views or not after the record is inserted
     * @throws SQLException if an error occurs while accessing the database
     */
    private static void createTableData(String table, Object[] values, LinkedHashMap<String, String> column, boolean reload) throws SQLException {
        List<String> col = new ArrayList<>(column.keySet());
        Connection conn = SpendBConnection.getConnection();
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("INSERT IGNORE INTO ").append(table).append("(");
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
        if (reload) ControllerUtils.triggerEvent("refreshViews");
    }

    // Array Parameter Implementation
    public static void createUser(Object[] values, boolean... reload) throws Exception {
        boolean reloadView = false;
        if (reload.length == 1) reloadView = reload[0];
        createTableData("USERS", values, SpendBConnection.getColumnNames("USERS"), reloadView);
    }
    public static void createOfficer(Object[] values, boolean... reload) throws Exception {
        boolean reloadView = false;
        if (reload.length == 1) reloadView = reload[0];
        if (!checkIfIdExists("USERS", "USER_ID", (Integer) values[6])) return;
        createTableData("OFFICERS", values, SpendBConnection.getColumnNames("OFFICERS"), reloadView);
    }
    public static void createProject(Object[] values, boolean... reload) throws Exception {
        boolean reloadView = false;
        if (reload.length == 1) reloadView = reload[0];
        if (!checkIfIdExists("USERS", "USER_ID", (Integer) values[2])) return;
        createTableData("PROJECTS", values, SpendBConnection.getColumnNames("PROJECTS"), reloadView);
    }
    public static void createExpenses(Object[] values, boolean... reload) throws Exception {
        boolean reloadView = false;
        if (reload.length == 1) reloadView = reload[0];
        if (!checkIfIdExists("PROJECTS", "PROJECT_ID", (Integer) values[0])) return;
        createTableData("EXPENSES", values, SpendBConnection.getColumnNames("EXPENSES"), reloadView);
    }
    public static void createContributors(Object[] values, boolean... reload) throws Exception {
        boolean reloadView = false;
        if (reload.length == 1) reloadView = reload[0];
        if (!checkIfIdExists("PROJECTS", "PROJECT_ID", (Integer) values[0])) return;
        if (!checkIfIdExists("OFFICERS", "OFFICER_ID", (Integer) values[1])) return;
        createTableData("CONTRIBUTORS", values, SpendBConnection.getColumnNames("CONTRIBUTORS"), reloadView);
    }
    public static void createSchoolData(Object[] values, boolean... reload) throws SQLException {
        boolean reloadView = false;
        if (reload.length == 1) reloadView = reload[0];
        createTableData("SCHOOLDATA", values, SpendBConnection.getColumnNames("SCHOOLDATA"), reloadView);
    }
    public static void createFundsData(Object[] values, boolean... reload) throws SQLException {
        boolean reloadView = false;
        if (reload.length == 1) reloadView = reload[0];
        createTableData("FUNDS", values, SpendBConnection.getColumnNames("FUNDS"), reloadView);
    }
}
