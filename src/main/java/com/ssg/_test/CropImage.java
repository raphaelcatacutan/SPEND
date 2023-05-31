package com.ssg._test;

import com.ssg.utils.ProgramUtils;
import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

public class CropImage extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    public static String storeImage(Stage stage, boolean isCircular) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose an image file");
        File imageFile = fileChooser.showOpenDialog(stage);
        if (imageFile == null) return null;
        try {
            BufferedImage image = ImageIO.read(imageFile);
            BufferedImage finalImage;
            if (isCircular) {
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


    @Override
    public void start(Stage stage) throws Exception {
        String filePath = storeImage(stage, true);
        System.out.println("File path: " + filePath);
        stage.close();
    }
}
