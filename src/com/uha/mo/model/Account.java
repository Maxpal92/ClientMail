package com.uha.mo.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

/**
 * Created by othman on 29/10/2016.
 */
public class Account {

    private StringProperty password;
    private StringProperty username;
    private ObservableList<Mail> mails = FXCollections.observableArrayList();


    public Account(String mail, String password) {
        this.username = new SimpleStringProperty(mail);
        this.password = new SimpleStringProperty(password);
    }

    public String getPassword() {
        return password.get();
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public String getUsername() {
        return username.get();
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public ObservableList<Mail> getMails() {
        return mails;
    }

    public void setMails(ObservableList<Mail> mails) {
        this.mails = mails;
    }

    public Mail getMailById(long id) {
        for(Mail m : mails) {
            if(m.getID() == id) return m;
        }
        return null;
    }

    public void setMail(){
        for(Mail m : mails){

        }
    }

    public void SendMail(String subject) {
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
                        return new PasswordAuthentication("jeanmichelcrapaudensisa@gmail.com", "azerty12345!P");
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("jeanmichelcrapaudensisa@gmail.fr"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse("maxime.marcia@uha.fr"));
            message.setSubject(subject);
            message.setText("Dear Mail Crawler," +
                    "\n\n No spam to my email, please!");

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }

    public void CheckMail() throws MessagingException, IOException {
        //create properties field
        Properties properties = new Properties();

        properties.put("mail.pop3.host", "pop.gmail.com"); //host = pop.gmail.com
        properties.put("mail.pop3.port", "995");
        properties.put("mail.pop3.starttls.enable", "true");
        Session emailSession = Session.getDefaultInstance(properties);

        //create the POP3 store object and connect with the pop server
        Store store = emailSession.getStore("pop3s");

        store.connect("pop.gmail.com", username.toString(), password.toString());

        //create the folder object and open it
        Folder emailFolder = store.getFolder("INBOX");
        emailFolder.open(Folder.READ_ONLY);

        // retrieve the messages from the folder in an array and print it
        Message[] messages = emailFolder.getMessages();
        System.out.println("messages.length---" + messages.length);

        for (int i = 0, n = messages.length; i < n; i++) {
            Message message = messages[i];
            /*System.out.println("---------------------------------");
            System.out.println("Email Number " + (i + 1));
            System.out.println("Subject: " + message.getSubject());
            System.out.println("From: " + message.getFrom()[0]);
            System.out.println("Text: " + message.getContent().toString());*/

            String from = message.getFrom().toString();
            String subject = message.getSubject();
            String content = message.getContent().toString();
            Date date = message.getSentDate();

            Mail m = new Mail(from,subject,content,date);
            mails.add(m);
        }

        //close the store and folder objects
        emailFolder.close(false);
        store.close();

    }

}
