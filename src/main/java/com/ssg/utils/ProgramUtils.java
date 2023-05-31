package com.ssg.utils;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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

public class ProgramUtils {
    public static String USERHOME = System.getProperty("user.home");
    public static String SPENDDIR = Paths.get(USERHOME, "AppData", "Roaming").toString();
    public static String SPENDTEMP = Paths.get(SPENDDIR, "Spend", "temp").toString();
    public static String SPENDDATA = Paths.get(SPENDDIR, "Spend", "appdata").toString();

    /**
     * Gets a property from a property file
     * @param propertyFile The property file to read
     * @param property The property to access
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
     * @param property The property to access
     * @return The property value or empty string
     */
    public static String getProperty(String property) {
        return getProperty("configs", property);
    }
    public static void separator(int length) {
        for (int i = 0; i < length; i++)  System.out.print("-");
        System.out.println();
    }
    public static ArrayList<Date> getLastMonths(int monthNumbers) {
        Calendar cal = Calendar.getInstance();
        Date now = new Date(cal.getTimeInMillis());
        ArrayList<Date> lastMonths = new ArrayList<>();
        for (int i = 0; i < monthNumbers - 1; i++) {
            cal.add(Calendar.MONTH, -1);
            cal.set(Calendar.DATE, 1);
            Date date = new Date(cal.getTimeInMillis());
            lastMonths.add(date);
        }
        Collections.reverse(lastMonths);
        lastMonths.add(now);
        return lastMonths;
    }

    /**
     * Converts a Date to String
     * @param format The string format. Use Quick Formats by passing numbers
     * @param date The date to format
     * @return The string format of the date
     */
    public static String formatDate(String format, Date date) {
        format = switch (format) {
            // Quick Formats
            case "1" -> "yyyy-MMM-dd";
            case "2" -> "MMMM dd, yyyy";
            case "3" -> "yyyy-MM-dd";
            case "4" -> "yyyy-MM-dd HH:mm:ss";
            default -> format;
        };
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        LocalDate localDate = date.toLocalDate();
        return formatter.format(localDate);
    }
    /**
     * Converts a String to Date
     * @param format The string format. Use Quick Formats by passing numbers
     * @param date The string to parse
     * @return The parsed date
     */
    public static Date parseDate(String format, String date) {
        try {
            format = switch (format) {
                // Quick Formats
                case "1" -> "yyyy-MMM-dd";
                case "2" -> "MMMM dd, yyyy";
                case "3" -> "yyyy-MM-dd";
                case "4" -> "yyyy-MM-dd HH:mm:ss";
                default -> format;
            };
            SimpleDateFormat formatter = new SimpleDateFormat(format);
            return new Date(formatter.parse(date).getTime());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Parse the name parts from a full anme
     * @param fullName The fullname
     * @return The array of parsed name parts {FirstName, MiddleInitial, LastName}
     */
    public static String[] parseName(String fullName) {
        String[] nameParts = fullName.split(" ");
        StringBuilder firstName = new StringBuilder();
        StringBuilder middleInitial = new StringBuilder();
        StringBuilder lastName = new StringBuilder();

        int addTo = (fullName.contains(",")) ? 0: 1;
        for (String x: nameParts) {
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
        return new String[] {firstName.toString(), middleInitial.toString(), lastName.toString()};
    }
    public static Date sqlDateNow() {
        return Date.valueOf(LocalDateTime.now().toLocalDate());
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
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    public static void print(int type, Object... args) {
        String dateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
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

    /**
     * Formats a double in currency
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
     * Stores an image in a temporary directory for database blob
     * @param stage
     * @param isAvatar If the image will be cropped
     * @return
     * @author Stephen Bugna
     */
    public static String storeImage(Stage stage, boolean isAvatar) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose an image file");
        File imageFile = fileChooser.showOpenDialog(stage);
        if (imageFile == null) return null;
        try {
            BufferedImage image = ImageIO.read(imageFile);
            BufferedImage finalImage;
            if (isAvatar) {
                // TODO Downscale to 1mb
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
            return outputFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
