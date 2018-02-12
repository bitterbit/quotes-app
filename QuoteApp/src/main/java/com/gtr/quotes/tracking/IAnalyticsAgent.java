package com.gtr.quotes.tracking;

/**
 * Created by Gal on 6/7/2014.
 */
public interface IAnalyticsAgent {
    void sendEvent(IEvent event);

    void updateUserValue(String name, String value);

    void updateUserValue(String name, int value);

    void flush();

    void dispose();

}
