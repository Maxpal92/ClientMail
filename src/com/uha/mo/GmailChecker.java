package com.uha.mo;

import com.uha.mo.model.Account;
import com.uha.mo.model.GmailAccount;
import com.uha.mo.model.Message;

import javax.mail.*;
import java.util.ArrayList;
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
            inbox.open(Folder.READ_ONLY);
            javax.mail.Message msg = inbox.getMessage(inbox.getMessageCount());

            Address[] in = msg.getFrom();

            Multipart mp = (Multipart) msg.getContent();
            BodyPart bp = mp.getBodyPart(0);

            this.messages.add(new Message(msg.getFrom()[0].toString(), account.getMailAddress(), msg.getSubject(), bp.getContent().toString(), msg.getSentDate()));

        } catch (Exception mex) {
            mex.printStackTrace();
        }
    }

    public ArrayList<Message> getMessages() {
        return this.messages;
    }
}
