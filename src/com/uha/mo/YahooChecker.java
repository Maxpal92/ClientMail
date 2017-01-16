package com.uha.mo;

import com.uha.mo.model.Account;
import com.uha.mo.model.Message;
import com.uha.mo.model.YahooAccount;

import javax.mail.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

/**
 * Created by maxime on 13/11/2016.
 */
public class YahooChecker {

    private ArrayList<Message> messages = new ArrayList<>();
    private Account account;
    private String from;
    private String subject;
    private String content;
    private Date date;

    public YahooChecker(Account account) throws MessagingException, IOException {
        this.account = account;

        Properties properties = new Properties();
        properties.put("mail.pop3.host", YahooAccount.POP3_HOST);
        properties.put("mail.pop3.port", YahooAccount.POP3_PORT);
        properties.put("mail.pop3.starttls.enable", "true");

        Session emailSession = Session.getDefaultInstance(properties);

        Store store = emailSession.getStore("pop3s");
        store.connect(YahooAccount.POP3_HOST,account.getMailAddress(),account.getPassword());

        Folder inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_ONLY);

        for(int i = inbox.getMessages().length - 1; i >= 0; i--) {
            javax.mail.Message msg = inbox.getMessages()[i];
            this.from = msg.getFrom()[0].toString();
            this.subject = msg.getSubject();
            this.content = "";
            this.date = msg.getSentDate();

            if(!msg.getFlags().contains(Flags.Flag.SEEN)) {
                if(msg.getContent() instanceof String)
                    this.messages.add(new Message(msg.getFrom()[0].toString(), account.getMailAddress(), msg.getSubject(), (String)msg.getContent(), msg.getSentDate(), msg));
                else
                    this.messages.add(new Message(msg.getFrom()[0].toString(), account.getMailAddress(), msg.getSubject(), ((Multipart)msg.getContent()).getBodyPart(0).getContent().toString(), msg.getSentDate(), msg));
            }
        }

        inbox.close(true);
        store.close();
    }

    public ArrayList<Message> getMessages() {
        return this.messages;
    }
}
