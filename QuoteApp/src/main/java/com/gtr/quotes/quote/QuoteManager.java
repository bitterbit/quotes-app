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
    private Iterator<LazyParseObjectHolder<Quote>> quoteIterator;
    private FavoriteQuotesManager favManager;
    private AnalyticsHandler analyticsHandler;

    public QuoteManager(Activity activity, AnalyticsHandler analyticsHandler) {
        ParseQuery<Quote> query = new ParseQuery<Quote>(Quote.class);
        query.orderByDescending("random");

        this.analyticsHandler = analyticsHandler;
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
