package com.ssg.database;

import com.ssg.MainActivity;
import com.ssg.utils.ProgramUtils;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SpendBUtils {
    private static final HashMap<String, Boolean> SPENDBCHANGES = new HashMap<>();
    public static final String[] SPENDBTABLES = {"OFFICERS", "PROJECTS", "CONTRIBUTORS", "EXPENSES", "SETTINGS", "USERS"};

    public static final String[] EMPTY = {};
    static {
        spendBUpdate(true);
    }

    public static void spendBUpdate(boolean state, String... table) {
        if (table.length == 0) table = SPENDBTABLES;
        for (String x: table) SPENDBCHANGES.put(x, state);
    }

    public static boolean spendBHasChanges(String... table) {
        if (table.length == 0) table = SPENDBTABLES;
        return Arrays.stream(table).anyMatch(SPENDBCHANGES::get);
    }

    public static boolean generateReport(int template, String query, boolean view, Object... parameters) {
        try {
            Map<String, Object> param = new HashMap<>();
            String JRXMLname = switch (template) {
                case 1 -> {
                    param.put("SCHOOL_YEAR", parameters[0]);
                    yield "expense-proposal";
                }
                case 2 -> "report-officer";
                case 3 -> "report-project";
                case 4-> "report-officers";
                case 5 -> "report-projects";
                default -> throw new IllegalStateException("Unexpected value: " + template);
            };
            String pathToJRXML = Objects.requireNonNull(MainActivity.class.getResource("reports/" + JRXMLname + ".jrxml")).toURI().getPath();
            String outputPath = Paths.get(ProgramUtils.USERHOME, "Downloads") + "\\output.pdf"; // TODO Change to settings
            JasperDesign jdesign = JRXmlLoader.load(pathToJRXML);

            // Setting query
//            JRDesignQuery updateQuery = new JRDesignQuery();
//            updateQuery.setText(query);
//            jdesign.setQuery(updateQuery);

            JasperReport jreport = JasperCompileManager.compileReport(jdesign);
            JasperPrint jprint = JasperFillManager.fillReport(jreport, param, SpendBConnection.getConnection());

            if (view) JasperViewer.viewReport(jprint);
            JasperExportManager.exportReportToPdfFile(jprint, outputPath);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public static void analyzeValue(PreparedStatement stmt, Object value, int index, String dataType) throws SQLException, FileNotFoundException {
        switch (dataType.toUpperCase()) {
            case "INT" -> stmt.setInt(index, (Integer) value);
            case "DOUBLE" -> stmt.setDouble(index, Double.parseDouble(value.toString()));
            case "TIMESTAMP" -> stmt.setTimestamp(index, (Timestamp) value);
            case "DATE" -> stmt.setDate(index, ProgramUtils.parseDate("3", (String) value));
            case "BIT" -> stmt.setBoolean(index, (Integer) value == 1);
            case "LONGBLOB" -> {
                File imageFile = new File((String) value);
                stmt.setBinaryStream(index, new FileInputStream(imageFile), (int) imageFile.length());
            }
            default -> stmt.setString(index, value.toString());
        }
    }
}
