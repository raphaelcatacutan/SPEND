package com.ssg._test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;

public class DatabaseExporter {

    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/spendBBackup";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    public static void exportDatabase(String filePath) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement();
             FileWriter fileWriter = new FileWriter(filePath)) {

            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet tableResultSet = metaData.getTables(connection.getCatalog(), null, null, new String[]{"TABLE"});

            while (tableResultSet.next()) {
                String tableName = tableResultSet.getString("TABLE_NAME");
                exportTable(statement, tableName, fileWriter);
            }

            System.out.println("Database export completed successfully.");

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void exportTable(Statement statement, String tableName, FileWriter fileWriter) throws SQLException, IOException {
        ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName);

        fileWriter.write("-- Table: " + tableName + System.lineSeparator());

        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        // Write column names
        for (int i = 1; i <= columnCount; i++) {
            fileWriter.write(metaData.getColumnName(i));
            if (i < columnCount) {
                fileWriter.write(",");
            }
        }
        fileWriter.write(System.lineSeparator());

        // Write table data
        while (resultSet.next()) {
            for (int i = 1; i <= columnCount; i++) {
                if (metaData.getColumnType(i) == Types.BLOB) {
                    writeBlobData(resultSet.getBlob(i), fileWriter);
                } else {
                    String value = resultSet.getString(i);
                    if (value != null) {
                        fileWriter.write(value);
                    }
                }
                if (i < columnCount) {
                    fileWriter.write(",");
                }
            }
            fileWriter.write(System.lineSeparator());
        }

        fileWriter.write(System.lineSeparator());
        resultSet.close();
    }

    private static void writeBlobData(Blob blob, FileWriter fileWriter) throws SQLException, IOException {
        if (blob == null) {
            return;
        }

        InputStream inputStream = blob.getBinaryStream();
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            String data = new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);
            fileWriter.write(data);
        }
        inputStream.close();
    }


    public static void main(String[] args) {
        String exportFilePath = "G:\\Downloads\\backup.sql";
        exportDatabase(exportFilePath);
    }
}
