package com.uha.mo.view;


import com.uha.mo.App;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class NewCustomAccountController implements Initializable {
    @FXML
    private ToolBar menuBar;
    @FXML
    private ImageView exitButton;
    @FXML
    private ImageView backButton;
    @FXML
    private HBox menuBarContainer;

    private Stage stage;
    private App app;
    private double xOffset;
    private double yOffset;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.menuBarContainer.prefWidthProperty().bind(this.menuBar.widthProperty().subtract(20));

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

        /********************************* BACK BUTTON *********************************/
        backButton.setOnMouseClicked(event -> {
            app.initRootLayout();
        });
        backButton.setOnMouseEntered(event -> backButton.setImage(new Image("images/back_hover.png")));
        backButton.setOnMouseExited(event -> backButton.setImage(new Image("images/back.png")));


    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setApp(App app) {
        this.app = app;
    }
}
