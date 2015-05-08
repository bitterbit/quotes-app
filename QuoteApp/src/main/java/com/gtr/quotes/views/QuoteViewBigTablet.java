package com.gtr.quotes.views;

import android.content.Context;

import com.gtr.quotes.quote.Quote;

public class QuoteViewBigTablet extends QuoteView {

    private final static float FONT_SIZE = 4;
    private final static int QUOTE_WIDTH_PERCENTAGE = 85;
    private final static int AUTHOR_PIC_SIZE = 150;
    private final static int HEADER_MARGIN = 200;
    private final static int HEADER_LANDSCAPE_WIDTH = 800;


    public QuoteViewBigTablet(Context context, Quote quote) {
        super(context, quote);

    }

    @Override
    public QuoteViewVars getVars() {
        QuoteViewVars vars = new QuoteViewVars();
        vars.text_size = 4;
        vars.width_parentage = 85;
        vars.pic_size = 160;
        vars.top_margin = 260;
        vars.header_to_body_margin = 40;
        vars.image_to_title_margin = 40;

        vars.landscape_top_margin = 100;
        vars.landscape_header_width = 800;
        vars.landscape_header_to_body_margin = 50;
        vars.landscape_image_to_title_margin = 100;
        return vars;
    }

    /**
     * Properties:
     *  - width precantage
     *  - pic size
     *  - text size
     *  - title text size
     *  - padding between image to title
     *  - padding between header to body
     *  - padding from top
     */


}
