package com.ssg.database;

import com.ssg.utils.RuntimeData;
import com.ssg.views.MainEvents;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class XamppServer {
    private static String xamppPath = "C:\\xampp";

    public static void manage(boolean start) {
        if (!isXamppInstalled()) xamppPath = RuntimeData.XAMPPLOCATION;

        String xamppStartPath = xamppPath + "\\xampp_start.exe";
        String xamppStopPath = xamppPath + "\\xampp_stop.exe";

        try {
            Process process;
            if (start) process = Runtime.getRuntime().exec(new String[] { xamppStartPath });
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

}
