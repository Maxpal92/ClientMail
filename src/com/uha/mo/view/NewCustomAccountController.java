package com.uha.mo.view;

import com.uha.mo.App;
import com.uha.mo.model.CustomAccount;
import com.uha.mo.utils.AsyncTask;
import com.uha.mo.model.ModelManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javax.mail.Session;
import javax.mail.Store;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
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
    @FXML
    private Button valid;
    @FXML
    private TextField email;
    @FXML
    private PasswordField password;
    @FXML
    private ImageView loading;
    @FXML
    private StackPane root;
    @FXML
    private TextField smtpHost;
    @FXML
    private TextField smtpPort;
    @FXML
    private ComboBox getProtocol;
    @FXML
    private TextField getHost;
    @FXML
    private TextField getPort;

    private Stage stage;
    private App app;
    private double xOffset;
    private double yOffset;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.menuBarContainer.prefWidthProperty().bind(this.menuBar.widthProperty().subtract(20));
        this.root.prefWidthProperty().bind(this.menuBar.widthProperty());
        this.loading.setVisible(false);

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

            String getProtocolValue = (String) getProtocol.getSelectionModel().getSelectedItem();
            switch (getProtocolValue) {
                case "IMAP":
                    props.setProperty("mail.store.protocol", "imaps");
                    break;
                case "POP3":
                    props.setProperty("mail.store.protocol", "pop3");
                    break;
            }

            try {
                Session session = Session.getInstance(props, null);
                Store store = session.getStore();
                store.connect(getHost.getText(), params[0], params[1]);
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
                CustomAccount customAccount = new CustomAccount(email.getText(), password.getText());
                customAccount.setSMTP_HOST(smtpHost.getText());
                customAccount.setSMTP_PORT(smtpPort.getText());
                customAccount.setGET_PROTOCOL((String) getProtocol.getSelectionModel().getSelectedItem());
                customAccount.setGET_PROTOCOL_HOST(getHost.getText());
                customAccount.setGET_PROTOCOL_PORT(getPort.getText());

                ModelManager.getInstance().addAccount(customAccount);
                app.initRootLayout();
            }
            else {
                try {
                    new com.uha.mo.utils.Error(root,"Erreur lors de la connection au compte. Vérifiez les paramètres entrés.").show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
