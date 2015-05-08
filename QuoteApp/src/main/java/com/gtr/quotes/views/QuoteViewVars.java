package com.gtr.quotes.views;

/**
 * title size = 60*text_size
 * landscape_top_margin = top_margin/2.2f
 * landscape_image_to_title_margin = 100
 */

public class QuoteViewVars {
    public int text_size; // Value in em
    public int pic_size, title_text_size; // dp,px
    float landscape_image_to_title_margin, image_to_title_margin, top_margin, landscape_top_margin, header_to_body_margin, landscape_header_to_body_margin, landscape_header_width; //Values in dp
    public int width_parentage;

    public QuoteViewVars() {
        default_val();
    }

    public void default_val() {
        text_size = 2;
        title_text_size = 60 * text_size;

        pic_size = 100;

        image_to_title_margin = 10;
        landscape_image_to_title_margin = 100;

        top_margin = 50;
        landscape_top_margin = top_margin / 2.2f;

        header_to_body_margin = 10;
        landscape_header_to_body_margin = header_to_body_margin;

        landscape_header_width = 400;
        width_parentage = 95;
    }


}
