package com.uha.mo.view;

import com.uha.mo.App;
import com.uha.mo.model.GmailAccount;
import com.uha.mo.model.YahooAccount;
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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.mail.Folder;
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
                try {
                    Document xml = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File("src/com/uha/mo/model/accounts.xml"));
                    Node root = xml.getDocumentElement();

                    Element newAccount = xml.createElement("Account");
                    newAccount.setAttribute("type", "custom");
                    newAccount.setAttribute("address", email.getText());
                    newAccount.setAttribute("password", password.getText());
                    newAccount.setAttribute("smtpHost", smtpHost.getText());
                    newAccount.setAttribute("smtpPort", smtpPort.getText());
                    newAccount.setAttribute("getProtocol", (String) getProtocol.getSelectionModel().getSelectedItem());
                    newAccount.setAttribute("getHost", getHost.getText());
                    newAccount.setAttribute("getPort", getPort.getText());

                    root.appendChild(newAccount);

                    Transformer tr = TransformerFactory.newInstance().newTransformer();
                    tr.setOutputProperty(OutputKeys.INDENT, "yes");
                    tr.setOutputProperty(OutputKeys.METHOD, "xml");
                    tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                    tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
                    tr.transform(new DOMSource(xml), new StreamResult(new FileOutputStream("src/com/uha/mo/model/accounts.xml")));

                    app.initRootLayout();

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
