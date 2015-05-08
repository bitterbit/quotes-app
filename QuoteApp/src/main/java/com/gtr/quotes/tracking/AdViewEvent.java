package com.gtr.quotes.tracking;

import org.json.JSONException;
import org.json.JSONObject;

public class AdViewEvent implements IEvent {

    private double duration;

    public AdViewEvent(double duration) {
        this.duration = duration;
    }

    @Override
    public String getName() {
        return "Viewed Ad";
    }

    @Override
    public JSONObject toJsonObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("Duration", (int) duration / 1000);
        } catch (JSONException e) {
        }

        return obj;
    }
}
