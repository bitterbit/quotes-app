package com.gtr.quotes.viewwrappers;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.SystemClock;

import com.gtr.quotes.R;
import com.gtr.quotes.pager.QuotePageAdapter;
import com.gtr.quotes.pager.QuotePager;
import com.gtr.quotes.quote.Quote;
import com.gtr.quotes.quote.QuoteManager;
import com.gtr.quotes.tracking.AdViewEvent;
import com.gtr.quotes.tracking.AnalyticsHandler;
import com.gtr.quotes.tracking.IEvent;
import com.gtr.quotes.tracking.QuoteViewEvent;

public class QuotePagerWrapper {

    private static final int OFF_SCREEN_LIMIT = 2;
    private QuoteManager quoteManager;
    private ActionBarLayoutWrapper barWrapper;
    private QuotePageAdapter quotePageAdapter;
    private QuotePager quotePager;

    private AnalyticsHandler analyticsHandler = null;

    public QuotePagerWrapper(Activity activity, QuoteManager quoteManager, ActionBarLayoutWrapper barWrapper) {

        this.quoteManager = quoteManager;
        this.barWrapper = barWrapper;
        quotePageAdapter = new QuotePageAdapter(quoteManager);

        quotePager = (QuotePager) activity.findViewById(R.id.pager);
        quotePager.setActionBarWrapper(barWrapper);
        quotePager.setAnimationCacheEnabled(true);
        quotePager.setOffscreenPageLimit(OFF_SCREEN_LIMIT);
        quotePager.setAdapter(quotePageAdapter);

        quotePager.setListener(new QuotePagerListener());
    }

    public Quote getCurrentQuote() {
        return quotePager.getCurrentQuote();
    }

    public void setAnalyticsHandler(AnalyticsHandler analyticsHandler) {
        this.analyticsHandler = analyticsHandler;
    }

    private class QuotePagerListener implements QuotePager.Listener {

        private PageEventHandler pageEventHandler;

        public QuotePagerListener() {
            pageEventHandler = new PageEventHandler();
        }

        @Override
        public void onPageChange(Quote quote) {
            if (quoteManager.isQuoteFavorite(quote))
                barWrapper.setMenuLayout(R.menu.quotes_red_heart);
            else
                barWrapper.setMenuLayout(R.menu.activity_main);
            pageEventHandler.updatePageChange();
        }
    }

    /**
     * Inner class used to send event analytics about viewed pages
     */
    private class PageEventHandler {

        private long lastTick = 0;

        public PageEventHandler() {
            lastTick = SystemClock.uptimeMillis();
        }

        public void updatePageChange() {
            double duration = SystemClock.uptimeMillis() - lastTick;
            Quote quote = getCurrentQuote();
            if (isAdPage(quote))
                sendAdEvent(duration);
            else
                sendQuoteEvent(quote, duration);

            lastTick = SystemClock.uptimeMillis();
        }

        private void sendQuoteEvent(Quote quote, double duration) {
            IEvent event = new QuoteViewEvent(quote, duration, quoteManager.isQuoteFavorite(quote));
            if (analyticsHandler != null)
                analyticsHandler.sendEvent(event);
            // TODO: Send quote event
            // new ViewedQuote().execute(quote.getId());
        }

        private void sendAdEvent(double duration) {
            IEvent event = new AdViewEvent(duration);
            if (analyticsHandler != null)
                analyticsHandler.sendEvent(event);
        }

        private boolean isAdPage(Quote quote) {
            return quote.getId().equals("AD");
        }
    }


}
