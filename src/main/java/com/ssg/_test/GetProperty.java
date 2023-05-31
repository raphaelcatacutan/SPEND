package com.ssg._test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class GetProperty {
    public static void main(String[] args) {
        System.out.println(getProperty("configs", "adminPassword"));
    }
    public static String getProperty(String propertyFile, String property) {
        Properties prop = new Properties();
        String propertiesDir = "src/main/resources/com/ssg/properties/";
        String fileLoad = propertiesDir + propertyFile + ".properties";
        try {
            FileInputStream input = new FileInputStream(fileLoad);
            prop.load(input);
        } catch (IOException e) {
            System.out.println("Error loading property file: " + fileLoad);
            return "";
        }

        String value = prop.getProperty(property);
        return (value != null) ? value : "";
    }
}
