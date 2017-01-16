package com.uha.mo.model;


public class CustomAccount extends Account {

    private String SMTP_HOST;
    private String SMTP_PORT;

    private String GET_PROTOCOL ;
    private String GET_PROTOCOL_HOST;
    private String GET_PROTOCOL_PORT ;


    public CustomAccount(String mailAddress, String password) {
        this.setMailAddress(mailAddress);
        this.setPassword(password);
    }

    public String getSMTP_HOST() { return SMTP_HOST; }
    public void setSMTP_HOST(String SMTP_HOST) { this.SMTP_HOST = SMTP_HOST; }

    public String getSMTP_PORT() { return SMTP_PORT; }
    public void setSMTP_PORT(String SMTP_PORT) { this.SMTP_PORT = SMTP_PORT; }

    public String getGET_PROTOCOL() { return this.GET_PROTOCOL; }
    public void setGET_PROTOCOL(String protocol) { this.GET_PROTOCOL = protocol; }

    public String getGET_PROTOCOL_HOST() { return this.GET_PROTOCOL_HOST; }
    public void setGET_PROTOCOL_HOST(String host) { this.GET_PROTOCOL_HOST = host; }

    public String getGET_PROTOCOL_PORT() { return this.GET_PROTOCOL_PORT; }
    public void setGET_PROTOCOL_PORT(String port) { this.GET_PROTOCOL_PORT = port; }
}
