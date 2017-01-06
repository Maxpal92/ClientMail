package com.uha.mo;


import com.uha.mo.model.Account;
import com.uha.mo.model.CustomAccount;
import com.uha.mo.model.GmailAccount;
import com.uha.mo.model.YahooAccount;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class AccountLoader {

    private ArrayList<Account> accounts = new ArrayList<>();

    public AccountLoader() {


    }

    public ArrayList<Account> getAccounts() {
        return this.accounts;
    }
}
