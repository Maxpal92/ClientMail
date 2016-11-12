package com.uha.mo.view;

import com.uha.mo.App;
import com.uha.mo.MailStage;
import com.uha.mo.model.Account;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by othman on 29/10/2016.
 */
public class MailController implements Initializable {

    @FXML
    private VBox root;
    @FXML
    private Label from;
    @FXML
    private Label date;
    @FXML
    private Label subject;

    private long id;
    private Account account;
    private App main;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        root.setOnMouseEntered(event -> {
            root.setCursor(Cursor.HAND);
            root.setStyle("-fx-background-color: rgba(52, 152, 219, 0.8);");
        });

        root.setOnMouseExited(event -> {
            root.setCursor(Cursor.DEFAULT);
            root.setStyle("-fx-background-color: rgba(52, 152, 219, 0.5);");
        });

        root.setOnMouseClicked(event -> {
            MailStage s = new MailStage(account.getMailById(this.id), account, this);
            s.show();
        });
    }

    public VBox getRoot() {
        return root;
    }

    public Label getFrom() {
        return from;
    }

    public Label getDate() {
        return date;
    }

    public Label getSubject() {
        return subject;
    }

    public void setID(long id) {
        this.id = id;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setApp(App app) {
        this.main = app;
    }

    public void notifyEnd() {
        main.notifyEnd(account, id);
    }
}
