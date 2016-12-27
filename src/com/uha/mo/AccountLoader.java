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

                    String name = address;
                    if(((Element)accountNode).hasAttribute("name")) {
                        name = ((Element)accountNode).getAttribute("name");
                    }

                    boolean notifications = true;
                    if(((Element)accountNode).hasAttribute("notifications")) {
                        if(((Element)accountNode).getAttribute("notifications").equals("yes"))
                            notifications = true;
                        else
                            notifications = false;
                    }

                    int period = 900000;
                    if(((Element)accountNode).hasAttribute("period")) {
                        period = Integer.parseInt(((Element)accountNode).getAttribute("period"));
                    }

                    switch (type) {
                        case "gmail":
                            GmailAccount gmailAccount = new GmailAccount(address, password);
                            gmailAccount.setNotifications(notifications);
                            gmailAccount.setSyncPeriod(period);
                            gmailAccount.setName(name);

                            this.accounts.add(gmailAccount);
                            break;
                        case "yahoo":
                            YahooAccount yahooAccount = new YahooAccount(address, password);
                            yahooAccount.setNotifications(notifications);
                            yahooAccount.setSyncPeriod(period);
                            yahooAccount.setName(name);

                            this.accounts.add(yahooAccount);
                            break;
                        case "custom":
                            CustomAccount customAccount = new CustomAccount(address, password);

                            customAccount.setSMTP_HOST(((Element)accountNode).getAttribute("smtpHost"));
                            customAccount.setSMTP_PORT(((Element)accountNode).getAttribute("smtpPort"));

                            String getProtocol = ((Element)accountNode).getAttribute("getProtocol");
                            switch (getProtocol) {
                                case "IMAP":
                                    customAccount.setGET_PROTOCOL("IMAP");
                                    customAccount.setGET_PROTOCOL_HOST(((Element)accountNode).getAttribute("getHost"));
                                    customAccount.setGET_PROTOCOL_PORT(((Element)accountNode).getAttribute("getPort"));

                            }

                            customAccount.setNotifications(notifications);
                            customAccount.setSyncPeriod(period);
                            customAccount.setName(name);

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
