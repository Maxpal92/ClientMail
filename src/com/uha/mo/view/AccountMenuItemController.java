package com.uha.mo.view;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by othman on 20/12/2016.
 */
public class AccountMenuItemController implements Initializable {

    @FXML
    private ImageView accountIcon;
    @FXML
    private Label accountName;
    @FXML
    private Label accountAddress;
    @FXML
    private HBox root;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setAccountIcon(Image accountIcon) {
        this.accountIcon .setImage(accountIcon);
    }

    public void setAccountName(StringProperty accountName) {
        this.accountName.textProperty().bind(accountName);
    }

    public void setAccountAddress(String accountAddress) {
        this.accountAddress.setText(accountAddress);
    }
}
