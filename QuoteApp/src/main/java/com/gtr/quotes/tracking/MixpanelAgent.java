package com.gtr.quotes.tracking;

import android.content.Context;
import android.util.Log;

import com.gtr.quotes.util.ID;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;

public class MixpanelAgent implements IAnalyticsAgent {

    private static final String TOKEN = "3d2214bccd307a490ef7e38e3d460202";

    private MixpanelAPI analytics;

    public MixpanelAgent(Context context) {
        analytics = MixpanelAPI.getInstance(context, TOKEN);
        String userID = ID.getUniquePsuedoID();
        analytics.identify(userID);
        analytics.getPeople().identify(userID);
    }

    @Override
    public void sendEvent(IEvent event) {
        analytics.track(event.getName(), event.toJsonObject());
        updateUserCounters(event.getName());
        Log.d("Quotes", "Sending event: " + event.getName());
    }

    @Override
    public void updateUserValue(String name, String value) {
        JSONObject obj = new JSONObject();
        try {
            obj.put(name, value);
        } catch (JSONException e) {
        }
        analytics.getPeople().set(obj);
    }

    @Override
    public void updateUserValue(String name, int value) {
        JSONObject obj = new JSONObject();
        try {
            obj.put(name, value);
        } catch (JSONException e) {
        }
        analytics.getPeople().set(obj);
    }

    @Override
    public void flush() {
        analytics.flush();
    }

    @Override
    public void dispose() {
        analytics.flush();
    }

    /**
     * Count how much times each event occurred for each person
     *
     * @param eventName the event that occurred
     */
    private void updateUserCounters(String eventName) {
        analytics.getPeople().increment(eventName, 1);
    }
}
