package com.uha.mo.view;

import com.uha.mo.MailStage;
import com.uha.mo.model.Account;
import com.uha.mo.model.Message;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
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
    private WebView contentViewer;

    private MailStage stage;

    private double xOffset;
    private double yOffset;

    private MailController mailController;
    private Account account;
    private Message message;

    public MailViewerController() {
    }

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
            mailController.notifyEnd();
            stage.close();
        });

        exitButton.setOnMouseEntered(event -> exitButton.setImage(new Image("images/delete_hover.png")));
        exitButton.setOnMouseExited(event -> exitButton.setImage(new Image("images/delete.png")));

        /********************************* REPLY BUTTON *********************************/

        reply.setOnMouseEntered(event -> reply.setImage(new Image("images/reply_hover.png")));
        reply.setOnMouseExited(event -> reply.setImage(new Image("images/reply.png")));
        reply.setOnMouseClicked(event -> this.getSendMailBox());

        /********************************* REPLY ALL BUTTON *********************************/

        replyAll.setOnMouseEntered(event -> replyAll.setImage(new Image("images/replyAll_hover.png")));
        replyAll.setOnMouseExited(event -> replyAll.setImage(new Image("images/replyAll.png")));
        replyAll.setOnMouseClicked(event -> replyAll());

        /********************************* REPLY BUTTON *********************************/

        forward.setOnMouseEntered(event -> forward.setImage(new Image("images/forward_hover.png")));
        forward.setOnMouseExited(event -> forward.setImage(new Image("images/forward.png")));
        forward.setOnMouseClicked(event -> forward());
    }

    public void setStage(MailStage stage) {
        this.stage = stage;
    }

    public void setMailController(MailController mailController) {
        this.mailController = mailController;
    }

    public void setMail(Message message) {

        this.message = message;
        this.subject.setText(message.getSubject());
        this.from.setText(message.getFrom());

        String to = "";
        for(int i = 0; i < message.getTo().length; i++) {
            to +=  message.getTo()[i];
            if(i < message.getTo().length - 1)
                to += " , ";
        }
        this.to.setText(to);

        String cc = "";
        for(int i = 0; i < message.getCc().length; i++) {
            cc +=  message.getCc()[i];
            if(i < message.getCc().length - 1)
                cc += " , ";
        }
        this.cc.setText(cc);
        this.contentViewer.getEngine().loadContent(message.getContent());
    }

    private void forward() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("sendMail.fxml"));
            VBox vBox = loader.load();
            Stage sendMailStage = new Stage();
            sendMailStage.setTitle("Transférer");
            sendMailStage.initModality(Modality.WINDOW_MODAL);

            Scene scene = new Scene(vBox);
            sendMailStage.setScene(scene);

            SendMailController controller = loader.getController();
            controller.setStage(sendMailStage);
            controller.setAccount(this.account);
            controller.setSubject(this.subject.getText().toString());
            controller.setSubjectTextField("Fwd : " + this.subject.getText().toString());
            controller.setMailContentTextArea(message.getContent());

            sendMailStage.showAndWait();

        }
        catch(IOException var7){
            var7.printStackTrace();
        }
    }

    private void replyAll() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("sendMail.fxml"));
            VBox vBox = loader.load();
            Stage sendMailStage = new Stage();
            sendMailStage.setTitle("Répondre à tous");
            sendMailStage.initModality(Modality.WINDOW_MODAL);

            Scene scene = new Scene(vBox);
            sendMailStage.setScene(scene);

            SendMailController controller = loader.getController();
            controller.setStage(sendMailStage);
            controller.setAccount(this.account);
            controller.setSubject(this.subject.getText().toString());
            controller.setSubjectTextField("Re : " + this.subject.getText().toString());

            controller.setSendTo(this.from.getText());
            controller.setSendCc(message.getCc());
            controller.setSendToTextField(this.from.getText());

            sendMailStage.showAndWait();

        }
        catch(IOException var7){
            var7.printStackTrace();
        }
    }

    public void getSendMailBox(){

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("sendMail.fxml"));
            VBox vBox = loader.load();
            Stage sendMailStage = new Stage();
            sendMailStage.setTitle("Mail Sender");
            sendMailStage.initModality(Modality.WINDOW_MODAL);

            Scene scene = new Scene(vBox);
            sendMailStage.setScene(scene);

            SendMailController controller = loader.getController();
            controller.setStage(sendMailStage);
            controller.setAccount(this.account);
            controller.setSubject(this.subject.getText().toString());
            controller.setSubjectTextField("Re : " + this.subject.getText().toString());

            controller.setSendTo(this.from.getText());
            controller.setSendToTextField(this.from.getText());


            sendMailStage.showAndWait();

        }
        catch(IOException var7){
            var7.printStackTrace();
        }
    }

    public void setAccount(Account account){
        this.account = account;
    }
}

