package com.uha.mo;

import com.uha.mo.model.Account;
import com.uha.mo.model.GmailAccount;
import com.uha.mo.model.Message;
import com.uha.mo.model.YahooAccount;
import com.uha.mo.notifications.Notification;
import com.uha.mo.utils.AsyncTask;
import javafx.application.Platform;
import javafx.stage.Stage;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by othman on 16/01/2017.
 */
public class Checker {

    private Stage mainStage;

    public Checker(Stage mainStage, Account account) {

        this.mainStage = mainStage;

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                new AccountsChecker().execute(account);
            }
        }, account.getSyncPeriod() + new Random().nextInt(1000), account.getSyncPeriod());
    }

    private class AccountsChecker extends AsyncTask<Account, ArrayList<Message>> {

        @Override
        protected ArrayList<Message> doInBackground() {
            if(params[0] instanceof GmailAccount) {
                return new GmailChecker(params[0]).getMessages();
            }
            else if(params[0] instanceof YahooAccount) {
                try {
                    return new YahooChecker(params[0]).getMessages();
                } catch (MessagingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return new ArrayList<>();
        }

        @Override
        protected void onPostExecute(ArrayList<Message> messages) {
            if(messages.size() > 0) {
                new Notification(mainStage, params[0], messages.size());
            }
        }
    }
}
