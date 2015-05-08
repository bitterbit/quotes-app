package com.gtr.quotes.tracking;

import org.json.JSONObject;

public interface IEvent {

    public String getName();

    public JSONObject toJsonObject();
}
