package com.uha.mo.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Created by maxime on 17/11/2016.
 */
public class sendMailController implements Initializable {
    @FXML
    private Label sendToLabel;
    @FXML
    private TextField sendToTextField;
    @FXML
    private TextArea mailContentTextArea;
    @FXML
    private Button sendButton;

    private String account;
    private String password;

    public void setAccount(String account){
        this.account = account;
    }
    public void setPassword(String password){
        this.password = password;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void sendMail(){
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(account,password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("jeanmichelcrapaudensisa@gmail.fr"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(sendToTextField.getText().toString()));
            message.setSubject("Testing Subject");
            message.setText(mailContentTextArea.getText());

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }
}
