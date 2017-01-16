package com.uha.mo.notifications;

import com.uha.mo.model.Account;
import com.uha.mo.model.CustomAccount;
import com.uha.mo.model.GmailAccount;
import com.uha.mo.model.YahooAccount;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by othman on 16/01/2017.
 */
public class NotificationController implements Initializable {

    @FXML
    private ImageView exitButton;
    @FXML
    private ImageView icon;
    @FXML
    private HBox root;
    @FXML
    private Label address;
    @FXML
    private Label count;
    @FXML
    private VBox mainRoot;

    private Stage mainStage;
    private Stage notificationStage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        exitButton.setOnMouseClicked(event -> {
            notificationStage.hide();
            if(Notification.counter > 1)
                Notification.counter --;
        });
        root.setOnMouseClicked(event -> {
            mainStage.getScene().getRoot().setVisible(true);
            mainStage.toFront();
        });
    }

    public void config(Stage mainStage, Stage notificationStage, Account account, int count) {

        this.mainStage = mainStage;
        this.notificationStage = notificationStage;

        this.address.setText(account.getMailAddress());
        this.count.setText(String.valueOf(count));

        if(account instanceof GmailAccount)
            this.icon.setImage(new Image("images/gmail.png"));
        else if(account instanceof YahooAccount)
            this.icon.setImage(new Image("images/yahoo.png"));
        else if(account instanceof CustomAccount)
            this.icon.setImage(new Image("images/custom.png"));
    }

    public VBox getMainRoot() {
        return mainRoot;
    }
}
