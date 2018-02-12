package com.gtr.quotes.quote;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.gtr.quotes.util.Consts;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * This class represents a Quote that was not loaded yet, it downloads its content in it c'tor
 *
 * @author Gal
 */
public class AsyncQuote extends Quote {

    private final static String EMPTY_MD5 = "d41d8cd98f00b204e9800998ecf8427e";

    /**
     * When the quotes status changes, will be called
     *
     * @author Gal
     */
    public interface Listener {
        void onStatusChange(Status status);
    }

    private Listener listener = null;
    private Status status = Status.LOADING;
    private final String API_URL = Consts.API_URL;

    public AsyncQuote() {
        this.text = "";
        QuoteGetter quoteGetter = new QuoteGetter();
        quoteGetter.execute(API_URL);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public Status getStatus() {
        return status;
    }


    private class QuoteGetter extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {

            // TODO: get quote from server
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);

            if (listener != null)
                listener.onStatusChange(status);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            status = Quote.Status.LOADING;
        }
    }
}
