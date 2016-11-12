package com.uha.mo.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by othman on 29/10/2016.
 */
public class AccountController implements Initializable {

    @FXML
    private VBox accountRoot;

    @FXML
    private ScrollPane scroll;

    @FXML
    private ToolBar toolbar;

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

    public ScrollPane getScroll() {
        return this.scroll;
    }

    public ToolBar getToolbar() {
        return toolbar;
    }

    public VBox getMailsContainer() {
        return mailsContainer;
    }

    public void setTitle(String mailProperty) {
        this.mail.setText(mailProperty);
    }
}
