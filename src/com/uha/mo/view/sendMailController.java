package com.uha.mo.view;

import com.uha.mo.model.Account;
import com.uha.mo.model.GmailAccount;
import com.uha.mo.model.YahooAccount;
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
    private TextField sendToTextField;
    @FXML
    private TextArea mailContentTextArea;
    @FXML
    private TextField subjectTextField;

    private Account account;
    private String subject;
    private String sendTo;

    public TextField getSubjectTextField() {
        return subjectTextField;
    }

    public void setSubjectTextField(String cc) {
        subjectTextField.setText(cc);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setSubject(String sub){
        this.subject = sub;
    }

    public String getSubject(){
        return subject;
    }

    public void setSendTo(String sendTo){
        this.sendTo = sendTo;
    }

    public void setSendToTextField (String to){
        this.sendToTextField.setText(to);
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void sendMailFromYahoo(){

        // Get system properties
        Properties properties = System.getProperties();
        // Setup mail server
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", YahooAccount.SMTP_HOST);
        properties.put("mail.smtp.user", account.getMailAddress());
        properties.put("mail.smtp.password", account.getPassword());
        properties.put("mail.smtp.port", YahooAccount.SMTP_PORT);
        properties.put("mail.smtp.auth", "true");

        // Get the default Session object.
        Session session = Session.getInstance(properties);

        try{
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(account.getMailAddress()));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(sendToTextField.getText().toString()));

            // Set Subject: header field
            message.setSubject("This is the Subject Line!");

            // Now set the actual message
            message.setText(mailContentTextArea.getText());

            // Send message
            Transport transport = session.getTransport("smtp");
            System.out.println(account.getMailAddress() + account.getPassword());
            transport.connect(YahooAccount.SMTP_HOST,account.getMailAddress(), account.getPassword());

            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            System.out.println("Sent message successfully....");
        }catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

    public void sendMailFromGmail(){

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        System.out.println("Debut du debug");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(account.getMailAddress(),account.getPassword());
                    }
                });
        System.out.println(account.getMailAddress() + account.getPassword());

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(account.getMailAddress()));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(sendToTextField.getText().toString()));
            message.setSubject("Testing Subject");
            message.setText(mailContentTextArea.getText());

            Transport.send(message);


        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }

    public void sendMail(){
        if(account instanceof GmailAccount){
            sendMailFromGmail();
        }
        if(account instanceof YahooAccount){
            sendMailFromYahoo();
        }
    }
}
