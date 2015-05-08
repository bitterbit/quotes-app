package com.gtr.quotes.tracking;

import com.gtr.quotes.quote.Quote;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Gal on 6/8/2014.
 */
public class ShareEvent implements IEvent {

    private Quote sharedQuote;

    public ShareEvent(Quote sharedQuote) {
        this.sharedQuote = sharedQuote;
    }

    @Override
    public String getName() {
        return "Shared Quote";
    }

    @Override
    public JSONObject toJsonObject() {
        try {
            JSONObject obj = new JSONObject();
            obj.put("Quote ID", sharedQuote.getId());
            return obj;
        } catch (JSONException e) {
        }

        return null;
    }
}
