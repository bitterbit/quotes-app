package com.gtr.quotes.tracking;

import com.gtr.quotes.quote.Quote;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Gal on 6/8/2014.
 */
public class QuoteViewEvent implements IEvent {

    private Quote quote;
    private double duration; // Duration the quote was viewed in milliseconds
    private boolean isFavorite;

    public QuoteViewEvent(Quote quote, double duration, boolean isFavorite) {
        this.quote = quote;
        this.duration = duration;
        this.isFavorite = isFavorite;
    }

    public QuoteViewEvent(Quote quote, double duration) {
        this.quote = quote;
        this.duration = duration;
        this.isFavorite = false;
    }

    @Override
    public String getName() {
        return "Viewed Quote";
    }

    @Override
    public JSONObject toJsonObject() {
        JSONObject obj = new JSONObject();

        try {
            obj.put("Quote ID", quote.getId());
            obj.put("Duration", (int) duration / 1000);
            //obj.put("Quote Status", getStatusString(quote.getStatus()));
            obj.put("Is Favorite", isFavorite);
            //obj.put("Quote Risk", quote.getRiskTaken());
        } catch (JSONException e) {
        }

        return obj;
    }

    private String getStatusString(Quote.Status status) {
        if (status == Quote.Status.DONE_SUCCESSFUL)
            return "SUCCESS";
        if (status == Quote.Status.ERROR)
            return "ERROR";
        if (status == Quote.Status.LOADING)
            return "LOADING";
        return "ERROR";
    }
}
