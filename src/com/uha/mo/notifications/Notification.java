package com.uha.mo.notifications;

import com.uha.mo.model.Account;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.awt.*;
import java.io.IOException;

/**
 * Created by othman on 16/01/2017.
 */
public class Notification extends Stage {

    private Account account;
    private int count;
    private Stage mainStage;
    private Stage refForAnimation;
    private NotificationController controller;

    public static int counter = 1;

    public Notification(Stage mainStage, Account account, int count) {
        this.account = account;
        this.count = count;
        this.mainStage = mainStage;

        this.initStyle(StageStyle.TRANSPARENT);
        this.setAlwaysOnTop(true);
        this.setX(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getWidth() - 360);
        this.setY(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getHeight() - 115*counter);

        initRootLayout();
        start();
        show();
        this.refForAnimation = this;
    }

    private void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("notification.fxml"));
            VBox vBox = loader.load();

            this.controller = loader.getController();
            this.controller.config(mainStage, this, account, count);

            this.setScene(new Scene(vBox));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void start() {

        counter ++;

        FadeTransition fadeIn = new FadeTransition(Duration.millis(2000));
        fadeIn.setNode(controller.getMainRoot());
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.setCycleCount(1);
        fadeIn.setAutoReverse(false);

        PauseTransition pause = new PauseTransition(Duration.millis(5000));
        pause.setOnFinished(event -> {
            refForAnimation.close();
            if (counter > 1)
            counter --;
        });
        fadeIn.setOnFinished(event -> pause.play());
        fadeIn.playFromStart();
    }
}
