package com.uha.tenich;

import com.uha.tenich.model.Account;
import com.uha.tenich.model.Mail;
import com.uha.tenich.model.Model;
import com.uha.tenich.view.AccountController;
import com.uha.tenich.view.MailController;
import com.uha.tenich.view.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class App extends Application {

    private Stage primaryStage;
    private MainController rootController;

    private Model model;

    public MainController getRootController() {
        return this.rootController;
    }

    @Override
    public void start(Stage primaryStage) {

        this.primaryStage = primaryStage;
        this.model = new Model();
        model.populate();

        initRootLayout();
        addAccountsLayout();

        this.primaryStage.show();

    }

    private void initRootLayout() {
        try {
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

                accountsController.setTitle(a.getMailProperty());

                accountsController.getAccountRoot().prefHeightProperty().bind(this.rootController.getAccounts().heightProperty().subtract(20));

                if(a.getMails().size() == 0) {
                    FXMLLoader emptyLoader = new FXMLLoader(getClass().getResource("view/nomessage.fxml"));
                }

                for(Mail m : a.getMails()) {

                    FXMLLoader mailLoader = new FXMLLoader(getClass().getResource("view/mail.fxml"));
                    VBox mailRoot = mailLoader.load();
                    MailController mailController = mailLoader.getController();
                    mailController.setAccount(a);
                    mailController.setApp(this);
                    accountsController.getMailsContainer().getChildren().add(mailRoot);

                    mailController.getFrom().setText(m.getFrom());
                    mailController.getDate().setText(new SimpleDateFormat("dd MMM - HH:mm:ss").format(m.getDate()));
                    mailController.getSubject().setText(m.getSubject());
                    mailController.setID(m.getID());

                    mailRoot.prefWidthProperty().bind(accountsController.getAccountRoot().widthProperty().subtract(20));
                }
            }

            this.rootController.getToolbar().setPrefWidth(this.rootController.getRoot().getWidth());
            this.rootController.getScrollPane().setFitToHeight(true);

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

    public void notifyEnd(Account account, long id) {
        account.getMails().remove(account.getMailById(id));
        this.rootController.getAccounts().getChildren().clear();
        addAccountsLayout();
    }
}
