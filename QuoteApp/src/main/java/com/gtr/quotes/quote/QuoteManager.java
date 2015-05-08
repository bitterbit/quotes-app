package com.gtr.quotes.quote;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gtr.quotes.R;
import com.gtr.quotes.tracking.AnalyticsHandler;
import com.gtr.quotes.tracking.LikeEvent;
import com.gtr.quotes.viewwrappers.CardsUIWrapper;

import java.util.LinkedList;
import java.util.List;

public class QuoteManager {
    private static final String PREF_KEY = "pref_key";
    private static final String CACHE_LIST_KEY = "cache_list_key";

    private SharedPreferences sharedPreferences;

    private List<Quote> waitingQuotes;
    private FavoriteQuotesManager favManager;
    private AnalyticsHandler analyticsHandler;

    private static final int CACHE_SIZE = 50;

    public QuoteManager(Activity activity, AnalyticsHandler analyticsHandler) {
        this.analyticsHandler = analyticsHandler;
        sharedPreferences = activity.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);
        waitingQuotes = loadWaitingQuotes();

        // Load default quotes if none were downloaded yet
        if (waitingQuotes == null) {
            waitingQuotes = new LinkedList<Quote>();
            waitingQuotes.add(new Quote("I am convinced that He (God) does not play dice.", "Albert Einstein", "15a21f2e2b8ff332495078ddb89b3f08", "http://upload.wikimedia.org/wikipedia/commons/thumb/6/66/Einstein_1921_by_F_Schmutzer.jpg/228px-Einstein_1921_by_F_Schmutzer.jpg"));
            waitingQuotes.add(new Quote("Once you make a decision, the universe conspires to make it happen.", "Ralph Waldo Emerson", "22a7e67e223fc0dd29a7613836fcd5af", "http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/thumb\\/d\\/d5\\/Ralph_Waldo_Emerson_ca1857_retouched.jpg\\/187px-Ralph_Waldo_Emerson_ca1857_retouched.jpg"));
        }

        CardsUIWrapper cardUI = (CardsUIWrapper) activity.findViewById(R.id.cardsview);
        favManager = new FavoriteQuotesManager(activity, cardUI);

        for (int i = waitingQuotes.size(); i < CACHE_SIZE; i++) {
            waitingQuotes.add(new AsyncQuote());
        }
    }

    /**
     * Get a quote to be displayed
     *
     * @return returns a quote
     */
    public Quote getQuote() {

        // Add a quote to the waiting quotes
        waitingQuotes.add(new AsyncQuote());

        // If no quotes in waiting quotes, return a new async one
        if (waitingQuotes.isEmpty()) {
            return new AsyncQuote();
        }

        // Else return the oldest waiting quote that was loaded correctly
        Quote returnedQuote = waitingQuotes.get(0);
        waitingQuotes.remove(0);
        while (waitingQuotes.size() > 1 && returnedQuote.getStatus() == Quote.Status.ERROR) {
            returnedQuote = waitingQuotes.get(0);
            waitingQuotes.remove(0);
        }

        return returnedQuote;
    }


    public void refreshFavView() {
        favManager.refreshView();
    }

    /**
     * Save favorites localy
     */
    public void saveWaitingQuotes() {
        Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();

        // Filter quotes, save only ones that are ready
        List<Quote> quotesToBeSaved = new LinkedList<Quote>();
        for (Quote quote : waitingQuotes) {
            if (quote.getStatus() == Quote.Status.DONE_SUCCESSFUL)
                quotesToBeSaved.add(quote);
        }

        editor.putString(CACHE_LIST_KEY, gson.toJson(quotesToBeSaved));
        editor.commit();
    }

    private LinkedList<Quote> loadWaitingQuotes() {
        String favJson = sharedPreferences.getString(CACHE_LIST_KEY, "");
        if (!favJson.equals("")) {
            Gson gson = new Gson();
            return gson.fromJson(favJson, new TypeToken<LinkedList<Quote>>() {
            }.getType());
        }
        return null;
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
