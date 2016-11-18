package com.uha.mo;

import com.uha.mo.model.Account;
import com.uha.mo.model.GmailAccount;
import com.uha.mo.model.Message;
import com.uha.mo.model.Model;
import com.uha.mo.view.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class App extends Application {

    private Stage primaryStage;
    private MainController rootController;

    private Model model;
    private ArrayList<Account> accounts;

    @Override
    public void start(Stage primaryStage) {

        this.primaryStage = primaryStage;
        this.model = new Model();

        /************** LOAD ACCOUNTS REGISTERED FROM THE XML FILE **************/
        AccountLoader loader = new AccountLoader();
        this.accounts = loader.getAccounts();

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
        try {
            if(accounts.size() == 0) {
                initNoAccountLayout();
            }
            else {
                this.model.getAccounts().addAll(accounts);

                /************** CHECK OUT EMAILS FOR EACH ACCOUNT **************/
                for (Account account : this.model.getAccounts()) {
                    if (account instanceof GmailAccount) {
                        ArrayList<Message> messages = new GmailChecker(account).getMessages();
                        account.getMessages().addAll(messages);
                    }
                }

                FXMLLoader loader = new FXMLLoader(getClass().getResource("view/main.fxml"));
                loader.load();

                this.rootController = loader.getController();
                this.rootController.setStage(this.primaryStage);

                Group group = new Group(this.rootController.getRoot());
                group.setEffect(new DropShadow());

                Scene scene = new Scene(group);
                scene.setFill(Color.TRANSPARENT);

                this.primaryStage.initStyle(StageStyle.TRANSPARENT);
                this.primaryStage.setScene(scene);

                addAccountsLayout();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addAccountsLayout() {

        try {
            for(Account a : model.getAccounts()) {

                FXMLLoader accountLoader = new FXMLLoader(getClass().getResource("view/account.fxml"));
                accountLoader.load();
                AccountController accountsController = accountLoader.getController();

                this.rootController.getAccounts().getChildren().add(accountsController.getAccountRoot());

                accountsController.setTitle(a.mailAddressProperty());
                accountsController.setParent(this.rootController.getAccounts());

                if(a.getMessages().size() == 0) {
                    FXMLLoader emptyLoader = new FXMLLoader(getClass().getResource("view/nomessage.fxml"));
                    Label label = emptyLoader.load();
                    accountsController.getMailsContainer().getChildren().add(label);

                    label.prefWidthProperty().bind(accountsController.getMailsContainer().widthProperty());
                    label.setPrefHeight(100);
                }
                else {
                    for(Message m : a.getMessages()) {

                        FXMLLoader mailLoader = new FXMLLoader(getClass().getResource("view/mail.fxml"));
                        VBox mailRoot = mailLoader.load();
                        MailController mailController = mailLoader.getController();

                        mailController.setAccountParent(a);
                        mailController.setMain(this);

                        accountsController.getMailsContainer().getChildren().add(mailRoot);

                        mailController.getFrom().setText(m.getFrom());
                        mailController.getDate().setText(new SimpleDateFormat("dd MMM - HH:mm:ss").format(m.getDate()));
                        mailController.getSubject().setText(m.getSubject());
                        mailController.setID(m.getID());

                        mailRoot.prefWidthProperty().bind(accountsController.getAccountRoot().widthProperty().subtract(20));
                    }
                }
            }

            if(model.getAccounts().size() > 2) {
                this.rootController.getScrollPane().setPrefWidth(640*2 + 10 + 2*10);
            }

        } catch (IOException e) {
            e.printStackTrace();
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
        }
    }
}
