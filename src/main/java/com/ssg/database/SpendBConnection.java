
package com.ssg.database;

import com.ssg.utils.ProgramUtils;
import com.ssg.utils.RuntimeData;

import java.sql.*;
import java.util.*;


public class SpendBConnection {

    private static final String DATABASE_NAME = "spendB";
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_CONNECTION = "jdbc:mysql://localhost:3306/";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    private static Connection connection;
    public static final Map<String, List<String>> TABLECOLUMNS = new LinkedHashMap<>();

    // Editable
    static {
        /*
         * Adding Columns in a Table:
         *    Edit this HashMap
         *    Edit the Reading
         *    Edit the Updating
         *    Edit the Creating
         *    Generate Random Data
         *    Model Data
         *    Model Values
         * In creating data, columns ending with _ID, _CD and is a TIMESTAMP are skipped
         */
        TABLECOLUMNS.put("USERS", Arrays.asList(
                "USER_ID/INT",
                "FIRSTNAME/VARCHAR(255)",
                "MIDDLEINITIAL/VARCHAR(255)",
                "LASTNAME/VARCHAR(255)",
                "USERNAME/VARCHAR(255)",
                "PASSWORD/VARCHAR(255)",
                "ISADMIN/BIT",
                "USER_CD/DATE",
                "UPDATETIME/TIMESTAMP"
        ));
        TABLECOLUMNS.put("OFFICERS", Arrays.asList(
                "OFFICER_ID/INT",
                "FIRSTNAME/VARCHAR(255)",
                "MIDDLEINITIAL/VARCHAR(255)",
                "LASTNAME/VARCHAR(255)",
                "DESCRIPTION/VARCHAR(400)",
                "POSITION/VARCHAR(255)",
                "STRAND/VARCHAR(255)",
                "USER_ID/INT/USERS",
                "OFFICER_CD/DATE",
                "TERM/INT",
                "UPDATETIME/TIMESTAMP",
                "AVATAR/LONGBLOB"
        ));
        TABLECOLUMNS.put("PROJECTS", Arrays.asList(
                "PROJECT_ID/INT",
                "TITLE/VARCHAR(255)",
                "DESCRIPTION/VARCHAR(400)",
                "USER_ID/INT/USERS",
                "PROJECT_CD/DATE",
                "EVENTDATE/DATE",
                "UPDATETIME/TIMESTAMP"
        ));
        TABLECOLUMNS.put("EXPENSES", Arrays.asList(
                "EXPENSE_ID/INT",
                "PROJECT_ID/INT/PROJECTS",
                "ITEMNAME/VARCHAR(255)",
                "TOTALPRICE/DOUBLE",
                "EXPENSEDATE_CD/DATE",
                "QUANTITY/DOUBLE",
                "UNITPRICE/DOUBLE",
                "STATUS/INT",
                "UPDATETIME/TIMESTAMP"
        ));
        TABLECOLUMNS.put("CONTRIBUTORS", Arrays.asList(
                "PROJECT_ID/INT/PROJECTS",
                "OFFICER_ID/INT/OFFICERS",
                "UPDATETIME/TIMESTAMP"
        ));
        TABLECOLUMNS.put("FUNDS", Arrays.asList(
                "FUND_ID/INT",
                "AMOUNT/DOUBLE",
                "DESCRIPTION/VARCHAR(400)",
                "UPDATETIME/TIMESTAMP",
                "FUND_CD/DATE"
        ));
        TABLECOLUMNS.put("SCHOOLDATA", Arrays.asList(
                "DATA_ID/INT",
                "UPDATETIME/TIMESTAMP",
                "SCHOOLYEAR/INT",
                "SCHOOLLOGO/LONGBLOB",
                "SSGLOGO/LONGBLOB",
                "REPORTEXPORTLOCATION/VARCHAR(255)",
                "VIEWPDF/BIT",
                "CURRENTSCHOOLYEAR/BIT",
                "SSGADVISER/VARCHAR(255)",
                "PRINCIPAL/VARCHAR(255)",
                "PROPOSALPARAGRAPH/VARCHAR(500)"
        ));
    }

    /**
     * Initializes the database connection and sets up the necessary components.
     * This method checks if the database needs to be set up, retrieves the database connection,
     * sends a packet, sets up the required table if a new database was created,
     * and generates pre-fill data if the `FILLDATA` flag is enabled.
     */
    public static void initializeConnection() {
        try {
            boolean newDatabase = setupDatabase();
            connection = SpendBConnection.getDBConnection();
            SpendBUtils.spendBPacket(5048576);
            if (!newDatabase) return;
            setupTable();
            if (RuntimeData.FILLDATA) SpendBPrefill.generate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets up the database by creating a new database or dropping an existing one if necessary.
     *
     * @return {@code true} if the database setup was successful, {@code false} if the setup was skipped.
     * @throws RuntimeException if a MySQL connection error occurs during the setup.
     */
    private static boolean setupDatabase() {
        try (Connection tempConnection = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD)) {

            // Check if the database exists
            String checkSql = "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = ?";
            try (PreparedStatement checkStatement = tempConnection.prepareStatement(checkSql)) {
                checkStatement.setString(1, DATABASE_NAME);
                try (ResultSet rs = checkStatement.executeQuery()) {
                    if (rs.next()) {
                        if (RuntimeData.CREATEDATABASE) {
                            String dropSql = "DROP DATABASE " + DATABASE_NAME;
                            tempConnection.createStatement().executeUpdate(dropSql);
                        } else {
                            return false;
                        }
                    } else {
                        String createSql = "CREATE DATABASE " + DATABASE_NAME;
                        tempConnection.createStatement().executeUpdate(createSql);
                    }
                }
            }
            return true;
        } catch (SQLException e) {
            throw new RuntimeException("MySQL connection error occurred during database setup.", e);
        }
    }


    /**
     * Sets up the database tables by creating them if they do not already exist.
     *
     * @throws SQLException if a database error occurs while setting up the tables.
     */
    private static void setupTable() throws SQLException {
        Connection connection = getConnection();
        Statement stmt = connection.createStatement();

        for (String tableName : TABLECOLUMNS.keySet()) {
            List<String> columns = TABLECOLUMNS.get(tableName);

            StringBuilder sb = new StringBuilder();
            sb.append("CREATE TABLE IF NOT EXISTS ").append(tableName).append(" (");
            int i = 0;
            for (String column : columns) {
                String[] tokens = column.split("/");
                String columnName = tokens[0];
                String columnType = tokens[1];

                boolean isReferenced = tokens.length == 3;
                boolean isPrimary = columnName.endsWith("_ID") && !isReferenced;
                boolean isTimestamp = Objects.equals(columnType, "TIMESTAMP");
                boolean isDate = Objects.equals(columnType, "DATE");

                sb.append(columnName).append(" ").append(columnType);

                if (isPrimary) sb.append(" AUTO_INCREMENT PRIMARY KEY");
                if (isTimestamp || isDate) sb.append(" DEFAULT CURRENT_TIMESTAMP");
                if (isReferenced) sb.append(", FOREIGN KEY (").append(columnName).append(") REFERENCES ").append(tokens[2]).append("(").append(columnName).append(") ON DELETE CASCADE");

                if (i < columns.size() - 1) sb.append(", ");
                i++;
            }
            sb.append(");");
            String sql = sb.toString();
            stmt.executeUpdate(sql);
        }
    }

    /**
     * Retrieves a database connection using the specified driver, connection URL, database name, username, and password.
     * This method dynamically loads the JDBC driver at runtime and establishes a connection to the database.
     *
     * @return the database connection if successful, or null if an error occurs
     */
    private static Connection getDBConnection() {
        try {
            Class.forName(DB_DRIVER); // Load a JDBC driver dynamically at runtime.
            return DriverManager.getConnection(DB_CONNECTION + DATABASE_NAME, DB_USER, DB_PASSWORD);
        } catch (Exception e) {
            ProgramUtils.showDialogMessage("MYSQL Connection Error", "The MySQL connection encountered an error while initializing.\nPlease ensure that the MySQL server is running and accessible.\nVerify that the server is up and running properly to establish\na successful connection.");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gets the columns of a table excluding
     * Columns ending with "_ID" (AND not referenced) or "CD", Columns with TIMESTAMP type
     * @param tableName The name of the table
     * @return A LinkedHashMap containing the name as key and type as the value of the column
     */
    public static LinkedHashMap<String, String> getColumnNames(String tableName) {
        LinkedHashMap<String, String> columns = new LinkedHashMap<>();
        for (String column: TABLECOLUMNS.get(tableName)) {
            String[] tokens = column.split("/");
            String columnName = tokens[0];
            String columnType = tokens[1];
            boolean isReferenced = tokens.length == 3;
            boolean isInserted = !columnName.endsWith("_ID") || isReferenced;
            boolean isTimestamp = Objects.equals(columnType, "TIMESTAMP");
            boolean isCreationDate = columnName.endsWith("_CD");
            if (!isInserted || isTimestamp || isCreationDate) continue;
            columns.put(columnName, columnType);
        }
        return columns;
    }

    /**
     * Retrieves the established database connection.
     *
     * @return the established database connection
     * @throws AssertionError if the connection is null
     */
    public static Connection getConnection() {
        assert connection != null;
        return connection;
    }

}
