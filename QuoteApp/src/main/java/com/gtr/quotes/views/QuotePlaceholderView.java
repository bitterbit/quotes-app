package com.gtr.quotes.views;

import android.content.Context;
import android.view.View;

import com.gtr.quotes.quote.Quote;

public class QuotePlaceholderView extends View implements IQuoteView {

    public QuotePlaceholderView(Context context) {
        super(context);
    }

    @Override
    public Quote getQuote() {
        return new Quote("", "", "EMPTY");
    }

    @Override
    public boolean isLoading() {
        return false;
    }

    @Override
    public void setVisible(boolean visible) {
        //Do nothing, always invisible
    }

    @Override
    public boolean isVisible() {
        return false;
    }
}
