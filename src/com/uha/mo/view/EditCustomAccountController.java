package com.uha.mo.view;

import com.uha.mo.model.CustomAccount;
import com.uha.mo.model.GmailAccount;
import com.uha.mo.model.YahooAccount;
import com.uha.mo.utils.AsyncTask;
import com.uha.mo.utils.ModelManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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
 * Created by othman on 25/12/2016.
 */
public class EditCustomAccountController implements Initializable {

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
    @FXML
    private TextField name;
    @FXML
    private ComboBox<String> period;
    @FXML
    private CheckBox notifications;
    @FXML
    private Button delete;

    private CustomAccount account;
    private SettingsController parent;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.loading.setVisible(false);
        this.valid.setOnAction(event -> onValid());
        this.delete.setOnAction(event -> onDelete());
    }

    private void onValid() {
        String email = this.email.getText();
        String password = this.password.getText();

        this.valid.setVisible(false);
        this.loading.setVisible(true);
        new EditCustomAccountController.LoginChecker().execute(email, password);
    }

    private void onDelete() {
        ModelManager.getInstance().deleteAccount(this.account);
        parent.notifyEvent("delete");
    }

    public void setParent(SettingsController parent) {
        this.parent = parent;
    }

    public void setAccount(CustomAccount account) {
        this.account = account;

        name.setText(this.account.getName());
        email.setText(this.account.getMailAddress());
        password.setText(this.account.getPassword());

        smtpHost.setText(this.account.getSMTP_HOST());
        smtpPort.setText(this.account.getSMTP_PORT());

        if(this.account.getGET_PROTOCOL().equals("IMAP"))
            getProtocol.setValue("IMAP");
        else
            getProtocol.setValue("POP3");

        getHost.setText(this.account.getGET_PROTOCOL_HOST());
        getPort.setText(this.account.getGET_PROTOCOL_PORT());

        switch (this.account.getSyncPeriod()) {
            case 900000:
                period.setValue("Toutes les 15 minutes");
                break;
            case 1800000:
                period.setValue("Toutes les 30 minutes");
                break;
            case 3600000:
                period.setValue("Toutes les heures");
                break;
            case 7200000:
                period.setValue("Toutes les 2 heures");
                break;
        }

        if (this.account.isNotifications())
            notifications.setSelected(true);
        else
            notifications.setSelected(false);
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

            if (result) {
                CustomAccount newAccount = new CustomAccount(email.getText(), password.getText());
                newAccount.setName(name.getText());
                newAccount.setSMTP_HOST(smtpHost.getText());
                newAccount.setSMTP_PORT(smtpPort.getText());
                newAccount.setGET_PROTOCOL((String) getProtocol.getSelectionModel().getSelectedItem());
                newAccount.setGET_PROTOCOL_HOST(getHost.getText());
                newAccount.setGET_PROTOCOL_PORT(getPort.getText());

                switch (period.getSelectionModel().getSelectedItem()) {
                    case "Toutes les 15 minutes":
                        newAccount.setSyncPeriod(900000);
                        break;
                    case "Toutes les 30 minutes":
                        newAccount.setSyncPeriod(1800000);
                        break;
                    case "Toutes les heures":
                        newAccount.setSyncPeriod(3600000);
                        break;
                    case "Toutes les 2 heures":
                        newAccount.setSyncPeriod(7200000);
                        break;
                }
                if(notifications.isSelected())
                    newAccount.setNotifications(true);
                else
                    newAccount.setNotifications(false);

                ModelManager.getInstance().editAccount(account, newAccount);
                parent.notifyEvent("edited");

            } else {
                try {
                    new com.uha.mo.utils.Error(root, "Erreur lors de la connection au compte. Vérifiez vos identifiant et mot de passe.").show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

