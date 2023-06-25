package com.ssg._test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DatabaseBackup {
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/spendbbackup";
    private static final String DB_URL2 = "jdbc:mysql://localhost:3306/spendnnnn";
    private static final String USER = "root";
    private static final String PASS = "";

    public static void backupDatabase(String backupFilePath) {
        try {
            Class.forName(JDBC_DRIVER);
            Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
            DatabaseMetaData metadata = connection.getMetaData();

            // Open the output file
            OutputStream outputStream = new FileOutputStream(backupFilePath);

            // Get all tables in the specific database, excluding system tables
            String catalog = connection.getCatalog();
            String[] tableTypes = { "TABLE" };
            ResultSet tableResultSet = metadata.getTables(catalog, null, "%", tableTypes);

            // Export table data
            ResultSet dataResultSet;
            ResultSetMetaData resultSetMetaData;
            int columnCount;
            StringBuilder insertStatement;

            while (tableResultSet.next()) {
                String tableName = tableResultSet.getString("TABLE_NAME");

                Statement statement = connection.createStatement();

                // Export table structure
                ResultSet structureResultSet = statement.executeQuery("SHOW CREATE TABLE " + tableName);
                if (structureResultSet.next()) {
                    String createTableStatement = structureResultSet.getString("Create Table");
                    outputStream.write((createTableStatement + ";\n\n").getBytes());
                }
                structureResultSet.close();
            }
            tableResultSet.absolute(0);
            while (tableResultSet.next()) {
                String tableName = tableResultSet.getString("TABLE_NAME");

                Statement statement = connection.createStatement();

                // Export table data
                System.out.println("Exporting " + tableName);
                dataResultSet = statement.executeQuery("SELECT * FROM " + tableName);
                resultSetMetaData = dataResultSet.getMetaData();
                columnCount = resultSetMetaData.getColumnCount();
                insertStatement = new StringBuilder();

                while (dataResultSet.next()) {
                    insertStatement.setLength(0); // Clear previous statement
                    insertStatement.append("INSERT INTO " + tableName + " VALUES (");
                    for (int i = 1; i <= columnCount; i++) {
                        Object value = dataResultSet.getObject(i);
                        int columnType = resultSetMetaData.getColumnType(i);

                        if (value == null) {
                            insertStatement.append("NULL");
                        } else {
                            switch (columnType) {
                                case Types.BIT:
                                    // BIT type
                                    insertStatement.append((Boolean) value ? "1" : "0");
                                    break;
                                case Types.TIMESTAMP:
                                    // TIMESTAMP type
                                    insertStatement.append("'").append(value.toString()).append("'");
                                    break;
                                case Types.DATE:
                                    // DATE type
                                    insertStatement.append("'").append(value.toString()).append("'");
                                    break;
                                case Types.INTEGER:
                                    // INTEGER type
                                    insertStatement.append(value.toString());
                                    break;
                                case Types.BLOB:
                                    // BLOB type (byte[])
                                    insertStatement.append("0x").append(bytesToHex((byte[]) value));
                                    break;
                                case Types.VARCHAR:
                                case Types.CHAR:
                                    // String types
                                    insertStatement.append("'").append(value.toString().replace("'", "''")).append("'");
                                    break;
                                // Add more cases for other data types as needed
                                default:
                                    insertStatement.append(value.toString());
                                    break;
                            }
                        }

                        if (i < columnCount) {
                            insertStatement.append(", ");
                        }
                    }

                    insertStatement.append(");\n");
                    outputStream.write(insertStatement.toString().getBytes());
                }

                statement.close();
            }

            // Close the output file
            outputStream.close();
            tableResultSet.close();
            connection.close();

            System.out.println("Database backup completed successfully!");
        } catch (ClassNotFoundException | SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexStringBuilder = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xFF & b);
            if (hex.length() == 1) {
                hexStringBuilder.append('0');
            }
            hexStringBuilder.append(hex);
        }
        return hexStringBuilder.toString();
    }
    public static void restoreDatabase(String sqlFilePath) {
        try {
            Class.forName(JDBC_DRIVER);
            Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);

            StringBuilder scriptContent = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new FileReader(sqlFilePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    scriptContent.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            String[] queries = scriptContent.toString().split(";");

            try (Statement statement = connection.createStatement()) {
                for (String query : queries) {
                    try {
                        statement.executeUpdate(query);
                    }  catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            System.out.println("Database restoration completed successfully!");
            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
    public static void executeSqlFile(String sqlFilePath) {
        try {
            Class.forName(JDBC_DRIVER);
            Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement statement = connection.createStatement();

            // Read the SQL file
            String sql = new String(Files.readAllBytes(Paths.get(sqlFilePath)));

            // Split SQL statements based on semicolon
            String[] sqlStatements = sql.split(";");

            // Execute each SQL statement
            for (String sqlStatement : sqlStatements) {
                if (!sqlStatement.trim().isEmpty()) {
                    // Handle binary data
                    if (sqlStatement.contains("[B@")) {
                        // Replace binary data placeholder with hex representation
                        String hexData = extractHexData(sqlStatement);
                        sqlStatement = sqlStatement.replaceFirst("\\[B@", hexData);
                    }

                    // Execute the SQL statement
                    statement.executeUpdate(sqlStatement);
                }
            }

            statement.close();
            connection.close();

            System.out.println("SQL file executed successfully!");
        } catch (ClassNotFoundException | SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private static String extractHexData(String sqlStatement) {
        Pattern pattern = Pattern.compile("\\[B@(\\p{XDigit}+)");
        Matcher matcher = pattern.matcher(sqlStatement);

        if (matcher.find()) {
            String hexData = matcher.group(1);
            return "X'" + hexData + "'";
        }

        return "";
    }

    public static void main(String[] args) {
//        backupDatabase("G:\\Downloads\\backup.sql");
        executeSqlFile("G:\\Downloads\\backup.sql");
    }
}
