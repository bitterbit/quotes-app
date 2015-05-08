package com.gtr.quotes.tracking;

import com.gtr.quotes.quote.Quote;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Gal on 6/8/2014.
 */
public class LikeEvent implements IEvent {

    private Quote quote;

    public LikeEvent(Quote quote) {
        this.quote = quote;
    }

    @Override
    public String getName() {
        return "Liked Quote";
    }

    @Override
    public JSONObject toJsonObject() {
        JSONObject obj = new JSONObject();

        try {
            obj.put("Quote ID", quote.getId());
            obj.put("Quote Risk", 100);
        } catch (JSONException e) {
        }

        return obj;
    }
}
