package com.ssg.views;

import com.ssg.MainActivity;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.util.Objects;

public class SplashScreen {

    @FXML private AnchorPane anpSplashScreen;
    @FXML private MediaView mvwLogoMedia;

    public void initialize() {
        mvwLogoMedia.setFitWidth(anpSplashScreen.getPrefWidth());
        mvwLogoMedia.setFitHeight(anpSplashScreen.getPrefHeight());
        Media media = new Media(Objects.requireNonNull(MainActivity.class.getResource("assets/videos/SPLASH-SCREEN.mp4")).toExternalForm());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mvwLogoMedia.setMediaPlayer(mediaPlayer);
        mediaPlayer.play();
        mediaPlayer.setOnEndOfMedia(mediaPlayer::dispose);
    }

}
