package com.uha.mo.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Date;

public class Model {

    private ObservableList<Account> accounts = FXCollections.observableArrayList();

    public ObservableList<Account> getAccounts() {
        return accounts;
    }
}
