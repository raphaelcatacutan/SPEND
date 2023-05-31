package com.ssg._test;

import com.ssg.utils.ProgramUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class WriteData {
    public static void writeToFile(String fileName, String content) {
        String filePath = Paths.get(ProgramUtils.SPENDTEMP, fileName).toString();

        File spendFolder = new File(ProgramUtils.SPENDTEMP);
        if (!spendFolder.exists()) {
            boolean created = spendFolder.mkdirs();
            if (!created) {
                System.err.println("Failed to create the 'Spend' folder.");
                return;
            }
        }

        try (FileWriter fileWriter = new FileWriter(filePath)) {
            fileWriter.write(content);
            System.out.println("File written successfully.");
        } catch (IOException e) {
            System.err.println("An error occurred while writing the file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        String fileName = "test.txt";
        String content = "Hello World";
        writeToFile(fileName, content);
    }
}
