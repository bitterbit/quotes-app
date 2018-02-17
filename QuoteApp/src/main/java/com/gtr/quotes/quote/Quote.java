package com.gtr.quotes.quote;

import android.os.Parcel;
import android.util.Log;

import com.galtashma.lazyparse.LazyParseObject;
import com.gtr.quotes.util.Consts;
import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Quotes")
public class Quote extends LazyParseObject {

    public enum Status {
        DONE_SUCCESSFUL,
        LOADING,
        ERROR
    }

    public Quote() {}

    public static Quote createStaticQuote(String text, String author, String artistIConUrl){
        Quote q = new Quote();
        q.setText(text);
        q.setAuthor(author);
        q.setArtistIconUrl(artistIConUrl);
        return q;
    }

    public String getText() {
        return getString("text");
    }

    public String getAuthor() {
        return getString("author");
    }

    public String getId() {
        return getObjectId();
    }

    public String getWikipediaLink() {
        return "http://en.wikipedia.org/w/index.php?search=" + getAuthor().replace(' ', '+');
    }

    public String getArtistIconUrl() {
        String iconUrl = getString("artistIconUrl");
        if (iconUrl != null && !iconUrl.isEmpty())
            return iconUrl;

        return Consts.UNKNOWN_PERSON_URL;
    }

    private void setAuthor(String author) {
        put("author", author);
    }

    private void setText(String text) {
        put("text", text);
    }

    private void setArtistIconUrl(String artistIconUrl) {
        put("artistIconUrl", artistIconUrl);
    }

    @Override
    public boolean equals(Object o) {

        if (o instanceof Quote)
            return ((Quote) o).getId().equals(this.getId());

        return super.equals(o);
    }

    private static boolean isEmptyString(String s) {
        if (s == null) {
            return true;
        }
        return s.equals("null") || s.isEmpty();
    }


}
