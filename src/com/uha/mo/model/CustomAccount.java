package com.uha.mo.model;


public class CustomAccount extends Account {

    private String SMTP_HOST;
    private String SMTP_PORT;

    private String IMAP_HOST;
    private String IMAP_PORT;

    public CustomAccount(String mailAddress, String password) {
        this.setMailAddress(mailAddress);
        this.setPassword(password);
    }

    public String getSMTP_HOST() { return SMTP_HOST; }
    public void setSMTP_HOST(String SMTP_HOST) { this.SMTP_HOST = SMTP_HOST; }

    public String getSMTP_PORT() { return SMTP_PORT; }
    public void setSMTP_PORT(String SMTP_PORT) { this.SMTP_PORT = SMTP_PORT; }

    public String getIMAP_HOST() { return IMAP_HOST; }
    public void setIMAP_HOST(String IMAP_HOST) { this.IMAP_HOST = IMAP_HOST; }

    public String getIMAP_PORT() { return IMAP_PORT; }
    public void setIMAP_PORT(String IMAP_PORT) { this.IMAP_PORT = IMAP_PORT; }
}
