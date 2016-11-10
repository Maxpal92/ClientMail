package com.uha.tenich.model;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by maxime on 10/11/2016.
 */
public class EMailController {

    private String username;
    private String password;

    private String subject;
    private String from;
    private String text;

    public EMailController(String username, String password) {
        this.password = password;
        this.username = username;
    }

    public EMailController() {
        this.password = "password";
        this.username = "username";
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

        store.connect("pop.gmail.com", username, password);

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

            this.subject = message.getSubject();
            this.from = message.getFrom().toString();
            this.text = message.getContent().toString();

        }

        //close the store and folder objects
        emailFolder.close(false);
        store.close();

    }
}

