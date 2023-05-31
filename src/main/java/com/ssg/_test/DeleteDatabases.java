package com.ssg._test;

import java.sql.*;

public class DeleteDatabases {
    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            // Load the JDBC driver and establish a connection to the database
            Class.forName("com.mysql.cj.jdbc.Driver");
            String DB_CONNECTION = "jdbc:mysql://localhost:3306/";
            String DB_USER = "root";
            String DB_PASSWORD = "";
            conn = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);

            // Create a statement and execute a query to retrieve all databases with a name containing "spendbtest"
            stmt = conn.createStatement();
            String sql = "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME LIKE '%spendbtest%'";
            rs = stmt.executeQuery(sql);

            // Loop through the results and drop each database
            while (rs.next()) {
                String dbName = rs.getString("SCHEMA_NAME");
                String dropSql = "DROP DATABASE " + dbName;
                stmt.executeUpdate(dropSql);
                System.out.println("Dropped database: " + dbName);
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            // Close the resources
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

