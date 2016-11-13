package com.uha.mo.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;


public class NoAccountController implements Initializable{

    @FXML
    private ToolBar menuBar;
    @FXML
    private ImageView exitButton;
    @FXML
    private VBox gmail;
    @FXML
    private VBox yahoo;
    @FXML
    private VBox custom;

    private Stage stage;
    private double xOffset;
    private double yOffset;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        menuBar.setOnMousePressed(event -> {
            xOffset = stage.getX() - event.getScreenX();
            yOffset = stage.getY() - event.getScreenY();
        });

        menuBar.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() + xOffset);
            stage.setY(event.getScreenY() + yOffset);
        });

        /********************************* EXIT BUTTON *********************************/
        exitButton.setOnMouseClicked(event -> System.exit(0));
        exitButton.setOnMouseEntered(event -> exitButton.setImage(new Image("images/delete_hover.png")));
        exitButton.setOnMouseExited(event -> exitButton.setImage(new Image("images/delete.png")));

        gmail.setOnMouseEntered(event -> gmail.setStyle("-fx-background-color: #bdc3c7;"));
        gmail.setOnMouseExited(event -> gmail.setStyle("-fx-background-color: white;"));

        yahoo.setOnMouseEntered(event -> yahoo.setStyle("-fx-background-color: #bdc3c7;"));
        yahoo.setOnMouseExited(event -> yahoo.setStyle("-fx-background-color: white;"));

        custom.setOnMouseEntered(event -> custom.setStyle("-fx-background-color: #bdc3c7;"));
        custom.setOnMouseExited(event -> custom.setStyle("-fx-background-color: white;"));
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
