package com.uha.mo;

import com.uha.mo.model.Account;
import com.uha.mo.model.GmailAccount;
import com.uha.mo.model.Message;

import javax.mail.*;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Pattern;


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
            inbox.open(Folder.READ_ONLY);

            for(int i = inbox.getMessages().length - 1; i >= 0; i--) {
                javax.mail.Message msg = inbox.getMessages()[i];
                writePart(msg);

                //this.messages.add(new Message(msg.getFrom()[0].toString(), account.getMailAddress(), msg.getSubject(), bp.getContent().toString(), msg.getSentDate()));
            }

            inbox.close(false);
            store.close();

        } catch (Exception mex) {
            mex.printStackTrace();
        }
    }

    public void writePart(Part p) throws Exception {
        if (p instanceof javax.mail.Message) {
            this.date = ((javax.mail.Message) p).getSentDate();
            writeEnvelope((javax.mail.Message) p);
        }
        //check if the content is plain text
        if (p.isMimeType("text/plain")) {
            this.content = p.getContent().toString();
            this.messages.add(new Message(this.from, this.account.getMailAddress(), this.subject, this.content, this.date));
        }
        //check if the content has attachment
        else if (p.isMimeType("multipart/*")) {
            Multipart mp = (Multipart) p.getContent();
            int count = mp.getCount();
            for (int i = 0; i < count; i++)
                writePart(mp.getBodyPart(i));
        }
    }

    public void writeEnvelope(javax.mail.Message m) throws Exception {

        if (m.getFrom() != null)
            this.from = m.getFrom()[0].toString();

        if (m.getSubject() != null)
            this.subject = m.getSubject();
    }

    public ArrayList<Message> getMessages() {
        return this.messages;
    }
}
