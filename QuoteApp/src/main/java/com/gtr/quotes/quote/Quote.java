package com.gtr.quotes.quote;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.gtr.quotes.util.Consts;

public class Quote implements Parcelable {

    public static enum Status {
        DONE_SUCCESSFUL,
        LOADING,
        ERROR
    }

    protected String text = "";
    protected String author = "";
    protected String id = "";
    protected double riskTaken = 0.01;
    protected String artist_icon_url = Consts.UNKOWN_PERSON_URL;

    public Quote() {

    }

    public Quote(String text, String author, String id) {
        super();
        this.id = id;
        this.author = author;
        this.text = text;
    }

    public Quote(String text, String author, String id, String artistIconUrl) {
        super();
        this.id = id;
        this.author = author;
        this.text = text;
        this.artist_icon_url = artistIconUrl;
    }

    public Quote(Parcel parcel) {
        this.id = parcel.readString();
        this.author = parcel.readString();
        this.text = parcel.readString();
        this.artist_icon_url = Consts.UNKOWN_PERSON_URL;
        ;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getRiskTaken() {
        return riskTaken;
    }

    public void setRiskTaken(double riskTaken) {
        this.riskTaken = riskTaken;
    }

    private static boolean isEmptyString(String s) {
        if (s == null)
            return true;
        if (s.equals("null") || s.isEmpty())
            return true;
        return false;
    }

    /**
     * Get the status of the quote
     *
     * @return returns the status of the quote at that moment
     */
    public Status getStatus() {
        final String EMPTY_MD5 = "d41d8cd98f00b204e9800998ecf8427e";

        Log.i("Quotes", "get status. id: " + id);

        if (isEmptyString(id) || id.equals(EMPTY_MD5) || isEmptyString(author) || isEmptyString(text)) {
            Log.w("Quotes", "Empty Quote. id: " + id);
            return Status.ERROR;
        }

        return Status.DONE_SUCCESSFUL;
    }

    public String getWikipediaLink() {
        return "http://en.wikipedia.org/w/index.php?search=" + author.replace(' ', '+');
    }

    public String getArtistIconUrl() {
        if (artist_icon_url != null)
            return artist_icon_url;
        return Consts.UNKOWN_PERSON_URL;
    }

    public void setArtistIconUrl(String artistIconUrl) {
        this.artist_icon_url = artistIconUrl;
    }

    @Override
    public boolean equals(Object o) {

        if (o instanceof Quote)
            return ((Quote) o).getId().equals(this.getId());

        return super.equals(o);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(id);
        parcel.writeString(author);
        parcel.writeString(text);
    }


}
