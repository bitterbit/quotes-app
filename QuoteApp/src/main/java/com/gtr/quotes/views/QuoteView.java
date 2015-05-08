package com.gtr.quotes.views;


import android.animation.LayoutTransition;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gtr.quotes.quote.AsyncQuote;
import com.gtr.quotes.quote.Quote;
import com.gtr.quotes.quote.Quote.Status;
import com.gtr.quotes.util.PixelUtils;

public class QuoteView extends LinearLayout implements IQuoteView {

    private Quote quote;
    private WebView webView;

    private LinearLayout header = null;
    private ProgressBar loadingSpinner = null;
    private RoundImageView authorImage = null;

    private boolean error = false;

    private QuoteViewVars vars;

    private static final String TEXT_COLOR = "#1d1d1d";

    public QuoteView(Context context) {
        super(context);
        this.vars = getVars();
        initialize();
    }

    /**
     * Initialize the view no matter what,
     * load the quote data to the view only when its ready
     */
    public QuoteView(Context context, Quote quote) {
        super(context);
        this.quote = quote;
        this.vars = getVars();
        initialize();

        if (quote instanceof AsyncQuote)
            loadAsyncQuote((AsyncQuote) quote);
        else
            loadBody();
    }

    public QuoteViewVars getVars() {
        return new QuoteViewVars();
    }

    @Override
    public Quote getQuote() {
        return quote;
    }

    @Override
    public boolean isLoading() {
        return quote.getStatus() == Status.LOADING;
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        refreshHeader();
    }

    private void loadAsyncQuote(AsyncQuote quote) {

        if (quote.getStatus() == Status.DONE_SUCCESSFUL) {
            refreshView();
        } else if (quote.getStatus() == Status.LOADING) {
            quote.setListener(new AsyncQuote.Listener() {

                @Override
                public void onStatusChange(Status status) {

                    if (status == Status.DONE_SUCCESSFUL)
                        refreshView();
                    else if (status == Status.LOADING)
                        showNoInternetMessage();
                    else
                        showNoInternetMessage();

                    hideLoadingSpinner();
                }
            });
            showLoadingSpinner();
        } else {
            showNoInternetMessage();
        }
    }

    private void initialize() {
        this.setGravity(Gravity.CENTER);
        this.setOrientation(LinearLayout.VERTICAL);
        this.setBackgroundColor(Color.argb(0, 0, 0, 0));

        this.setLayoutTransition(new LayoutTransition());

        initHeader();
        initWebView();

        // If is not ready initialize the loading spinner
        if (quote.getStatus() == Status.LOADING)
            initLoadingSpinner();
    }

    private void initHeader() {
        header = new LinearLayout(getContext());
        header.setGravity(Gravity.CENTER);

        refreshHeader();
        this.addView(header, 0);
    }

    private void refreshView() {
        refreshHeader();
        loadBody();
    }

    private void refreshHeader() {
        header.removeAllViews();
        View authorImage = initAuthorImage(isPortrait());
        View title = initTitle(isPortrait());

        if (isPortrait()) {
            header.setOrientation(LinearLayout.VERTICAL);
            LayoutParams headerParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            headerParams.setMargins(0, PixelUtils.pxFromDp(getContext(), vars.top_margin), 0, PixelUtils.pxFromDp(getContext(), vars.header_to_body_margin));
            header.setLayoutParams(headerParams);
        } else {
            header.setOrientation(LinearLayout.HORIZONTAL);
            LayoutParams headerParams = new LayoutParams(PixelUtils.pxFromDp(getContext(), vars.landscape_header_width), LayoutParams.WRAP_CONTENT);
            headerParams.setMargins(0, PixelUtils.pxFromDp(getContext(), vars.landscape_top_margin), 0, PixelUtils.pxFromDp(getContext(), vars.landscape_header_to_body_margin));
            header.setLayoutParams(headerParams);
        }

        header.addView(authorImage);
        header.addView(title);
    }

    private View initTitle(boolean portrait) {
        TextView title = new AutoFitTextView(getContext());
        title.setText(quote.getAuthor());
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, PixelUtils.pxFromDp(getContext(), 100));

        if (portrait) {
            title.setGravity(Gravity.CENTER);
        } else {
            title.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        }

        title.setLayoutParams(params);

        title.setMaxLines(1);
        title.setTextSize(vars.title_text_size);

        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "font/google_font.ttf");
        title.setTypeface(tf, Typeface.BOLD);
        title.setTextColor(Color.parseColor(TEXT_COLOR));

        return title;
    }

    private View initAuthorImage(boolean portrait) {
        return initAuthorImage(quote.getArtistIconUrl(), portrait);
    }

    private View initAuthorImage(String url, boolean portrait) {
        int imageSize = PixelUtils.pxFromDp(getContext(), vars.pic_size);
        authorImage = new RoundImageView(getContext(), url);

        LayoutParams params;
        if (portrait) {
            params = new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, imageSize);
            params.setMargins(0, 0, 0, PixelUtils.pxFromDp(getContext(), vars.image_to_title_margin));
        } else {
            imageSize = imageSize - (imageSize / 10);
            params = new LayoutParams(imageSize, imageSize);
            params.setMargins(0, 0, PixelUtils.pxFromDp(getContext(), vars.landscape_image_to_title_margin), 0);
        }

        authorImage.setLayoutParams(params);

        return authorImage;
    }

    private void initWebView() {
        webView = new WebView(getContext());
        webView.setBackgroundColor(Color.argb(0, 100, 100, 100));
        webView.setHorizontalScrollBarEnabled(false);
        webView.setVerticalScrollBarEnabled(false);
        webView.setDrawingCacheEnabled(true);
        webView.setAlwaysDrawnWithCacheEnabled(true);

        LayoutParams webviewParams = new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT);
        webView.setLayoutParams(webviewParams);
        this.addView(webView);
    }

    private void loadBody() {
        String text = "<html><head>" + getCss() + "</head>"
                + "<body>"
                + "<div id='main'><blockquote class=\"bq\"><p class=\"quote\">" + quote.getText() + "</p></blockquote></div>"
                + "</body></html>";

        webView.loadDataWithBaseURL("file:///android_asset/", text, "text/html", "utf-8", null);
    }

    private void showNoInternetMessage() {
        error = true;
        authorImage.setVisibility(View.INVISIBLE);

        String text = "<html><head>" + getCss() + "</head>"
                + "<body>"
                + "<h3 style='text-align:center; color: black;'> An error occurred :( </h3>"
                + "<h4 style='text-align:center; color: black;'> check your internet connection and swipe </h4>"
                + "</body></html>";

        webView.loadDataWithBaseURL("file:///android_asset/", text, "text/html", "utf-8", null);
    }

    private String getCss() {

        String css = "<link href='http://fonts.googleapis.com/css?family=Alegreya+Sans:300' rel='stylesheet' type='text/css'>"
                + "<style>"
                + "body { font-family: 'Alegreya Sans', sans-serif;  font-size:" + vars.text_size + "em; width: 97%; color: " + TEXT_COLOR + ";}"
                + "#main { width: " + vars.width_parentage + "%; margin: auto; }"
                + ".quote { margin-right: -15px; }"
                + ".bq { margin-left: 10px; text-align: center; }"
                + ".bqstart { font-family: 'Trebuchet MS', Helvetica, sans-serif; float: left; height: 100px; margin-top: -35px; padding-top: 20px; padding-right: 20px; margin-bottom: -50px; font-size: 300%;}"
                + ".bqend { float: right; height: 10px; width: 0px; margin-top: -160px; padding-top: 80px; font-size: 300%; font-family: 'Trebuchet MS', Helvetica, sans-serif;}"//height: 10px; width: 5px; margin-top: -160px; padding-top: 80px; padding-right: -200px;
                + ".author { margin-top: 10px; color: #000000; text-decoration: none; text-align:center}"
                + "</style>";
        return css;
    }

    private void initLoadingSpinner() {
        loadingSpinner = new ProgressBar(getContext());
        loadingSpinner.setVisibility(View.GONE);
        this.addView(loadingSpinner);
    }

    private void showLoadingSpinner() {
        if (loadingSpinner != null) {
            webView.setVisibility(View.GONE);
            authorImage.setVisibility(View.GONE);
            loadingSpinner.setVisibility(View.VISIBLE);
        }
    }

    private void hideLoadingSpinner() {
        if (loadingSpinner != null) {
            loadingSpinner.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
            if (error == false) {
                authorImage.setVisibility(View.VISIBLE);
                Log.d("Quotes", "Quote: " + quote.getId() + "image VISI");
            }
        }
    }

    private boolean isPortrait() {
        return getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    @Override
    public void setVisible(boolean makeVisible) {
        if (makeVisible) {
            super.setAlpha(1);
            super.setVisibility(View.VISIBLE);
        } else {
            super.setAlpha(0);
            super.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean isVisible() {
        return this.getVisibility() == View.VISIBLE && this.getAlpha() > 0.01;
    }

}

