package com.gtr.quotes.views;

import android.content.Context;

import com.galtashma.lazyparse.LazyParseObjectHolder;
import com.gtr.quotes.quote.Quote;

public class QuoteViewPhone extends QuoteView {

    public QuoteViewPhone(Context context, LazyParseObjectHolder<Quote> quote) {
        super(context, quote);
    }

    @Override
    public QuoteViewVars getVars() {
        QuoteViewVars vars = new QuoteViewVars();

        vars.pic_size = 100;
        vars.top_margin = 90;
        vars.header_to_body_margin = 0;
        vars.image_to_title_margin = 0;
        vars.landscape_top_margin = 50;
        vars.landscape_image_to_title_margin = 20;
        vars.landscape_header_width = 400;
        vars.width_parentage = 99;
        return vars;
    }
}
