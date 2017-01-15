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
    private WebView content;

    private MailStage stage;

    private double xOffset;
    private double yOffset;

    private MailController mailController;
    private Account account;

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

        /********************************* REPLY BUTTON *********************************/

        forward.setOnMouseEntered(event -> forward.setImage(new Image("images/forward_hover.png")));
        forward.setOnMouseExited(event -> forward.setImage(new Image("images/forward.png")));
    }

    public void setStage(MailStage stage) {
        this.stage = stage;
    }

    public void setMailController(MailController mailController) {
        this.mailController = mailController;
    }

    public void setMail(Message message) {

        this.subject.setText(message.getSubject());
        this.from.setText(message.getFrom());
        this.to.setText(message.getTo());
        this.cc.setText("");
        //this.content.getEngine().loadContent(message.getContent());
    }

    public void getSendMailBox(){

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/sendMail.fxml"));
            VBox vBox = loader.load();
            Stage sendMailStage = new Stage();
            sendMailStage.setTitle("Mail Sender");
            sendMailStage.initModality(Modality.WINDOW_MODAL);

            Scene scene = new Scene(vBox);
            sendMailStage.setScene(scene);

            sendMailController controller = loader.getController();
            controller.setAccount(this.account);
            controller.setSubject(this.subject.getText().toString());
            controller.setSubjectTextField("Re : " + this.subject.getText().toString());


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

