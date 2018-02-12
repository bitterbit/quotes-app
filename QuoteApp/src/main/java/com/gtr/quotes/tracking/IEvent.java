package com.gtr.quotes.tracking;

import org.json.JSONObject;

public interface IEvent {

    String getName();

    JSONObject toJsonObject();
}
