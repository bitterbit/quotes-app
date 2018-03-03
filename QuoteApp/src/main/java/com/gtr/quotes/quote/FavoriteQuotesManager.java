package com.gtr.quotes.quote;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.fima.cardsui.objects.Card;
import com.fima.cardsui.objects.Card.OnCardSwiped;
import com.gtr.quotes.util.Consts;
import com.gtr.quotes.views.CardFavoriteQuoteView;
import com.gtr.quotes.views.CardFavoriteQuoteView.DeleteListener;
import com.gtr.quotes.viewwrappers.CardsUIWrapper;
import com.jensdriller.libs.undobar.UndoBar;
import com.jensdriller.libs.undobar.UndoBar.Listener;

import java.util.ArrayList;
import java.util.List;

public class FavoriteQuotesManager {

    private final String API_URL = Consts.API_URL;

    private CardsUIWrapper favoritesCardUi;

    private List<Quote> favorites = null;
    private String allTimesFavIds = "";

    private SharedPreferences sharedPref;

    private Activity activity;

    private static final String PREF_KEY = "pref_key";
    private static final String FAV_LIST_KEY = "fav_list_key";
    private static final String ALL_TIMES_FAV_QUOTES_KEY = "all_times_fav_quotes_key";


    public FavoriteQuotesManager(Activity context, CardsUIWrapper favoritesCardUi) {
        this.favoritesCardUi = favoritesCardUi;

        this.activity = context;

        sharedPref = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);
        loadSavedFavorites();
    }

    public void refreshView() {
        favoritesCardUi.clearCards();
        this.putFavoritesInCardUI(favorites);
    }

    public List<Quote> getFavorites() {
        return favorites;
    }

    public boolean isQuoteFavorite(Quote quote) {

        // Ignore ads
        if (quote == null || quote.getArtistIconUrl().equals("AD")){
            return false;
        }

        for (Quote qf : favorites)
            if (qf.equals(quote))
                return true;

        return false;
    }

    public void addToFavorites(Quote quote) {

        if (quote.getArtistIconUrl().equals("AD")){
            return;
        }

        // Send favorites to server only if was never liked before
        if (allTimesFavIds.contains(quote.getId()) == false) {
            allTimesFavIds += "," + quote.getId();
            //TODO: new LikeQuote().execute(quote.getId());
        }

        // Add the quote to the favorites view if not there already (and the quote is valid)
        if (!isQuoteFavorite(quote)) {
            addFavoriteQuoteCard(quote);
            favorites.add(quote);
            saveFavoritesToStorage();
        }
    }

    public void removeFromFavorites(Quote quote) {
        if (isQuoteFavorite(quote)) {
            favorites.remove(quote);
            favoritesCardUi.removeQuoteCard(quote);
            saveFavoritesToStorage();
        }
    }

    public int getFavoritesCount() {
        return this.favorites.size();
    }

    private void loadSavedFavorites() {
        allTimesFavIds = sharedPref.getString(ALL_TIMES_FAV_QUOTES_KEY, "");
        favorites = getSavedFavorites();
        putFavoritesInCardUI(favorites);

        // If no favorites were saved
        if (favorites == null)
            favorites = new ArrayList<Quote>();
    }

    private void saveFavoritesToStorage() {
        Editor editor = sharedPref.edit();

        List<Quote.QuoteStruct> favs = new ArrayList<>();
        for (Quote f : favorites){
             favs.add(f.getData());
        }

        // Save favorites locally
        Gson gson = new Gson();
        editor.putString(FAV_LIST_KEY, gson.toJson(favs));
        editor.putString(ALL_TIMES_FAV_QUOTES_KEY, allTimesFavIds);
        editor.apply();
    }

    private void addFavoriteQuoteCard(Quote quote) {
        CardFavoriteQuoteView fav = new CardFavoriteQuoteView(quote);
        fav.setOnCardSwipedListener(new OnCardSwipe());
        fav.setOnDeleteListener(new DeleteListener() {
            @Override
            public void onDeleteRequest(CardFavoriteQuoteView quoteCard) {
                removeFromFavorites(quoteCard.getQuote());
            }
        });
        favoritesCardUi.addCard(fav, true);
    }

    private void putFavoritesInCardUI(List<Quote> favorites) {
        for (Quote favQuote : favorites) {
            addFavoriteQuoteCard(favQuote);
        }
        favoritesCardUi.refresh();
    }

    private ArrayList<Quote> getSavedFavorites() {
        String favJson = sharedPref.getString(FAV_LIST_KEY, "");
        if (!favJson.equals("")) {
            Gson gson = new Gson();
            List<Quote.QuoteStruct> savedQuotes = gson.fromJson(favJson, new TypeToken<ArrayList<Quote.QuoteStruct>>() {
            }.getType());

            ArrayList<Quote> quotes = new ArrayList<>();
            for(Quote.QuoteStruct q : savedQuotes){
                quotes.add(Quote.createStaticQuote(q));
            }

            return quotes;
        }

        return new ArrayList<Quote>();
    }

    private class OnCardSwipe implements OnCardSwiped {

        @Override
        public void onCardSwiped(Card card, View layout) {
            CardFavoriteQuoteView quoteCard = (CardFavoriteQuoteView) card;
            UndoBar.Builder bar = new UndoBar.Builder(activity).setMessage("Deleted " + quoteCard.getQuote().getAuthor());
            bar.setListener(new UndoQuoteDeleteListener(quoteCard.getQuote()));
            bar.setAnimationDuration(500);
            bar.setDuration(1500);
            bar.show();
            favoritesCardUi.refresh();
        }
    }


    private class UndoQuoteDeleteListener implements Listener {

        private Quote quote;

        public UndoQuoteDeleteListener(Quote quote) {
            this.quote = quote;
            removeFromFavorites(quote);
        }

        @Override
        public void onHide() {
        }

        @Override
        public void onUndo(Parcelable token) {
            addFavoriteQuoteCard(quote);
        }
    }
}
