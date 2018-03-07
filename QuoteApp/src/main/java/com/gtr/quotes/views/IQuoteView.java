package com.gtr.quotes.views;

import com.gtr.quotes.quote.Quote;

public interface IQuoteView {

    Quote getQuote();

    boolean isLoading();

    void setVisible(boolean visible);

    boolean isVisible();

}
