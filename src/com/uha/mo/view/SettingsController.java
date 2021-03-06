package com.uha.mo.view;

import com.uha.mo.App;
import com.uha.mo.model.*;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
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


public class SettingsController implements Initializable {

    @FXML
    private ToolBar menuBar;
    @FXML
    private ImageView exitButton;
    @FXML
    private ImageView backButton;
    @FXML
    private HBox menuBarContainer;

    @FXML
    private VBox accounts;
    @FXML
    private HBox gmail;
    @FXML
    private HBox yahoo;
    @FXML
    private HBox custom;
    @FXML
    private StackPane root;

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
        exitButton.setOnMouseClicked(event -> stage.hide());
        exitButton.setOnMouseEntered(event -> exitButton.setImage(new Image("images/delete_hover.png")));
        exitButton.setOnMouseExited(event -> exitButton.setImage(new Image("images/delete.png")));

        /********************************* BACK BUTTON *********************************/
        backButton.setOnMouseClicked(event -> {
            app.initRootLayout();
        });
        backButton.setOnMouseEntered(event -> backButton.setImage(new Image("images/back_hover.png")));
        backButton.setOnMouseExited(event -> backButton.setImage(new Image("images/back.png")));

        gmail.setOnMouseClicked(event -> this.addAccount("gmail"));
        gmail.setOnMouseEntered(event -> gmail.setStyle("-fx-background-color: rgba(189, 195, 199,1.0);"));
        gmail.setOnMouseExited(event -> gmail.setStyle("-fx-background-color: rgba(236, 240, 241,1.0);"));

        yahoo.setOnMouseClicked(event -> this.addAccount("yahoo"));
        yahoo.setOnMouseEntered(event -> yahoo.setStyle("-fx-background-color: rgba(189, 195, 199,1.0);"));
        yahoo.setOnMouseExited(event -> yahoo.setStyle("-fx-background-color: rgba(236, 240, 241,1.0);"));

        custom.setOnMouseClicked(event -> this.addAccount("custom"));
        custom.setOnMouseEntered(event -> custom.setStyle("-fx-background-color: rgba(189, 195, 199,1.0);"));
        custom.setOnMouseExited(event -> custom.setStyle("-fx-background-color: rgba(236, 240, 241,1.0);"));
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setApp(App app) {
        this.app = app;
        setUpMenu();
    }

    private void setUpMenu() {

        this.accounts.getChildren().clear();

        for(Account account : this.app.getModel().getAccounts()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("accountMenuItem.fxml"));
                HBox root = loader.load();

                root.setOnMouseEntered(event -> root.setStyle("-fx-background-color: rgba(189, 195, 199,1.0);"));
                root.setOnMouseExited(event -> root.setStyle("-fx-background-color: rgba(236, 240, 241,1.0);"));

                root.setOnMouseClicked(event -> this.editAccount(account));

                AccountMenuItemController controller = loader.getController();

                controller.setAccountName(account.nameProperty());
                controller.setAccountAddress(account.getMailAddress());

                if(account instanceof GmailAccount)
                    controller.setAccountIcon(new Image("images/gmail.png"));
                else if(account instanceof YahooAccount)
                    controller.setAccountIcon(new Image("images/yahoo.png"));
                else
                    controller.setAccountIcon(new Image("images/custom.png"));

                this.accounts.getChildren().add(root);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void editAccount(Account account) {
        root.getChildren().clear();

        try {
            if(account instanceof GmailAccount) {
                FXMLLoader gmailLoader = new FXMLLoader(getClass().getResource("editGmailAccount.fxml"));
                StackPane gmailRoot = gmailLoader.load();

                EditGmailAccountController gmailController = gmailLoader.getController();
                gmailController.setAccount((GmailAccount)account);
                gmailController.setParent(this);

                root.getChildren().add(gmailRoot);
            }
            else if(account instanceof YahooAccount) {
                FXMLLoader yahooLoader = new FXMLLoader(getClass().getResource("editYahooAccount.fxml"));
                StackPane yahooRoot = yahooLoader.load();

                EditYahooAccountController yahooController = yahooLoader.getController();
                yahooController.setAccount((YahooAccount)account);
                yahooController.setParent(this);

                root.getChildren().add(yahooRoot);
            }
            else if(account instanceof CustomAccount) {
                FXMLLoader customLoader = new FXMLLoader(getClass().getResource("editCustomAccount.fxml"));
                StackPane customRoot = customLoader.load();

                EditCustomAccountController customController = customLoader.getController();
                customController.setAccount((CustomAccount)account);
                customController.setParent(this);

                root.getChildren().add(customRoot);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addAccount(String type) {

        try {
            root.getChildren().clear();

            switch (type) {
                case "gmail":
                    FXMLLoader gmailLoader = new FXMLLoader(getClass().getResource("addGmailAccount.fxml"));
                    VBox gmailRoot = gmailLoader.load();

                    AddGmailAccountController gmailController = gmailLoader.getController();
                    gmailController.setParent(this);

                    gmailRoot.prefHeightProperty().bind(root.heightProperty());
                    gmailRoot.prefWidthProperty().bind(root.widthProperty().subtract(3));
                    root.getChildren().add(gmailRoot);
                    break;
                case "yahoo":
                    FXMLLoader yahooLoader = new FXMLLoader(getClass().getResource("addYahooAccount.fxml"));
                    VBox yahooRoot = yahooLoader.load();

                    AddYahooAccountController yahooController = yahooLoader.getController();
                    yahooController.setParent(this);

                    yahooRoot.prefHeightProperty().bind(root.heightProperty());
                    yahooRoot.prefWidthProperty().bind(root.widthProperty().subtract(3));
                    root.getChildren().add(yahooRoot);
                    break;
                case "custom":
                    FXMLLoader customLoader = new FXMLLoader(getClass().getResource("addCustomAccount.fxml"));
                    VBox customRoot = customLoader.load();

                    AddCustomAccountController customController = customLoader.getController();
                    customController.setParent(this);

                    customRoot.prefHeightProperty().bind(root.heightProperty());
                    customRoot.prefWidthProperty().bind(root.widthProperty().subtract(3));
                    root.getChildren().add(customRoot);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void notifyEvent() {
        app.stopCheck();
        app.refreshModel();
        app.startCheck();
        setUpMenu();
    }

    public void notifyEvent(String event) {
        try {
            if(event.equals("delete")) {
                root.getChildren().clear();
                notifyEvent();
                new com.uha.mo.utils.Success(root, "Le compte a été supprimé.").show();
            }
            else if(event.equals("edited")) {
                notifyEvent();
                new com.uha.mo.utils.Success(root, "Paramètres modifiés.").show();
            }
            else if(event.equals("added")) {
                root.getChildren().clear();
                notifyEvent();
                new com.uha.mo.utils.Success(root, "Le compte a été ajouté avec succès.").show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
