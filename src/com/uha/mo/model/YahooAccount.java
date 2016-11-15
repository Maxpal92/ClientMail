package com.uha.mo.model;

/**
 * Created by maxime on 13/11/2016.
 */
public class YahooAccount extends Account {

    public static final String SMTP_HOST = "smtp.mail.yahoo.com";
    public static final String SMTP_PORT = "587";

    public static final String IMAP_HOST = "imap.mail.yahoo.com";
    public static final String IMAP_PORT = "993";

    public static final String POP3_HOST = "pop.mail.yahoo.com";
    public static final String POP3_PORT = "995";

    public YahooAccount(String mailAddres, String password){
        this.setMailAddress(mailAddres);
        this.setPassword(password);
    }
}
