package com.ssg.database;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class XamppServer {
    private static String xamppPath = "C:\\xampp";
    public static void manage(boolean start) {
        System.out.println(start ? "Starting XAMPP...": "Exiting XAMPP...");
        if (!isXamppInstalled()) selectXamppDirectory();

        String xamppStartPath = xamppPath + "\\xampp_start.exe";
        String xamppStopPath = xamppPath + "\\xampp_stop.exe";

        try {
            Process process;
            if (start)  process = Runtime.getRuntime().exec(new String[] { xamppStartPath });
            else process = Runtime.getRuntime().exec(new String[] { xamppStopPath });
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static boolean isXamppInstalled() {
        File xamppDir = new File(xamppPath);
        return xamppDir.exists() && xamppDir.isDirectory();
    }

    private static void selectXamppDirectory() {
        JOptionPane.showMessageDialog(null, "XAMPP installation directory not found.");
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int option = chooser.showOpenDialog(null);
        if (option != JFileChooser.APPROVE_OPTION) return;
        Path path = Paths.get(chooser.getSelectedFile().getAbsolutePath());
        xamppPath = path.toString();
        JOptionPane.showMessageDialog(null, "Selected directory: " + xamppPath);
    }
}