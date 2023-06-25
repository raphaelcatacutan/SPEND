package com.ssg._test;

import java.io.*;
import java.util.*;

public class TemplateHandler {
    public static void main(String[] args) {
        HashMap<String, Object> x = processTemplate();
        System.out.println(x.get("hello"));
        System.out.println(x.get("world"));
    }
    public static HashMap<String, Object> processTemplate() {
        // Step 1: Declare a HashMap 'template'
        HashMap<String, Object> template = new HashMap<>();
        template.put("hello", false);
        template.put("world", "raphael");
        template.put("ha", 1);

        // Step 2: Check if the JSON file exists
        File jsonFile = new File("template.json");
        if (jsonFile.exists()) {
            try {
                // Step 3: Read the JSON and update the 'template' HashMap
                BufferedReader reader = new BufferedReader(new FileReader(jsonFile));
                String line;
                StringBuilder jsonContent = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    jsonContent.append(line);
                }
                reader.close();

                // Replace the following code with your own logic to update the 'template' HashMap
                // This example assumes that the JSON file contains key-value pairs as a simple flat object
                // You might need to adjust it based on the structure of your JSON file
                String jsonString = jsonContent.toString();
                // Remove any leading/trailing whitespace and curly braces
                jsonString = jsonString.trim().substring(1, jsonString.length() - 1);
                String[] keyValuePairs = jsonString.split(",");
                for (String pair : keyValuePairs) {
                    String[] entry = pair.split(":");
                    String key = entry[0].trim().replace("\"", "");
                    String valueString = entry[1].trim();
                    Object value;
                    if (valueString.startsWith("\"") && valueString.endsWith("\"")) {
                        value = valueString.substring(1, valueString.length() - 1);
                    } else if (valueString.equalsIgnoreCase("true") || valueString.equalsIgnoreCase("false")) {
                        value = Boolean.parseBoolean(valueString);
                    } else if (valueString.contains(".")) {
                        value = Double.parseDouble(valueString);
                    } else {
                        value = Integer.parseInt(valueString);
                    }
                    template.put(key, value);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Step 4: Create a JSON using the 'template' HashMap
            try {
                // Replace the following code with your own logic to generate the JSON content from the 'template' HashMap
                // This example assumes that the 'template' HashMap contains key-value pairs as a simple flat object
                // You might need to adjust it based on the structure of your desired JSON output
                StringBuilder jsonContent = new StringBuilder();
                jsonContent.append("{");
                for (Map.Entry<String, Object> entry : template.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    jsonContent.append("\"").append(key).append("\":");
                    if (value instanceof String) {
                        jsonContent.append("\"").append(value).append("\"");
                    } else {
                        jsonContent.append(value);
                    }
                    jsonContent.append(",");
                }
                if (jsonContent.charAt(jsonContent.length() - 1) == ',') {
                    jsonContent.deleteCharAt(jsonContent.length() - 1);
                }
                jsonContent.append("}");

                // Write the JSON content to a new file
                BufferedWriter writer = new BufferedWriter(new FileWriter(jsonFile));
                writer.write(jsonContent.toString());
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Step 5: Return the new 'template' HashMap
        return template;
    }
}
