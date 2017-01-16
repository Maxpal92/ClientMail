package com.uha.mo.utils;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import org.w3c.dom.Node;

import java.io.IOException;

/**
 * Created by othman on 20/12/2016.
 */
public class Success {
    private Pane parent;
    private String message;

    public Success(Pane parent, String message) {
        this.parent = parent;
        this.message = message;
    }

    public void show() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("success.fxml"));
        Label label = loader.load();
        label.setText(this.message);

        this.parent.getChildren().add(label);

        label.prefWidthProperty().bind(parent.widthProperty());

        FadeTransition fadeIn = new FadeTransition(Duration.millis(2000));
        fadeIn.setNode(label);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.setCycleCount(1);
        fadeIn.setAutoReverse(false);

        PauseTransition pause = new PauseTransition(Duration.millis(2000));
        pause.setOnFinished(event -> {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(2000));
            fadeOut.setNode(label);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setCycleCount(1);
            fadeOut.setAutoReverse(false);
            fadeOut.playFromStart();
        });

        fadeIn.setOnFinished(event -> {
            pause.play();
        });

        fadeIn.playFromStart();
    }
}
