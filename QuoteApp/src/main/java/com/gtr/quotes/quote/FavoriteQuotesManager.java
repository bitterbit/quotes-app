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
import com.gtr.cardsuilib.objects.Card;
import com.gtr.cardsuilib.objects.Card.OnCardSwiped;
import com.gtr.quotes.util.Consts;
import com.gtr.quotes.views.CardFavoriteQuoteView;
import com.gtr.quotes.views.CardFavoriteQuoteView.DeleteListener;
import com.gtr.quotes.viewwrappers.CardsUIWrapper;
import com.gtr.undobar.UndoBar;
import com.gtr.undobar.UndoBar.Listener;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
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

        for (Quote qf : favorites)
            if (qf.equals(quote))
                return true;

        return false;
    }

    public void addToFavorites(Quote quote) {

        // Send favorites to server only if was never liked before
        if (allTimesFavIds.contains(quote.getId()) == false) {
            allTimesFavIds += "," + quote.getId();
            new LikeQuote().execute(quote.getId());
        }

        // Add the quote to the favorites view if not there already (and the quote is valid)
        if (!isQuoteFavorite(quote) && quote.getStatus() == Quote.Status.DONE_SUCCESSFUL) {
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
        // Save favorites locally
        Gson gson = new Gson();
        editor.putString(FAV_LIST_KEY, gson.toJson(favorites));
        editor.putString(ALL_TIMES_FAV_QUOTES_KEY, allTimesFavIds);
        editor.commit();
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
            return gson.fromJson(favJson, new TypeToken<ArrayList<Quote>>() {
            }.getType());
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

    private class LikeQuote extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            HttpClient httpclient = new DefaultHttpClient();
            try {
                if (params.length >= 1) {
                    String url = API_URL + "?page=AddLike&id=" + params[0];
                    httpclient.execute(new HttpGet(url));
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

    }
}