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

        try {
            Document xml = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File("src/com/uha/mo/model/accounts.xml"));
            Node root = xml.getDocumentElement();

            for(int i = 0; i < root.getChildNodes().getLength(); i++) {
                Node accountNode = root.getChildNodes().item(i);
                if(accountNode.getNodeType() == Node.ELEMENT_NODE) {
                    String type = ((Element)accountNode).getAttribute("type");
                    String address = ((Element)accountNode).getAttribute("address");
                    String password = ((Element)accountNode).getAttribute("password");

                    switch (type) {
                        case "gmail":
                            this.accounts.add(new GmailAccount(address, password));

                            break;
                        case "yahoo":
                            this.accounts.add(new YahooAccount(address, password));
                            break;
                        case "custom":
                            CustomAccount customAccount = new CustomAccount(address, password);

                            customAccount.setSMTP_HOST(((Element)accountNode).getAttribute("smtpHost"));
                            customAccount.setSMTP_PORT(((Element)accountNode).getAttribute("smtpPort"));
                            customAccount.setIMAP_HOST(((Element)accountNode).getAttribute("imapHost"));
                            customAccount.setIMAP_HOST(((Element)accountNode).getAttribute("imapPort"));

                            this.accounts.add(customAccount);
                            break;
                    }
                }
            }

        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Account> getAccounts() {
        return this.accounts;
    }
}
