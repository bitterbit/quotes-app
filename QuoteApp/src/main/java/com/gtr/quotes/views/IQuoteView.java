package com.gtr.quotes.views;

import com.gtr.quotes.quote.Quote;

public interface IQuoteView {

    public Quote getQuote();

    public boolean isLoading();

    public void setVisible(boolean visible);

    public boolean isVisible();

}
