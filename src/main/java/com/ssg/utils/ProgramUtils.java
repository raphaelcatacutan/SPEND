package com.ssg.utils;

import com.ssg.MainActivity;
import com.ssg.database.SpendBUtils;
import com.ssg.views.ControllerUtils;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;
import java.nio.file.Paths;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

public class ProgramUtils {
    public static String USERHOME = System.getProperty("user.home");
    public static String USERROAM = Paths.get(USERHOME, "AppData", "Roaming").toString();
    public static String USERDOCS = Paths.get(USERHOME, "Documents").toString();
    public static String SPENDTEMP = Paths.get(USERROAM, "Spend", "temp").toString();
    public static String SPENDDATA = Paths.get(USERROAM, "Spend", "appdata").toString();
    public static String CONFIGFILE = SPENDDATA + "/config.json";

    public static void main(String[] args) {
        System.out.println(readConfig("startXAMPP"));
    }

    /**
     * Gets a property from a property file
     *
     * @param propertyFile The property file to read
     * @param property     The property to access
     * @return The property value or empty string
     */
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

    /**
     * Gets a property from `configs.properties`
     *
     * @param property The property to access
     * @return The property value or empty string
     */
    public static String getProperty(String property) {
        return getProperty("configs", property);
    }

    public static void separator(int length) {
        for (int i = 0; i < length; i++) System.out.print("-");
        System.out.println();
    }


    /**
     * Parse the name parts from a full anme
     *
     * @param fullName The fullname
     * @return The array of parsed name parts {FirstName, MiddleInitial, LastName}
     */
    public static String[] parseName(String fullName) {
        String[] nameParts = fullName.split(" ");
        StringBuilder firstName = new StringBuilder();
        StringBuilder middleInitial = new StringBuilder();
        StringBuilder lastName = new StringBuilder();

        int addTo = (fullName.contains(",")) ? 0 : 1;
        for (String x : nameParts) {
            if (x.matches("[A-Z](\\.|\\.[A-Z]\\.)?")) addTo = 2;
            if (addTo == 0) { // 0 Last Name
                boolean hasComma = x.contains(",");
                lastName.append(hasComma ? x.substring(0, x.length() - 1) : x + " ");
                if (hasComma) addTo = 1;
            } else if (addTo == 1) { // 1 First Name
                firstName.append(x).append(" ");
            } else { // 2 Middle Initial
                middleInitial.append(x);
                addTo = 0;
            }
        }
        return new String[]{firstName.toString(), middleInitial.toString(), lastName.toString()};
    }

    public static Double parseDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Integer parseInt(String value) {
        try {
            value = value.split("\\.")[0];
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static void print(int type, Object... args) {
        String dateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS").format(LocalDateTime.now());
        String printType = switch (type) {
            case 2 -> "ERROR";
            case 3 -> "TEST";
            default -> "LOG";
        };
        String firstPrint = dateTime + " | [" + printType + "]";
        if (args.length == 1) System.out.print(firstPrint + " | ");
        else System.out.println(firstPrint);
        for (Object arg : args) System.out.println(arg);
        if (args.length > 1) ProgramUtils.separator(30);
    }

    public static void callDelay(double delay, MethodArgument method) {
        KeyFrame loadResource = new KeyFrame(Duration.seconds(delay), a -> Platform.runLater(method::callMethod));
        new Timeline(loadResource).play();
    }

    public static int stringMatch(String input, String pattern) {
        String regex = pattern.replace("%", ".*").replace("_", ".");
        Pattern patternObj = Pattern.compile(regex);
        if (input.equals(pattern)) return 1;
        else if (input.startsWith(pattern) && patternObj.matcher(input).matches()) return 2;
        else if (patternObj.matcher(input).matches()) return 3;
        else return 4;
    }

    public static int lowestNumber(int... numbers) {
        if (numbers.length == 0) throw new IllegalArgumentException("The array is empty.");
        int lowest = numbers[0];
        for (int i = 1; i < numbers.length; i++) if (numbers[i] < lowest) lowest = numbers[i];
        return lowest;
    }

    /**
     * Abbreviate a number
     *
     * @param number The number
     * @return The string of shorter representation for the number
     * @author hapiya1
     * @author andreaestudillo
     */
    public static String shortenNumber(double number) {
        String[] suffixes = {"", "K", "M", "B", "T"};
        double absoluteNumber = Math.abs(number);
        int index = 0;
        while (absoluteNumber >= 1000 && index < suffixes.length - 1) {
            absoluteNumber /= 1000;
            index++;
        }
        return String.format("%.0f%s", absoluteNumber, suffixes[index]);
    }

    /**
     * Abbreviate a number
     *
     * @param number The number
     * @return The string of shorter representation for the number
     * @author hapiya1
     * @author andreaestudillo
     */
    public static String shortenNumber(int number) {
        String[] suffixes = {"", "K", "M", "B", "T"};
        int index = 0;
        double num = number; // Convert to double for division
        while (num >= 1000 && index < suffixes.length - 1) {
            num /= 1000;
            index++;
        }
        return String.format("%.0f%s", num, suffixes[index]);
    }

    public static String currentTimeStampFileName() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH.mm.ss");
        return currentDateTime.format(formatter);
    }

    /**
     * Formats a double in currency
     *
     * @param amount The currency
     * @author CJ Belen
     */
    public static String formatCurrency(Double amount) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(',');
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00", symbols);
        return "₱" + decimalFormat.format(amount);
    }

    /**
     * Creates a file chooser dialog
     *
     * @param stage      The stage
     * @param title      The title
     * @param extensions The extension filters
     * @return The absolute path of the file
     */
    public static String chooseFile(Stage stage, String title, String... extensions) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        String[] modifiedExtensions = Arrays.stream(extensions)
                .map(extension -> "*." + extension)
                .toArray(String[]::new);
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("File Extensions", modifiedExtensions);
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) return file.getAbsolutePath();
        else return null; // No file selected
    }

    /**
     * Creates a file chooser dialog
     *
     * @param stage The stage
     * @param title The title
     * @return The absolute path of the file
     */
    public static String chooseDirectory(Stage stage, String title) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(title);
        File directory = directoryChooser.showDialog(stage);
        if (directory != null) return directory.getAbsolutePath();
        else return null; // No directory selected
    }

    /**
     * Stores an image in a temporary directory for database blob
     *
     * @param initialPath The initial path of the image
     * @param isAvatar    Whether the image will be cropped circular
     * @return The output file
     * @author Stephx01
     */
    public static File storeImage(String initialPath, boolean isAvatar) {
        try {
            if (initialPath == null) return null;
            File imageFile = new File(initialPath);
            BufferedImage image = ImageIO.read(imageFile);
            BufferedImage finalImage;
            if (isAvatar) {
                int size = Math.min(image.getWidth(), image.getHeight());
                finalImage = image.getSubimage(0, 0, size, size);
                BufferedImage circularImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = circularImage.createGraphics();
                g.setClip(new Ellipse2D.Float(0, 0, size, size));
                g.drawImage(finalImage, 0, 0, null);
                g.dispose();
                finalImage = circularImage;
            } else finalImage = image;

            File spendFolder = new File(ProgramUtils.SPENDTEMP);
            if (!spendFolder.exists()) {
                boolean created = spendFolder.mkdirs();
                if (!created) return null;
            }
            String fileName = UUID.randomUUID().toString();
            String filePath = Paths.get(ProgramUtils.SPENDTEMP, fileName).toString();
            File outputFile = new File(filePath);
            ImageIO.write(finalImage, "png", outputFile);
            return outputFile;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void browseURL(String link) {
        try {
            Desktop desktop = java.awt.Desktop.getDesktop();
            URI uri = new URI(link);
            desktop.browse(uri);
        } catch (Exception e) {
            ProgramUtils.print(2, e.getMessage());
        }
    }

    /**
     * Updates the Configuration Settings
     *
     * @param key   The key settings
     * @param value The settings value
     */
    public static void updateConfig(String key, Object value) {
        File jsonFile = new File(CONFIGFILE);
        if (!jsonFile.exists()) return;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(jsonFile));
            String line;
            StringBuilder jsonContent = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                jsonContent.append(line);
            }
            reader.close();
            String jsonString = jsonContent.toString();
            jsonString = jsonString.trim().substring(1, jsonString.length() - 1);
            String[] keyValuePairs = jsonString.split(",");
            StringBuilder updatedJsonContent = new StringBuilder();
            boolean isKeyFound = false;
            for (String pair : keyValuePairs) {
                String[] entry = pair.split(":");
                String currentKey = entry[0].trim().replace("\"", "");
                String currentValueString = String.join(":", Arrays.copyOfRange(entry, 1, entry.length));
                Object currentValue;
                if (currentKey.equals(key)) {
                    currentValue = value;
                    isKeyFound = true;
                } else if (currentValueString.startsWith("\"") && currentValueString.endsWith("\"")) {
                    currentValue = currentValueString.substring(1, currentValueString.length() - 1);
                } else if (currentValueString.equalsIgnoreCase("true") || currentValueString.equalsIgnoreCase("false")) {
                    currentValue = Boolean.parseBoolean(currentValueString);
                } else if (currentValueString.contains(".")) {
                    currentValue = Double.parseDouble(currentValueString);
                } else {
                    currentValue = Integer.parseInt(currentValueString);
                }
                updatedJsonContent.append("\"").append(currentKey).append("\":");
                if (currentValue instanceof String) {
                    updatedJsonContent.append("\"").append(currentValue).append("\"");
                } else {
                    updatedJsonContent.append(currentValue);
                }
                updatedJsonContent.append(",");
            }
            if (!isKeyFound) {
                updatedJsonContent.append("\"").append(key).append("\":");
                if (value instanceof String) {
                    updatedJsonContent.append("\"").append(value).append("\"");
                } else {
                    updatedJsonContent.append(value);
                }
                updatedJsonContent.append(",");
            }
            if (updatedJsonContent.charAt(updatedJsonContent.length() - 1) == ',') {
                updatedJsonContent.deleteCharAt(updatedJsonContent.length() - 1);
            }
            String updatedJsonString = "{" + updatedJsonContent.toString() + "}";
            BufferedWriter writer = new BufferedWriter(new FileWriter(jsonFile));
            writer.write(updatedJsonString);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Object readConfig(String key) {
        Object value = null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(CONFIGFILE));
            String line;
            StringBuilder jsonContent = new StringBuilder();
            while ((line = reader.readLine()) != null) jsonContent.append(line);
            reader.close();
            String jsonString = jsonContent.toString();
            jsonString = jsonString.trim().substring(1, jsonString.length() - 1);
            String[] keyValuePairs = jsonString.split(",");
            for (String pair : keyValuePairs) {
                String[] entry = pair.split(":");
                String currentKey = entry[0].trim().replace("\"", "");
                if (!currentKey.equals(key)) continue;
                String valueString = String.join(":", Arrays.copyOfRange(entry, 1, entry.length)).trim();
                if (valueString.startsWith("\"") && valueString.endsWith("\"")) value = valueString.substring(1, valueString.length() - 1);
                else if (valueString.equalsIgnoreCase("true") || valueString.equalsIgnoreCase("false")) value = Boolean.parseBoolean(valueString);
                else if (valueString.contains(".")) value = Double.parseDouble(valueString);
                else value = Integer.parseInt(valueString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return value;
    }


    public static void showDialogMessage(String title, String content) {
        try {
            // Set the look and feel to the system default
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            JOptionPane.showMessageDialog(
                    null,
                    content,
                    title,
                    JOptionPane.INFORMATION_MESSAGE
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
