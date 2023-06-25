package com.ssg._test;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class DialogBoxExample {
    public static void main(String[] args) {
        try {
            // Set the look and feel to the system default
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            // Display a simple message dialog box
            JOptionPane.showMessageDialog(null, "Hello, World!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
