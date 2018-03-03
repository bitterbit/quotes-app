package com.gtr.quotes.tracking;

import android.util.Log;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by gal on 3/3/18.
 */

public class ParseAgent implements IAnalyticsAgent {


    @Override
    public void sendEvent(IEvent event) {
        if (event.getName().equals("Viewed Quote")){
            handleView((QuoteViewEvent) event);
        } else if (event.getName().equals("Liked Quote")){
            handleLike((QuoteLikeEvent) event);
        }
    }

    private void handleView(QuoteViewEvent event){
        Log.i("Quotes", "parse analytics, quote view");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Quote");
        query.whereEqualTo("objectId", event.getQuoteId());
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (object == null){
                    return;
                }
                if (e != null){
                    Log.e("Quotes", "Error while fetching quote", e);
                    return;
                }
                object.increment("views");
                object.saveEventually();
            }
        });
    }

    private void handleLike(QuoteLikeEvent event){
        Log.i("Quotes", "parse analytics, quote like");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Quote");
        query.whereEqualTo("objectId", event.getQuoteId());
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (object == null){
                    return;
                }
                if (e != null){
                    Log.e("Quotes", "Error while fetching quote", e);
                    return;
                }
                object.increment("likes");
                object.saveEventually();
            }
        });
    }

    @Override
    public void updateUserValue(String name, String value) {

    }

    @Override
    public void updateUserValue(String name, int value) {

    }

    @Override
    public void flush() {

    }

    @Override
    public void dispose() {

    }
}
