package com.uha.mo.view;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.ResourceBundle;



public class AccountController implements Initializable {

    @FXML
    private VBox accountRoot;

    @FXML
    private ScrollPane scroll;

    @FXML
    private VBox mailsContainer;

    @FXML
    private Label mail;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        scroll.setFitToWidth(true);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    }

    public VBox getAccountRoot() {
        return this.accountRoot;
    }

    public VBox getMailsContainer() {
        return mailsContainer;
    }

    public void setTitle(StringProperty mailAddressProperty) {
        this.mail.textProperty().bind(mailAddressProperty);
    }

    public void setParent(HBox accounts) {
        this.accountRoot.prefHeightProperty().bind(accounts.heightProperty().subtract(20));
    }
}
