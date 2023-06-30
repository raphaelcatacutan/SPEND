package com.ssg.database;

import com.ssg.views.ControllerUtils;
import com.ssg.views.MainEvents;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SpendBDelete {
    /**
     * Deletes data from the specified table based on the provided filters.
     *
     * @param table        the name of the table from which to delete data
     * @param allFilters   a boolean flag indicating whether all filters should be applied using the "AND" operator (true),
     *                     or any filter should be applied using the "OR" operator (false)
     * @param filters      an array of filters to apply for deleting data (optional)
     */
    public static void deleteTableData(String table, boolean allFilters, String... filters) {
        Connection conn = SpendBConnection.getConnection();

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("DELETE FROM ").append(table);
        if (filters != null && filters.length > 0) {
            sqlBuilder.append(" WHERE ");
            if (allFilters) sqlBuilder.append(String.join(" AND ", filters));
            else sqlBuilder.append(String.join(" OR ", filters));
        }

        try {
            PreparedStatement pstmt = conn.prepareStatement(sqlBuilder.toString());
            pstmt.executeUpdate();
            SpendBUtils.spendBUpdate(true, table);
            ControllerUtils.triggerEvent("refreshViews");
        } catch (SQLException e) {
            MainEvents.showDialogMessage("Unexpected Error", "Database operation failed. Please try again later or see the exception for further details", "Dimiss");
            throw new RuntimeException(e);
        }
    }

}
