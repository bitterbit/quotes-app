package com.gtr.quotes.views;

import android.content.Context;

import com.galtashma.lazyparse.LazyParseObjectHolder;
import com.gtr.quotes.quote.Quote;

public class QuoteViewTablet extends QuoteView {

    public QuoteViewTablet(Context context, LazyParseObjectHolder<Quote> quote) {
        super(context, quote);
    }

    @Override
    public QuoteViewVars getVars() {
        QuoteViewVars vars = new QuoteViewVars();

        vars.text_size = 3;
        vars.width_parentage = 90;
        vars.pic_size = 125;
        vars.top_margin = 180;
        vars.landscape_top_margin = 85;
        vars.landscape_header_width = 500;
        vars.landscape_header_to_body_margin = 25;
        vars.landscape_image_to_title_margin = 100;

        return vars;
    }


}
