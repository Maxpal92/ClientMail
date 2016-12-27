package com.uha.mo.view;

import com.uha.mo.utils.AsyncTask;
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
                try {
                    Document xml = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File("src/com/uha/mo/model/accounts.xml"));
                    Node rootXML = xml.getDocumentElement();

                    Element newAccount = xml.createElement("Account");
                    newAccount.setAttribute("type", "custom");
                    newAccount.setAttribute("address", email.getText());
                    newAccount.setAttribute("password", password.getText());
                    newAccount.setAttribute("smtpHost", smtpHost.getText());
                    newAccount.setAttribute("smtpPort", smtpPort.getText());
                    newAccount.setAttribute("getProtocol", (String) getProtocol.getSelectionModel().getSelectedItem());
                    newAccount.setAttribute("getHost", getHost.getText());
                    newAccount.setAttribute("getPort", getPort.getText());

                    rootXML.appendChild(newAccount);

                    Transformer tr = TransformerFactory.newInstance().newTransformer();
                    tr.setOutputProperty(OutputKeys.INDENT, "yes");
                    tr.setOutputProperty(OutputKeys.METHOD, "xml");
                    tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                    tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
                    tr.transform(new DOMSource(xml), new StreamResult(new FileOutputStream("src/com/uha/mo/model/accounts.xml")));

                    new com.uha.mo.utils.Success(root, "Le compte a été ajouté avec succès.").show();

                    parent.notifyEvent();

                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (TransformerConfigurationException e) {
                    e.printStackTrace();
                } catch (TransformerException e) {
                    e.printStackTrace();
                }
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
