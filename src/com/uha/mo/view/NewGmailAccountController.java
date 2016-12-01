package com.uha.mo.view;

import com.uha.mo.App;
import com.uha.mo.model.GmailAccount;
import com.uha.mo.utils.AsyncTask;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.mail.Folder;
import javax.mail.Session;
import javax.mail.Store;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
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
    private PasswordField password;
    @FXML
    private Label error_email;
    @FXML
    private ImageView loading;
    @FXML
    private StackPane root;

    private Stage stage;
    private App app;
    private double xOffset;
    private double yOffset;

    private boolean emailOK = false;
    private boolean passwordOK = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.menuBarContainer.prefWidthProperty().bind(this.menuBar.widthProperty().subtract(20));
        this.root.prefWidthProperty().bind(this.menuBar.widthProperty());
        this.error_email.setVisible(false);
        this.valid.setDisable(true);
        this.loading.setVisible(false);

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
        String email = this.email.getText();
        String password = this.password.getText();

        this.valid.setVisible(false);
        this.loading.setVisible(true);
        new LoginChecker().execute(email, password);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setApp(App app) {
        this.app = app;
    }

    private class LoginChecker extends AsyncTask<String, Boolean> {

        @Override
        protected Boolean doInBackground() {
            Properties props = new Properties();
            props.setProperty("mail.store.protocol", "imaps");
            try {
                Session session = Session.getInstance(props, null);
                Store store = session.getStore();
                store.connect(GmailAccount.IMAP_HOST, params[0], params[1]);
                store.close();

                return true;

            } catch (Exception mex) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            valid.setVisible(true);
            loading.setVisible(false);

            if(result) {

            }
            else {
                try {
                    new com.uha.mo.utils.Error(root,"Erreur lors de la connection au compte. Vérifiez vos identifiant et mot de passe.").show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
