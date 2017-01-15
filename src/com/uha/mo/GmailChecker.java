package com.uha.mo;

import com.uha.mo.model.Account;
import com.uha.mo.model.GmailAccount;
import com.uha.mo.model.Message;

import javax.mail.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;


public class GmailChecker {

    private ArrayList<Message> messages = new ArrayList<>();
    private Account account;
    private String from;
    private String subject;
    private String content;
    private Date date;

    public GmailChecker(Account account) {

        this.account = account;

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
                if(!msg.getFlags().contains(Flags.Flag.SEEN))
                    this.messages.add(new Message(msg.getFrom()[0].toString(), account.getMailAddress(), msg.getSubject(), msg, msg.getSentDate()));
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
