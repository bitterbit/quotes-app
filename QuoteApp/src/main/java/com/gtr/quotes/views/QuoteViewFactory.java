package com.gtr.quotes.views;

import android.content.Context;
import android.content.res.Configuration;
import android.view.View;

import com.gtr.quotes.quote.Quote;

public class QuoteViewFactory {

    private enum ScreenSize {PHONE_SMALL, PHONE_MED, PHONE_LARGE, TAB_SEVEN, TAB_TEN}

    ;

    public static View getQuoteView(Context context, Quote quote) {

        ScreenSize screenSize = getScreenSize(context);

        if (screenSize == ScreenSize.PHONE_SMALL || screenSize == ScreenSize.PHONE_MED || screenSize == ScreenSize.PHONE_LARGE)
            return new QuoteViewPhone(context, quote);

        else if (screenSize == ScreenSize.TAB_SEVEN)
            return new QuoteViewTablet(context, quote);

        else if (screenSize == ScreenSize.TAB_TEN)
            return new QuoteViewBigTablet(context, quote);

        return new QuoteViewPhone(context, quote);
    }

    public static ScreenSize getScreenSize(Context context) {
        int screenLayout = getScreenLayout(context);

        if (screenLayout == Configuration.SCREENLAYOUT_SIZE_XLARGE)
            return ScreenSize.TAB_TEN;

        if (screenLayout == Configuration.SCREENLAYOUT_SIZE_LARGE)
            return ScreenSize.TAB_SEVEN;

        if (screenLayout == Configuration.SCREENLAYOUT_SIZE_NORMAL)
            return ScreenSize.PHONE_LARGE;

        return ScreenSize.PHONE_MED;
    }

    public static int getScreenLayout(Context context) {
        int screenLayout = context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
        return screenLayout;
    }

}
