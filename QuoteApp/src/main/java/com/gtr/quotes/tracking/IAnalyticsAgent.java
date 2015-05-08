package com.gtr.quotes.tracking;

/**
 * Created by Gal on 6/7/2014.
 */
public interface IAnalyticsAgent {
    public void sendEvent(IEvent event);

    public void updateUserValue(String name, String value);

    public void updateUserValue(String name, int value);

    public void flush();

    public void dispose();

}
