package com.gtr.quotes.tracking;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Gal on 6/8/2014.
 */
public class FavoritesDrawerOpenEvent implements IEvent {

    private double duration;

    public FavoritesDrawerOpenEvent(double duration) {
        this.duration = duration;
    }

    @Override
    public String getName() {
        return "Drawer Opened Event";
    }

    @Override
    public JSONObject toJsonObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("Duration", duration);
        } catch (JSONException e) {
        }

        return obj;
    }
}
