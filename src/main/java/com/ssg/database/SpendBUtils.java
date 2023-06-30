package com.ssg.database;

import com.ssg.MainActivity;
import com.ssg.database.models.SchoolData;
import com.ssg.utils.DateUtils;
import com.ssg.utils.ProgramUtils;
import com.ssg.utils.RuntimeData;
import com.ssg.views.MainEvents;
import javafx.application.Platform;
import javafx.scene.image.Image;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.sql.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.ssg.views.ControllerUtils.DEFAULTAVATAR;

public class SpendBUtils {
    private static final HashMap<String, Boolean> SPENDBCHANGES = new HashMap<>();
    public static final String[] SPENDBTABLES = SpendBConnection.TABLECOLUMNS.keySet().toArray(new String[0]);

    static {
        spendBUpdate(true);
    }

    /**
     * Updates the state of SpendB changes for the specified table(s).
     *
     * @param state  the new state to set for the table(s)
     * @param table  optional parameter specifying the table(s) to update the state for;
     *               if not provided, the state will be updated for all SpendB tables
     */
    public static void spendBUpdate(boolean state, String... table) {
        if (table.length == 0) table = SPENDBTABLES;
        for (String x: table) SPENDBCHANGES.put(x, state);
    }

    /**
     * Tries to load the blob as a javafx image
     * @param blob The blob to load
     * @return The image instance of the blob or the default avatar if an error occurs
     */
    public static javafx.scene.image.Image loadBlob(Blob blob) {
        try {
            return new Image(new ByteArrayInputStream(blob.getBytes(1, (int) blob.length())));
        } catch (Exception e) {
            return DEFAULTAVATAR;
        }
    }

    /**
     * Sets the maximum allowed packet size for the connection.
     *
     * @param packetSize The desired maximum allowed packet size.
     */
    public static void spendBPacket(int packetSize) {
        try (Statement statement = SpendBConnection.getConnection().createStatement()) {
            String sql = "SET GLOBAL max_allowed_packet = " + packetSize;
            statement.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Generates a filter condition for filtering IDs based on the specified column name and inclusion flag.
     *
     * @param columnName The name of the column to filter.
     * @param in         A flag indicating whether the IDs should be included or excluded.
     * @param ids        An array of integer IDs to filter.
     * @return A filter condition string based on the specified parameters.
     */
    public static String spendBFilterID(String columnName, boolean in, int... ids) {
        if (ids.length == 0) return "1 = 1";
        StringBuilder output = new StringBuilder();
        output.append(columnName).append(in ? " in (" : " not in (");
        for (int i = 0; i < ids.length; i++) {
            output.append(ids[i]);
            if (i < ids.length - 1) output.append(", ");
        }
        output.append(")");
        return output.toString();
    }

    /**
     * Checks if any of the specified tables have changes.
     *
     * @param table An optional array of table names to check for changes. If not provided, all tables will be checked.
     * @return {@code true} if any of the specified tables (or all tables if none are provided) have changes, {@code false} otherwise.
     */
    public static boolean spendBHasChanges(String... table) {
        if (table.length == 0) table = SPENDBTABLES;
        return Arrays.stream(table).anyMatch(SPENDBCHANGES::get);
    }

    /**
     * Generates a report based on the specified template and queries.
     *
     * @param template The template identifier for the report.
     * @param queries  A map of queries to be used for generating the report.
     */
    public static void generateReport(int template, Map<String, String> queries) {
        MainEvents.showDialogMessage("Generate Report", "Your file is being generated. When the file is ready, it will be opened to your PDF viewer app based on your current settings.", "Dismiss");

        Thread thread = new Thread(() -> {
            try {
                String JRXMLname = switch (template) {
                    case 1 -> "expense-proposal";
                    case 2 -> "officer-details";
                    case 3 -> "officer-list";
                    case 4 -> "project-details";
                    case 5 -> "project-list";
                    case 6 -> "funds-history";
                    default -> throw new IllegalStateException("Unexpected value: " + template);
                };
                SchoolData setting = SpendBRead.getSchoolData();
                Map<String, Object> parameters = new HashMap<>();

                parameters.put("SSG_ADVISER", setting.getSsgAdviser());
                parameters.put("PRINCIPAL", setting.getPrincipal());
                parameters.put("LETTER_CONTENT", setting.getProposalParagraph());
                parameters.put("PREPARED", RuntimeData.USER.getFullName());

                String outputPath = setting.getReportExportLocation() + "\\" + JRXMLname + " " + ProgramUtils.currentTimeStampFileName() + ".pdf";
                String pathToJRXML = Objects.requireNonNull(MainActivity.class.getResource("reports/" + JRXMLname + ".jrxml")).toURI().getPath();
                JasperDesign jDesign = JRXmlLoader.load(pathToJRXML);
                queries.keySet().forEach(dataset -> {
                    if (dataset.equals("main")) {
                        JRDesignQuery mainQuery = (JRDesignQuery) jDesign.getQuery();
                        mainQuery.setText(queries.get(dataset));
                        jDesign.setQuery(mainQuery);
                    } else {
                        JRDesignDataset jDataset = (JRDesignDataset) jDesign.getDatasetMap().get(dataset);
                        if (jDataset == null) return;
                        JRDesignQuery query = (JRDesignQuery) jDataset.getQuery();
                        query.setText(queries.get(dataset));
                    }
                });

                JasperReport jReport = JasperCompileManager.compileReport(jDesign);
                JasperPrint jPrint = JasperFillManager.fillReport(jReport, parameters, SpendBConnection.getConnection());
                if (jPrint.getPages().isEmpty()) {
                    Platform.runLater(() -> {
                        MainEvents.showDialogMessage("Unexpected Error", "There is an error generating your report. Your document might be empty and cannot be generated since one or more query resulted to an empty ResultSet", "Dismiss");
                    });
                    return;
                };
                JasperExportManager.exportReportToPdfFile(jPrint, outputPath);
                if (setting.isViewPDF()) Desktop.getDesktop().open(new File(outputPath));
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> {
                    MainEvents.showDialogMessage("Unexpected Error", "There is an error generating your report. Your document might be empty and cannot be generated since one or more query resulted to an empty ResultSet", "Dismiss");
                });
            }
        });
        thread.start();

    }

    /**
     * Analyzes and sets a value in the PreparedStatement based on the specified data type.
     *
     * @param stmt     The PreparedStatement to set the value on.
     * @param value    The value to be set.
     * @param index    The index of the parameter in the PreparedStatement.
     * @param dataType The data type of the value.
     * @throws SQLException If an SQL error occurs while setting the value.
     */
    public static void analyzeValue(PreparedStatement stmt, Object value, int index, String dataType) throws SQLException {
        switch (dataType.toUpperCase()) {
            case "INT" -> stmt.setInt(index, (Integer) value);
            case "DOUBLE" -> stmt.setDouble(index, Double.parseDouble(value.toString()));
            case "TIMESTAMP" -> stmt.setTimestamp(index, (Timestamp) value);
            case "DATE" -> stmt.setDate(index, DateUtils.parseDate("3", (String) value));
            case "BIT" -> stmt.setBoolean(index, (Integer) value == 1);
            case "LONGBLOB" -> {
                File imageFile = ProgramUtils.storeImage((String) value, true);
                if (imageFile == null) stmt.setBinaryStream(index, null);
                try {
                    assert imageFile != null;
                    stmt.setBinaryStream(index, new FileInputStream(imageFile), (int) imageFile.length());
                } catch (Exception e) {
                    stmt.setBinaryStream(index, null);
                }

            }
            default -> stmt.setString(index, value.toString());
        }
    }

}
