package com.uha.mo.utils;

import javafx.application.Platform;

/**
 * Created by Othman on 29/11/2016.
 */
public abstract class AsyncTask<Params, Result> {

    protected abstract Result doInBackground();
    protected abstract void onPostExecute(Result result);
    protected Params[] params;

    public void execute(Params... params) {
        this.params = params;

        new Thread(new Runnable() {
            @Override
            public void run() {

                Result result = doInBackground();

                //Platform.runLater is a javafx code that executes onPost in Main Thread.
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        onPostExecute(result);
                    }
                });
            }
        }).start();
    }
}
