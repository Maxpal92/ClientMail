package com.uha.mo.view;

import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
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
    @FXML
    private ImageView writeButton;
    @FXML
    private ToolBar toolbar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        scroll.setFitToWidth(true);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        writeButton.setOnMouseEntered(event -> {
            this.writeButton.setImage(new Image("images/write_hover.png"));
            this.writeButton.setCursor(Cursor.HAND);
        });

        writeButton.setOnMouseExited(event -> {
            this.writeButton.setImage(new Image("images/write.png"));
            this.writeButton.setCursor(Cursor.DEFAULT);
        });
    }

    public VBox getAccountRoot() {
        return this.accountRoot;
    }

    public VBox getMailsContainer() {
        return mailsContainer;
    }

    public ToolBar getToolbar() { return this.toolbar; }

    public void setTitle(StringProperty mailAddressProperty) {
        this.mail.textProperty().bind(mailAddressProperty);
    }

    public void setParent(HBox accounts) {
        this.accountRoot.prefHeightProperty().bind(accounts.heightProperty());
    }
}
