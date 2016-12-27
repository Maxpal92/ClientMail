package com.uha.mo.utils;

import com.uha.mo.model.Account;
import com.uha.mo.model.GmailAccount;
import com.uha.mo.model.YahooAccount;
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

/**
 * Created by othman on 25/12/2016.
 */
public class ModelManager {

    private static ModelManager ourInstance = new ModelManager();

    public static ModelManager getInstance() {
        return ourInstance;
    }

    private ModelManager() {}

    public void addAccount(Account account) {

        try {
            if(account instanceof GmailAccount) {
                Document xml = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File("src/com/uha/mo/model/accounts.xml"));
                Node root = xml.getDocumentElement();

                Element newAccount = xml.createElement("Account");
                newAccount.setAttribute("type", "gmail");
                newAccount.setAttribute("address", account.getMailAddress());
                newAccount.setAttribute("password", account.getPassword());
                newAccount.setAttribute("name", account.getName());
                newAccount.setAttribute("period", String.valueOf(account.getSyncPeriod()));
                newAccount.setAttribute("notifications", String.valueOf(account.isNotifications()));

                root.appendChild(newAccount);

                Transformer tr = TransformerFactory.newInstance().newTransformer();
                tr.setOutputProperty(OutputKeys.INDENT, "yes");
                tr.setOutputProperty(OutputKeys.METHOD, "xml");
                tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
                tr.transform(new DOMSource(xml), new StreamResult(new FileOutputStream("src/com/uha/mo/model/accounts.xml")));
            }
            else if(account instanceof YahooAccount) {

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
            Document xml = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File("src/com/uha/mo/model/accounts.xml"));
            Node root = xml.getDocumentElement();

            for(int i = 0; i < root.getChildNodes().getLength(); i++) {
                Node accountNode = root.getChildNodes().item(i);
                if (accountNode.getNodeType() == Node.ELEMENT_NODE) {
                    if(((Element)accountNode).getAttribute("address").equals(account.getMailAddress())) {
                        root.removeChild(accountNode);

                        Transformer tr = TransformerFactory.newInstance().newTransformer();
                        tr.setOutputProperty(OutputKeys.INDENT, "yes");
                        tr.setOutputProperty(OutputKeys.METHOD, "xml");
                        tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                        tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
                        tr.transform(new DOMSource(xml), new StreamResult(new FileOutputStream("src/com/uha/mo/model/accounts.xml")));
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
}
