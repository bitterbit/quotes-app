package com.gtr.quotes.quote;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.gtr.quotes.util.Consts;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

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
        public void onStatusChange(Status status);
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

            try {
                double risk = 0.99;
                HttpClient httpclient = new DefaultHttpClient();
                String url = params[0] + "?page=SmartQuote&risk=" + risk;
                HttpResponse response = httpclient.execute(new HttpGet(url)); //TODO: calculate the risk taken

                StatusLine statusLine = response.getStatusLine();

                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    out.close();
                    response.getEntity().writeTo(out);
                    String responseString = out.toString();

                    Gson gson = new Gson();
                    Quote quote = gson.fromJson(responseString, Quote.class);
                    quote.setRiskTaken(risk);

                    setArtistIconUrl(quote.getArtistIconUrl());
                    setAuthor(quote.getAuthor());
                    setText(quote.getText());
                    setId(quote.getId());

                    if (id.equals(EMPTY_MD5))
                        status = Quote.Status.ERROR;
                    else
                        status = Quote.Status.DONE_SUCCESSFUL;
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            // If we got here something bad happened
            status = Quote.Status.ERROR;
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
