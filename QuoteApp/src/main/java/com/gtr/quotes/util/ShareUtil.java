package com.gtr.quotes.util;

import android.content.Context;
import android.content.Intent;

import com.gtr.quotes.quote.Quote;
import com.gtr.quotes.tracking.AnalyticsHandler;
import com.gtr.quotes.tracking.ShareEvent;

/**
 * Created by Gal on 6/8/2014.
 */
public class ShareUtil {

    private static ShareUtil instance = null;

    private ShareUtil() {
    }

    public static ShareUtil getInstance() {
        if (instance == null)
            instance = new ShareUtil();
        return instance;
    }

    private AnalyticsHandler analyticsHandler = null;

    public void setAnalyticsHandler(AnalyticsHandler analyticsHandler) {
        this.analyticsHandler = analyticsHandler;
    }

    public void shareQuote(Context context, Quote quote) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, quoteToString(quote));
        sendIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(sendIntent, "Send to: "));

        if (analyticsHandler != null) {
            analyticsHandler.sendEvent(new ShareEvent(quote));
        }
    }

    private String quoteToString(Quote quote) {
        return quote.getAuthor() + " -- " + '"' + quote.getText() + '"';
    }

    // TODO: make quote to image sharing
    private void quoteToImage() {
    }

}
