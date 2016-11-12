package com.uha.mo.model;

/**
 * Created by Othman on 12/11/2016.
 */
public class GmailAccount extends Account {

    public static final String SMTP_HOST = "smtp.gmail.com";
    public static final String SMTP_PORT = "465";

    public static final String IMAP_HOST = "imap.gmail.com";
    public static final String IMAP_PORT = "993";

    public GmailAccount(String mailAddress, String password) {
        this.setMailAddress(mailAddress);
        this.setPassword(password);
    }
}
