package com.uha.mo.view;

import com.uha.mo.App;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class NewGmailAccountController implements Initializable {

    @FXML
    private ToolBar menuBar;
    @FXML
    private ImageView exitButton;
    @FXML
    private ImageView backButton;
    @FXML
    private HBox menuBarContainer;
    @FXML
    private Button valid;
    @FXML
    private TextField email;
    @FXML
    private TextField password;
    @FXML
    private Label error_email;

    private Stage stage;
    private App app;
    private double xOffset;
    private double yOffset;

    private boolean emailOK = false;
    private boolean passwordOK = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.menuBarContainer.prefWidthProperty().bind(this.menuBar.widthProperty().subtract(20));
        this.error_email.setVisible(false);
        this.valid.setDisable(true);

        email.textProperty().addListener((observable, oldValue, newValue) -> {

            if(!email.getText().matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "gmail.com")){
                email.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
                error_email.setVisible(true);
                emailOK = false;
            }
            else {
                email.setStyle("-fx-text-box-border: green ; -fx-focus-color: green ;");
                error_email.setVisible(false);
                emailOK = true;
            }

            checkDisableValid();
        });

        password.textProperty().addListener((observable, oldValue, newValue) -> {
            if(password.getText().isEmpty()) {
                passwordOK = false;
            }
            else {
                passwordOK = true;
            }
            checkDisableValid();
        });

        valid.setOnAction(event -> onValid());

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

        valid.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //String email =
            }
        });
    }

    private void checkDisableValid() {
        if(emailOK && passwordOK) {
            valid.setDisable(false);
        }
        else {
            valid.setDisable(true);
        }
    }

    private void onValid() {

    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setApp(App app) {
        this.app = app;
    }
}
