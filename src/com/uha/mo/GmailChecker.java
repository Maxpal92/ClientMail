package com.uha.mo;

import com.uha.mo.model.Account;
import com.uha.mo.model.GmailAccount;
import com.uha.mo.model.Message;

import javax.mail.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;


public class GmailChecker {

    private ArrayList<Message> messages = new ArrayList<>();

    public GmailChecker(Account account) {

        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "imaps");
        try {
            Session session = Session.getInstance(props, null);
            Store store = session.getStore();
            store.connect(GmailAccount.IMAP_HOST, account.getMailAddress(), account.getPassword());

            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_WRITE);

            for(int i = inbox.getMessages().length - 1; i >= 0; i--) {
                javax.mail.Message msg = inbox.getMessages()[i];
                if(!msg.getFlags().contains(Flags.Flag.SEEN)) {
                    if(msg.getContent() instanceof String)
                        this.messages.add(new Message(msg.getFrom()[0].toString(), account.getMailAddress(), msg.getSubject(), (String)msg.getContent(), msg.getSentDate(), msg));
                    else
                        this.messages.add(new Message(msg.getFrom()[0].toString(), account.getMailAddress(), msg.getSubject(), ((Multipart)msg.getContent()).getBodyPart(0).getContent().toString(), msg.getSentDate(), msg));
                }
            }

            inbox.close(true);
            store.close();

        } catch (Exception mex) {
            mex.printStackTrace();
        }
    }

    public ArrayList<Message> getMessages() {
        return this.messages;
    }
}
