package com.uha.mo.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Date;

public class Model {

    private ObservableList<Account> accounts = FXCollections.observableArrayList();

    public void populate() {
        this.accounts.add(new Account("jeanmichelcrapaudensisa@gmail.com", "azerty12345!P"));
        //this.accounts.add(new Account("othman.mo@uha.fr", "azerty"));

        for(Account account : this.accounts) {
            account.getMessages().add(new Message("a@b.com", "Ceci est un mail de test", "<h1>My First Heading</h1>\n" + "<p>My first paragraph.</p>\n", new Date()));
            account.getMessages().add(new Message("c@d.com", "Ceci est un mail de test", "<h1>My First Heading</h1>\n" + "<p>My first paragraph.</p>\n", new Date()));
            account.getMessages().add(new Message("e@f.com", "Ceci est un mail de test", "<h1>My First Heading</h1>\n" + "<p>My first paragraph.</p>\n", new Date()));
        }
    }

    public ObservableList<Account> getAccounts() {
        return accounts;
    }
}
