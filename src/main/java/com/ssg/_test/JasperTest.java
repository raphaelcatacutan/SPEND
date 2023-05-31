package com.ssg._test;

import com.ssg.database.SpendBConnection;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

public class JasperTest {
    public static void generateReport(String path, String query) {
        try {
            String pathToJRXML = switch (path) {
                case "1" -> "";
                default -> path;
            };
            String outputPath = "";
            JasperDesign jdesign = JRXmlLoader.load(pathToJRXML);
            JRDesignQuery updateQuery = new JRDesignQuery();
            updateQuery.setText(query);
            jdesign.setQuery(updateQuery);
            JasperReport jreport = JasperCompileManager.compileReport(jdesign);
            JasperPrint jprint = JasperFillManager.fillReport(jreport, null, SpendBConnection.getConnection());

            JasperViewer.viewReport(jprint); // What this mean
            JasperExportManager.exportReportToPdfFile(jprint, outputPath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
