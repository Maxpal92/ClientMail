package com.uha.mo.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToolBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;


public class NewAccountController implements Initializable {

    @FXML
    private ToolBar menuBar;
    @FXML
    private ImageView exitButton;
    @FXML
    private HBox menuBarContainer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.menuBarContainer.prefWidthProperty().bind(this.menuBar.widthProperty());
    }
}
