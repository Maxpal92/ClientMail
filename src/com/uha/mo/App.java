package com.uha.mo;

import com.uha.mo.model.Account;
import com.uha.mo.model.GmailAccount;
import com.uha.mo.model.Message;
import com.uha.mo.model.Model;
import com.uha.mo.utils.AsyncTask;
import com.uha.mo.view.*;
import com.uha.mo.model.*;
import com.uha.mo.view.AccountController;
import com.uha.mo.view.MailController;
import com.uha.mo.view.MainController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.mail.MessagingException;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Properties;

public class App extends Application {

    private Stage primaryStage;
    private MainController rootController;

    private Model model;
    private App referenceForAccountChecker = this;

    @Override
    public void start(Stage primaryStage) {

        this.primaryStage = primaryStage;
        this.model = new Model();

        initRootLayout();

        this.primaryStage.initStyle(StageStyle.TRANSPARENT);
        this.primaryStage.show();
    }

    private void initNoAccountLayout() {
        try {
            FXMLLoader noAccountLoader = new FXMLLoader(getClass().getResource("view/noaccount.fxml"));
            VBox root = noAccountLoader.load();

            NoAccountController noAccountController = noAccountLoader.getController();
            noAccountController.setStage(this.primaryStage);
            noAccountController.setApp(this);

            Group noAccountGroup = new Group(root);
            noAccountGroup.setEffect(new DropShadow());

            Scene scene = new Scene(noAccountGroup);
            scene.setFill(Color.TRANSPARENT);

            this.primaryStage.setScene(scene);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initRootLayout() {

        this.model.getAccounts().clear();

        /************** LOAD ACCOUNTS REGISTERED FROM THE XML FILE **************/
        AccountLoader accountLoader = new AccountLoader();
        ArrayList<Account> accounts = accountLoader.getAccounts();

        if(accounts.size() == 0) {
            initNoAccountLayout();
        }
        else {
            this.model.getAccounts().addAll(accounts);

            /************** CHECK OUT EMAILS FOR EACH ACCOUNT **************/
            for (Account account : this.model.getAccounts()) {
                new AccountsChecker().execute(account);
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("view/main.fxml"));
            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            rootController = loader.getController();
            rootController.setStage(primaryStage);
            rootController.setModel(model);
            rootController.setApp(referenceForAccountChecker);

            addAccountsLayout();

            Group group = new Group(rootController.getRoot());
            group.setStyle("-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.5), 20, 0.5, 0.0, 0.0);");

            Scene scene = new Scene(group);
            scene.setFill(Color.TRANSPARENT);

            primaryStage.setScene(scene);
        }
    }

    private void addAccountsLayout() {

        try {
            for(Account a : model.getAccounts()) {

                FXMLLoader accountLoader = new FXMLLoader(getClass().getResource("view/account.fxml"));
                accountLoader.load();
                AccountController accountController = accountLoader.getController();

                this.rootController.getAccounts().getChildren().add(accountController.getAccountRoot());

                accountController.setTitle(a.nameProperty());
                accountController.setParent(this.rootController.getAccounts());

                a.setController(accountController);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addMessagesLayout() {
        for(Account a : this.model.getAccounts()) {

            a.getController().getMailsContainer().getChildren().clear();

            if(a.getMessages().size() == 0) {
                FXMLLoader emptyLoader = new FXMLLoader(getClass().getResource("view/nomessage.fxml"));
                Label label = null;
                try {
                    label = emptyLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                a.getController().getMailsContainer().getChildren().add(label);

                label.prefWidthProperty().bind(a.getController().getMailsContainer().widthProperty());
                label.setPrefHeight(100);
            }
            else {

                for(Message m : a.getMessages()) {

                    FXMLLoader mailLoader = new FXMLLoader(getClass().getResource("view/mail.fxml"));
                    HBox mailRoot = null;
                    try {
                        mailRoot = mailLoader.load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    MailController mailController = mailLoader.getController();

                    mailController.setAccountParent(a);
                    mailController.setMain(this);

                    a.getController().getMailsContainer().getChildren().add(mailRoot);

                    mailController.getFrom().setText(m.getFrom());
                    mailController.getDate().setText(new SimpleDateFormat("dd MMM - HH:mm:ss").format(m.getDate()));
                    mailController.getSubject().setText(m.getSubject());
                    mailController.setID(m.getID());

                    if(a.getMessages().size() > 5)
                        mailRoot.prefWidthProperty().bind(a.getController().getAccountRoot().widthProperty().subtract(40));
                    else
                        mailRoot.prefWidthProperty().bind(a.getController().getAccountRoot().widthProperty().subtract(20));
                }
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void notifyEnd(long id) {
        for(Account account : this.model.getAccounts()) {
            account.getMessages().remove(account.getMailById(id));
        }
        this.rootController.getAccounts().getChildren().clear();
        addAccountsLayout();
        addMessagesLayout();
    }

    public void setScene(String sceneType) throws IOException {

        switch (sceneType) {
            case "gmail":
                FXMLLoader newGmailAccountLoader = new FXMLLoader(getClass().getResource("view/newGmailAccount.fxml"));
                VBox gmailroot = newGmailAccountLoader.load();
                ((NewGmailAccountController) newGmailAccountLoader.getController()).setStage(this.primaryStage);
                ((NewGmailAccountController) newGmailAccountLoader.getController()).setApp(this);

                Group newAccountGroup = new Group(gmailroot);
                newAccountGroup.setEffect(new DropShadow());

                Scene scene = new Scene(newAccountGroup);
                scene.setFill(Color.TRANSPARENT);

                this.primaryStage.setScene(scene);
                break;

            case "yahoo":
                FXMLLoader newYahooAccountLoader = new FXMLLoader(getClass().getResource("view/newYahooAccount.fxml"));
                VBox yahooroot = newYahooAccountLoader.load();
                ((NewYahooAccountController) newYahooAccountLoader.getController()).setStage(this.primaryStage);
                ((NewYahooAccountController) newYahooAccountLoader.getController()).setApp(this);

                Group newAccountGroup1 = new Group(yahooroot);
                newAccountGroup1.setEffect(new DropShadow());

                Scene scene2 = new Scene(newAccountGroup1);
                scene2.setFill(Color.TRANSPARENT);

                this.primaryStage.setScene(scene2);
                break;

            case "custom":
                FXMLLoader newCustomAccountLoader = new FXMLLoader(getClass().getResource("view/newCustomAccount.fxml"));
                VBox customroot = newCustomAccountLoader.load();
                ((NewCustomAccountController) newCustomAccountLoader.getController()).setStage(this.primaryStage);
                ((NewCustomAccountController) newCustomAccountLoader.getController()).setApp(this);

                Group newAccountGroup2 = new Group(customroot);
                newAccountGroup2.setEffect(new DropShadow());

                Scene scene3 = new Scene(newAccountGroup2);
                scene3.setFill(Color.TRANSPARENT);

                this.primaryStage.setScene(scene3);
                break;

            case "settings":
                FXMLLoader settingsLoader = new FXMLLoader((getClass().getResource("view/settings.fxml")));
                BorderPane settingsRoot = settingsLoader.load();
                ((SettingsController) settingsLoader.getController()).setStage(this.primaryStage);
                ((SettingsController) settingsLoader.getController()).setApp(this);

                Group settingsGroup = new Group(settingsRoot);
                settingsGroup.setEffect(new DropShadow());

                Scene scene4 = new Scene(settingsGroup);
                scene4.setFill(Color.TRANSPARENT);

                this.primaryStage.setScene(scene4);
                break;
        }
    }

    public void refreshModel() {
        this.model.getAccounts().clear();
        this.model.getAccounts().addAll(new AccountLoader().getAccounts());
    }

    private class AccountsChecker extends AsyncTask<Account, ArrayList<Message>> {

        @Override
        protected ArrayList<Message> doInBackground() {
            if(params[0] instanceof GmailAccount) {
                return new GmailChecker(params[0]).getMessages();
            }
            else if(params[0] instanceof YahooAccount) {
                try {
                    return new YahooChecker(params[0]).getMessages();
                } catch (MessagingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return new ArrayList<>();
        }

        @Override
        protected void onPostExecute(ArrayList<Message> messages) {
            params[0].getMessages().addAll(messages);
            addMessagesLayout();
        }
    }

    public Model getModel() {
        return this.model;
    }
}
