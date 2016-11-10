package com.uha.tenich;

import com.uha.tenich.model.Account;
import com.uha.tenich.model.Mail;
import com.uha.tenich.view.MailController;
import com.uha.tenich.view.MailViewerController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class MailStage extends Stage {

    private Mail mail;
    private Account account;
    private MailController controller;

    public MailStage(Mail mail, Account account, MailController controller) {
        this.mail = mail;
        this.account = account;
        this.controller = controller;

        initRootLayout();
    }

    private void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("view/mailViewer.fxml"));
            VBox root = loader.load();

            MailViewerController controller = loader.getController();
            controller.setStage(this);
            controller.setMail(this.mail, this.account);

            Group group = new Group(root);
            group.setEffect(new DropShadow());

            Scene scene = new Scene(group);
            scene.setFill(Color.TRANSPARENT);

            this.initStyle(StageStyle.TRANSPARENT);
            this.setScene(scene);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void notifyEnd() {
        this.controller.notifyEnd();
    }
}
