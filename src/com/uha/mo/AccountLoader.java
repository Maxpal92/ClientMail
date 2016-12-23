package com.uha.mo;


import com.uha.mo.model.Account;
import com.uha.mo.model.CustomAccount;
import com.uha.mo.model.GmailAccount;
import com.uha.mo.model.YahooAccount;
import org.jasypt.util.text.BasicTextEncryptor;
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

        BasicTextEncryptor TextEncryptor = new BasicTextEncryptor();
        TextEncryptor.setPassword("glhfstfureportNamhtoThx");

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
                            this.accounts.add(new GmailAccount(address, TextEncryptor.decrypt(password)));

                            break;
                        case "yahoo":
                            this.accounts.add(new YahooAccount(address,  TextEncryptor.decrypt(password)));
                            break;
                        case "custom":
                            CustomAccount customAccount = new CustomAccount(address,  TextEncryptor.decrypt(password));

                            customAccount.setSMTP_HOST(((Element)accountNode).getAttribute("smtpHost"));
                            customAccount.setSMTP_PORT(((Element)accountNode).getAttribute("smtpPort"));

                            String getProtocol = ((Element)accountNode).getAttribute("getProtocol");
                            switch (getProtocol) {
                                case "IMAP":
                                    customAccount.setGET_PROTOCOL("IMAP");
                                    customAccount.setGET_PROTOCOL_HOST(((Element)accountNode).getAttribute("getHost"));
                                    customAccount.setGET_PROTOCOL_PORT(((Element)accountNode).getAttribute("getPort"));

                            }

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
