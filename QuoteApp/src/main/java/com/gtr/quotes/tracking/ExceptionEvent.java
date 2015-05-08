package com.gtr.quotes.tracking;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Gal on 6/8/2014.
 */
public class ExceptionEvent implements IEvent {

    private Exception exception;

    public ExceptionEvent(Exception e) {
        this.exception = e;
    }

    @Override
    public String getName() {
        return "Exception Event";
    }

    @Override
    public JSONObject toJsonObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("Message", exception.getMessage());
            obj.put("Cause", exception.getCause());
            obj.put("Stack Trace", exception.getStackTrace());
        } catch (JSONException e) {
        }

        return obj;
    }
}
