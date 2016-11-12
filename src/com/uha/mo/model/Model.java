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
            account.getMails().add(new Mail("a@b.com", "Ceci est un mail de test", "<h1>My First Heading</h1>\n" + "<p>My first paragraph.</p>\n", new Date()));
            account.getMails().add(new Mail("c@d.com", "Ceci est un mail de test", "<h1>My First Heading</h1>\n" + "<p>My first paragraph.</p>\n", new Date()));
            account.getMails().add(new Mail("e@f.com", "Ceci est un mail de test", "<h1>My First Heading</h1>\n" + "<p>My first paragraph.</p>\n", new Date()));
        }
    }

    public ObservableList<Account> getAccounts() {
        return accounts;
    }
}
