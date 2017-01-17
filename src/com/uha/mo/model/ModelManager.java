package com.uha.mo.model;

import org.jasypt.util.text.BasicTextEncryptor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by othman on 25/12/2016.
 */
public class ModelManager {

    private static ModelManager ourInstance = new ModelManager();
    private BasicTextEncryptor encryptor = new BasicTextEncryptor();
    private String encryptionKey = "AZERTY123456789";
    public static ModelManager getInstance() {
        return ourInstance;
    }

    private ModelManager() {
        encryptor.setPassword(encryptionKey);
    }

    public void addAccount(Account account) {

        try {
            if(account instanceof GmailAccount) {

                Document xml = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File("accounts.xml"));
                Node root = xml.getDocumentElement();

                Element newAccount = xml.createElement("Account");
                newAccount.setAttribute("type", "gmail");
                newAccount.setAttribute("address", account.getMailAddress());
                newAccount.setAttribute("password", encryptor.encrypt(account.getPassword()));
                newAccount.setAttribute("name", account.getName());
                newAccount.setAttribute("period", String.valueOf(account.getSyncPeriod()));
                newAccount.setAttribute("notifications", String.valueOf(account.isNotifications()));

                root.appendChild(newAccount);

                Transformer tr = TransformerFactory.newInstance().newTransformer();
                tr.setOutputProperty(OutputKeys.INDENT, "yes");
                tr.setOutputProperty(OutputKeys.METHOD, "xml");
                tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
                tr.transform(new DOMSource(xml), new StreamResult(new FileOutputStream("accounts.xml")));
            }
            else if(account instanceof YahooAccount) {

                Document xml = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File("accounts.xml"));
                Node root = xml.getDocumentElement();

                Element newAccount = xml.createElement("Account");
                newAccount.setAttribute("type", "yahoo");
                newAccount.setAttribute("address", account.getMailAddress());
                newAccount.setAttribute("password", encryptor.encrypt(account.getPassword()));
                newAccount.setAttribute("name", account.getName());
                newAccount.setAttribute("period", String.valueOf(account.getSyncPeriod()));
                newAccount.setAttribute("notifications", String.valueOf(account.isNotifications()));

                root.appendChild(newAccount);

                Transformer tr = TransformerFactory.newInstance().newTransformer();
                tr.setOutputProperty(OutputKeys.INDENT, "yes");
                tr.setOutputProperty(OutputKeys.METHOD, "xml");
                tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
                tr.transform(new DOMSource(xml), new StreamResult(new FileOutputStream("accounts.xml")));
            }
            else if(account instanceof CustomAccount) {
                Document xml = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File("accounts.xml"));
                Node rootXML = xml.getDocumentElement();

                Element newAccount = xml.createElement("Account");
                newAccount.setAttribute("type", "custom");
                newAccount.setAttribute("address", account.getMailAddress());
                newAccount.setAttribute("password", encryptor.encrypt(account.getPassword()));

                newAccount.setAttribute("smtpHost", ((CustomAccount) account).getSMTP_HOST());
                newAccount.setAttribute("smtpPort", ((CustomAccount) account).getSMTP_PORT());
                newAccount.setAttribute("getProtocol", ((CustomAccount) account).getGET_PROTOCOL());
                newAccount.setAttribute("getHost", ((CustomAccount) account).getGET_PROTOCOL_HOST());
                newAccount.setAttribute("getPort", ((CustomAccount) account).getGET_PROTOCOL_PORT());

                newAccount.setAttribute("name", account.getName());
                newAccount.setAttribute("period", String.valueOf(account.getSyncPeriod()));
                newAccount.setAttribute("notifications", String.valueOf(account.isNotifications()));

                rootXML.appendChild(newAccount);

                Transformer tr = TransformerFactory.newInstance().newTransformer();
                tr.setOutputProperty(OutputKeys.INDENT, "yes");
                tr.setOutputProperty(OutputKeys.METHOD, "xml");
                tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
                tr.transform(new DOMSource(xml), new StreamResult(new FileOutputStream("accounts.xml")));
            }

        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteAccount(Account account) {
        try {
            Document xml = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File("accounts.xml"));
            Node root = xml.getDocumentElement();

            for (int i = 0; i < root.getChildNodes().getLength(); i++) {
                Node accountNode = root.getChildNodes().item(i);
                if (accountNode.getNodeType() == Node.ELEMENT_NODE) {
                    if (((Element) accountNode).getAttribute("address").equals(account.getMailAddress())) {
                        root.removeChild(accountNode);

                        Transformer tr = TransformerFactory.newInstance().newTransformer();
                        tr.setOutputProperty(OutputKeys.INDENT, "yes");
                        tr.setOutputProperty(OutputKeys.METHOD, "xml");
                        tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                        tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
                        tr.transform(new DOMSource(xml), new StreamResult(new FileOutputStream("accounts.xml")));
                    }
                }
            }

        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    public void editAccount(Account oldAccount, Account newAccount) {
        try {
            Document xml = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File("accounts.xml"));
            Node rootXML = xml.getDocumentElement();

            if(oldAccount instanceof GmailAccount || oldAccount instanceof YahooAccount) {
                for (int i = 0; i < rootXML.getChildNodes().getLength(); i++) {
                    Node accountNode = rootXML.getChildNodes().item(i);
                    if (accountNode.getNodeType() == Node.ELEMENT_NODE) {
                        if (((Element) accountNode).getAttribute("address").equals(oldAccount.getMailAddress())) {

                            ((Element) accountNode).setAttribute("address", newAccount.getMailAddress());
                            ((Element) accountNode).setAttribute("password", encryptor.encrypt(newAccount.getPassword()));
                            ((Element) accountNode).setAttribute("name", newAccount.getName());
                            ((Element) accountNode).setAttribute("period", String.valueOf(newAccount.getSyncPeriod()));
                            ((Element) accountNode).setAttribute("notifications", String.valueOf(newAccount.isNotifications()));
                        }
                    }
                }
            }
            else if(oldAccount instanceof CustomAccount) {
                for (int i = 0; i < rootXML.getChildNodes().getLength(); i++) {
                    Node accountNode = rootXML.getChildNodes().item(i);
                    if (accountNode.getNodeType() == Node.ELEMENT_NODE) {
                        if (((Element) accountNode).getAttribute("address").equals(oldAccount.getMailAddress())) {

                            ((Element) accountNode).setAttribute("address", newAccount.getMailAddress());
                            ((Element) accountNode).setAttribute("password", encryptor.encrypt(newAccount.getPassword()));
                            ((Element) accountNode).setAttribute("name", newAccount.getName());

                            ((Element) accountNode).setAttribute("smtpHost", ((CustomAccount)newAccount).getSMTP_HOST());
                            ((Element) accountNode).setAttribute("smtpPort", ((CustomAccount)newAccount).getSMTP_PORT());
                            ((Element) accountNode).setAttribute("getProtocol", ((CustomAccount)newAccount).getGET_PROTOCOL());
                            ((Element) accountNode).setAttribute("getHost", ((CustomAccount)newAccount).getGET_PROTOCOL_HOST());
                            ((Element) accountNode).setAttribute("getPort", ((CustomAccount)newAccount).getGET_PROTOCOL_PORT());

                            ((Element) accountNode).setAttribute("period", String.valueOf(newAccount.getSyncPeriod()));
                            ((Element) accountNode).setAttribute("notifications", String.valueOf(newAccount.isNotifications()));

                        }
                    }
                }
            }

            Transformer tr = TransformerFactory.newInstance().newTransformer();
            tr.setOutputProperty(OutputKeys.INDENT, "yes");
            tr.setOutputProperty(OutputKeys.METHOD, "xml");
            tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            tr.transform(new DOMSource(xml), new StreamResult(new FileOutputStream("accounts.xml")));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Account> loadAccounts() {

        try {
            ArrayList<Account> result = new ArrayList<>();

            Document xml = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File("accounts.xml"));
            Node root = xml.getDocumentElement();

            for(int i = 0; i < root.getChildNodes().getLength(); i++) {
                Node accountNode = root.getChildNodes().item(i);
                if(accountNode.getNodeType() == Node.ELEMENT_NODE) {
                    String type = ((Element)accountNode).getAttribute("type");
                    String address = ((Element)accountNode).getAttribute("address");
                    String password = encryptor.decrypt(((Element)accountNode).getAttribute("password"));

                    String name = address;
                    if(((Element)accountNode).hasAttribute("name")) {
                        name = ((Element)accountNode).getAttribute("name");
                    }

                    boolean notifications = true;
                    if(((Element)accountNode).hasAttribute("notifications")) {
                        notifications = Boolean.valueOf(((Element)accountNode).getAttribute("notifications"));
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

                            result.add(gmailAccount);
                            break;
                        case "yahoo":
                            YahooAccount yahooAccount = new YahooAccount(address, password);
                            yahooAccount.setNotifications(notifications);
                            yahooAccount.setSyncPeriod(period);
                            yahooAccount.setName(name);

                            result.add(yahooAccount);
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
                                case "POP3":
                                    customAccount.setGET_PROTOCOL("POP3");
                                    customAccount.setGET_PROTOCOL_HOST(((Element)accountNode).getAttribute("getHost"));
                                    customAccount.setGET_PROTOCOL_PORT(((Element)accountNode).getAttribute("getPort"));
                            }

                            customAccount.setNotifications(notifications);
                            customAccount.setSyncPeriod(period);
                            customAccount.setName(name);

                            result.add(customAccount);
                            break;
                    }
                }
            }

            return result;

        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }
}
