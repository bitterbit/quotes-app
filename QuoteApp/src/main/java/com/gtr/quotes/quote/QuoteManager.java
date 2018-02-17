package com.gtr.quotes.quote;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.galtashma.lazyparse.LazyList;
import com.galtashma.lazyparse.LazyParseObjectHolder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gtr.quotes.R;
import com.gtr.quotes.tracking.AnalyticsHandler;
import com.gtr.quotes.tracking.LikeEvent;
import com.gtr.quotes.viewwrappers.CardsUIWrapper;
import com.parse.ParseQuery;

import java.util.Iterator;
import java.util.LinkedList;

public class QuoteManager {
    private static final String PREF_KEY = "pref_key";
    // private static final String CACHE_LIST_KEY = "cache_list_key";
    private SharedPreferences sharedPreferences;

    private Iterator<LazyParseObjectHolder<Quote>> quoteIterator;

    private FavoriteQuotesManager favManager;
    private AnalyticsHandler analyticsHandler;

    // TODO: add: set cache size to lazy parse lib
    private static final int CACHE_SIZE = 50;

    // TODO: control step size in lazy parse lib
    private static final int WAITING_QUEUE_MIN_SIZE = 5;

    public QuoteManager(Activity activity, AnalyticsHandler analyticsHandler) {
        ParseQuery<Quote> query = new ParseQuery<Quote>(Quote.class);
        query.orderByAscending("random");

        this.analyticsHandler = analyticsHandler;
        this.sharedPreferences = activity.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);
        LazyList<Quote> quotes = new LazyList<>(query);
        this.quoteIterator = quotes.iterator();

        CardsUIWrapper cardUI = (CardsUIWrapper) activity.findViewById(R.id.cardsview);
        this.favManager = new FavoriteQuotesManager(activity, cardUI);
    }

    /**
     * Get a quote to be displayed
     *
     * @return returns a quote
     */
    public LazyParseObjectHolder<Quote> getQuote() {
        return quoteIterator.next();
    }


    public void refreshFavView() {
        favManager.refreshView();
    }

    /**
     * Save favorites localy
     */
    public void saveWaitingQuotes() {
        // TODO implement in LazyParse library
    }

    private LinkedList<Quote> loadWaitingQuotes() {
        // TODO implement in LazyParse library
        return null;
    }

    private LinkedList<Quote> initWithStaticQuotes(){
        // TODO implement addOfflineData in LazyParse library
        LinkedList<Quote> staticQuotes = new LinkedList<Quote>();
        staticQuotes.add(Quote.createStaticQuote("I am convinced that He (God) does not play dice.", "Albert Einstein","http://upload.wikimedia.org/wikipedia/commons/thumb/6/66/Einstein_1921_by_F_Schmutzer.jpg/228px-Einstein_1921_by_F_Schmutzer.jpg"));
        staticQuotes.add(Quote.createStaticQuote("Once you make a decision, the universe conspires to make it happen.", "Ralph Waldo Emerson","http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/thumb\\/d\\/d5\\/Ralph_Waldo_Emerson_ca1857_retouched.jpg\\/187px-Ralph_Waldo_Emerson_ca1857_retouched.jpg"));
        return staticQuotes;
    }

    public void quoteLikeClicked(Quote quote) {
        if (favManager.isQuoteFavorite(quote)) {
            favManager.removeFromFavorites(quote);
        } else {
            favManager.addToFavorites(quote);
            analyticsHandler.sendEvent(new LikeEvent(quote));
        }
    }

    public int getFavoriteQuotesCount() {
        return favManager.getFavoritesCount();
    }

    public boolean isQuoteFavorite(Quote quote) {
        return favManager.isQuoteFavorite(quote);
    }

}
