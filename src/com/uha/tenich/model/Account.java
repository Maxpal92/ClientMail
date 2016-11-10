package com.uha.tenich.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Created by othman on 29/10/2016.
 */
public class Account {

    private StringProperty password;
    private StringProperty mailProperty;
    private ObservableList<Mail> mails = FXCollections.observableArrayList();
    private EMailController mailController;

    public Account(String mail, String password) {
        this.mailProperty = new SimpleStringProperty(mail);
        this.password = new SimpleStringProperty(password);
        this.mailController = new EMailController(mailProperty.toString(),password.toString());
    }

    public String getPassword() {
        return password.get();
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public String getMailProperty() {
        return mailProperty.get();
    }

    public StringProperty mailPropertyProperty() {
        return mailProperty;
    }

    public void setMailProperty(String mailProperty) {
        this.mailProperty.set(mailProperty);
    }

    public ObservableList<Mail> getMails() {
        return mails;
    }

    public void setMails(ObservableList<Mail> mails) {
        this.mails = mails;
    }

    public Mail getMailById(long id) {
        for(Mail m : mails) {
            if(m.getID() == id) return m;
        }
        return null;
    }
}
