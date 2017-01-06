package com.uha.mo.view;

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
 * Created by othman on 24/12/2016.
 */
public class EditYahooAccountController implements Initializable {

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
    @FXML
    private TextField name;
    @FXML
    private ComboBox<String> period;
    @FXML
    private CheckBox notifications;
    @FXML
    private Button delete;

    private YahooAccount account;
    private SettingsController parent;

    private boolean emailOK = false;
    private boolean passwordOK = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.error_email.setVisible(false);
        this.loading.setVisible(false);

        email.textProperty().addListener((observable, oldValue, newValue) -> {

            if(email.getText().matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "yahoo.com") || email.getText().matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "yahoo.fr")){
                email.setStyle("-fx-text-box-border: green ; -fx-focus-color: green ;");
                error_email.setVisible(false);
                emailOK = true;
            }
            else {
                email.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
                error_email.setVisible(true);
                emailOK = false;
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
        delete.setOnAction(event -> onDelete());
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
        new EditYahooAccountController.LoginChecker().execute(email, password);
    }

    private void onDelete() {
        ModelManager.getInstance().deleteAccount(this.account);
        parent.notifyEvent("delete");
    }

    public void setParent(SettingsController parent) {
        this.parent = parent;
    }

    public void setAccount(YahooAccount account) {
        this.account = account;

        name.setText(this.account.getName());
        email.setText(this.account.getMailAddress());
        password.setText(this.account.getPassword());
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

        if(this.account.isNotifications())
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
                store.connect(YahooAccount.IMAP_HOST, params[0], params[1]);
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
                YahooAccount newAccount = new YahooAccount(email.getText(), password.getText());
                newAccount.setName(name.getText());
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
