package com.uha.mo.model;

import com.uha.mo.view.AccountController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public abstract class Account {

    private StringProperty mailAddress = new SimpleStringProperty();
    private StringProperty password = new SimpleStringProperty();
    private ObservableList<com.uha.mo.model.Message> messages = FXCollections.observableArrayList();
    private StringProperty name = new SimpleStringProperty();
    private int syncPeriod = 900000; // De base, 15 minutes
    private boolean notifications = true; // De base, les notifications sont ON

    private AccountController controller;


    public String getMailAddress() { return mailAddress.get(); }
    public StringProperty mailAddressProperty() { return mailAddress; }
    public void setMailAddress(String mailAddress) { this.mailAddress.set(mailAddress); }

    public String getPassword() { return password.get(); }
    public StringProperty passwordProperty() { return password; }
    public void setPassword(String password) { this.password.set(password); }

    public String getName() {
        if(name.get() == null)
            return getMailAddress();
        else
            return name.get();
    }
    public StringProperty nameProperty() { return name; }
    public void setName(String name) { this.name.set(name); }

    public ObservableList<com.uha.mo.model.Message> getMessages() { return messages; }

    public com.uha.mo.model.Message getMailById(long id) {
        for(com.uha.mo.model.Message m : messages) {
            if(m.getID() == id) return m;
        }
        return null;
    }

    public void setController(AccountController controller) {
        this.controller = controller;
    }

    public AccountController getController() {
        return this.controller;
    }

    public int getSyncPeriod() { return syncPeriod; }
    public void setSyncPeriod(int syncPeriod) { this.syncPeriod = syncPeriod; }

    public boolean isNotifications() { return notifications; }
    public void setNotifications(boolean notifications) { this.notifications = notifications; }
}
