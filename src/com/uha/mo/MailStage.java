package com.uha.mo;

import com.uha.mo.model.Account;
import com.uha.mo.model.Message;
import com.uha.mo.view.MailController;
import com.uha.mo.view.MailViewerController;
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

    private Message message;
    private Account account;
    private MailController controller;

    public MailStage(Message message, Account account, MailController controller) {
        this.message = message;
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
            controller.setMail(this.message, this.account);

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
