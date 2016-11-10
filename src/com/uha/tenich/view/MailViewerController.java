package com.uha.tenich.view;

import com.uha.tenich.MailStage;
import com.uha.tenich.model.Account;
import com.uha.tenich.model.Mail;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ResourceBundle;

public class MailViewerController implements Initializable {

    @FXML
    private ToolBar menuBar;
    @FXML
    private ImageView exitButton;
    @FXML
    private ImageView reply;
    @FXML
    private ImageView replyAll;
    @FXML
    private ImageView forward;
    @FXML
    private Label subject;
    @FXML
    private Label from;
    @FXML
    private Label to;
    @FXML
    private Label cc;
    @FXML
    private WebView content;

    private MailStage stage;

    private double xOffset;
    private double yOffset;

    private double x;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        menuBar.setOnMousePressed(event -> {
            xOffset = stage.getX() - event.getScreenX();
            yOffset = stage.getY() - event.getScreenY();
        });

        menuBar.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() + xOffset);
            stage.setY(event.getScreenY() + yOffset);
        });

        /********************************* EXIT BUTTON *********************************/

        exitButton.setOnMouseClicked(event -> {
            stage.notifyEnd();
            stage.close();
        });

        exitButton.setOnMouseEntered(event -> exitButton.setImage(new Image("images/delete_hover.png")));
        exitButton.setOnMouseExited(event -> exitButton.setImage(new Image("images/delete.png")));

        /********************************* REPLY BUTTON *********************************/

        reply.setOnMouseEntered(event -> reply.setImage(new Image("images/reply_hover.png")));
        reply.setOnMouseExited(event -> reply.setImage(new Image("images/reply.png")));

        /********************************* REPLY ALL BUTTON *********************************/

        replyAll.setOnMouseEntered(event -> replyAll.setImage(new Image("images/replyAll_hover.png")));
        replyAll.setOnMouseExited(event -> replyAll.setImage(new Image("images/replyAll.png")));

        /********************************* REPLY BUTTON *********************************/

        forward.setOnMouseEntered(event -> forward.setImage(new Image("images/forward_hover.png")));
        forward.setOnMouseExited(event -> forward.setImage(new Image("images/forward.png")));
    }

    public void setStage(MailStage stage) {
        this.stage = stage;
    }

    public void setMail(Mail mail, Account a) {

        this.subject.setText(mail.getSubject());
        this.from.setText(mail.getFrom());
        this.to.setText(a.getUsername());
        this.cc.setText("");

        this.content.getEngine().loadContent(mail.getContent());
    }
}

