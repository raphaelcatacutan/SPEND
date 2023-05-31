package com.ssg.database;

import com.ssg.views.ControllerUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SpendBDelete {
    /**
     * Deletes data from a database table
     * @param table The table
     * @param allFilters If all filters should be met
     * @param filters Array of string filters
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
            throw new RuntimeException(e);
        }
    }

}
