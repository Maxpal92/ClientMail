package com.uha.mo.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public abstract class Account {

    private StringProperty mailAddress = new SimpleStringProperty();
    private StringProperty password = new SimpleStringProperty();
    private ObservableList<Message> messages = FXCollections.observableArrayList();

    public String getMailAddress() { return mailAddress.get(); }
    public StringProperty mailAddressProperty() { return mailAddress; }
    public void setMailAddress(String mailAddress) { this.mailAddress.set(mailAddress); }

    public String getPassword() { return password.get(); }
    public StringProperty passwordProperty() { return password; }
    public void setPassword(String password) { this.password.set(password); }

    public ObservableList<Message> getMessages() { return messages; }

    public Message getMailById(long id) {
        for(Message m : messages) {
            if(m.getID() == id) return m;
        }
        return null;
    }
}
