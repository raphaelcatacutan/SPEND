package com.ssg._test;

import java.io.*;
import java.sql.*;

public class MySQLBackup {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/spendbbackup";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public static void main(String[] args) {
        String backupFolderPath = "G:\\Downloads\\backup\\";
        String backupFileName = "backup.sql";
        String importFilePath = "G:\\Downloads\\backup\\backup.sql";

        // Backup all tables
        backupTables(backupFolderPath, backupFileName);

        // Import all tables
        importTables(importFilePath);
    }

    public static void backupTables(String folderPath, String fileName) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement();
             BufferedWriter writer = new BufferedWriter(new FileWriter(folderPath + fileName))) {

            // Get all table names
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet tableResultSet = metaData.getTables(null, null, "%", null);
            while (tableResultSet.next()) {
                String tableName = tableResultSet.getString(3);

                // Export table structure and data
                String query = "SELECT * INTO OUTFILE '" + folderPath + tableName + ".txt' " +
                        "FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '\"' " +
                        "LINES TERMINATED BY '\\n' FROM " + tableName;
                statement.execute(query);

                // Export blob data separately
                ResultSet blobResultSet = statement.executeQuery("SELECT * FROM " + tableName + " WHERE column_with_blob IS NOT NULL");
                while (blobResultSet.next()) {
                    Blob blob = blobResultSet.getBlob("column_with_blob");
                    try (InputStream inputStream = blob.getBinaryStream();
                         OutputStream outputStream = new FileOutputStream(folderPath + tableName + "_blob.txt")) {

                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }
                    }
                }
            }

            System.out.println("Backup completed successfully.");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void importTables(String filePath) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement()) {

            // Read backup SQL file
            StringBuilder builder = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line).append("\n");
                }
            }

            // Execute SQL statements to import tables
            String[] sqlStatements = builder.toString().split(";");
            for (String sqlStatement : sqlStatements) {
                statement.execute(sqlStatement);
            }

            System.out.println("Import completed successfully.");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}
