package com.uha.mo.view;

import com.uha.mo.model.CustomAccount;
import com.uha.mo.utils.AsyncTask;
import com.uha.mo.utils.ModelManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.mail.Session;
import javax.mail.Store;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Created by othman on 20/12/2016.
 */
public class AddCustomAccountController implements Initializable {

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

    private SettingsController parent;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.loading.setVisible(false);
        this.valid.setOnAction(event -> onValid());
    }

    private void onValid() {
        String email = this.email.getText();
        String password = this.password.getText();

        this.valid.setVisible(false);
        this.loading.setVisible(true);
        new AddCustomAccountController.LoginChecker().execute(email, password);
    }

    public void setParent(SettingsController parent) {
        this.parent = parent;
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
                parent.notifyEvent("added");
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
