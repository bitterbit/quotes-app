package com.gtr.quotes.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.gtr.quotes.quote.Quote;
import com.gtr.quotes.util.PixelUtils;

public class QuoteAdView extends LinearLayout implements IQuoteView {

    private static final String AD_UNIT_ID = "ca-app-pub-5093250322451066/6637524237";

    public QuoteAdView(Context context) {
        super(context);
        init();
    }

    public QuoteAdView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    public Quote getQuote() {
        //return an empty quote, this way this quote cant be liked
        return new Quote("", "", "AD");
    }

    @Override
    public boolean isLoading() {
        return false;
    }

    @Override
    public void setVisible(boolean makeVisible) {
        this.setVisibility((makeVisible ? View.VISIBLE : View.INVISIBLE));
    }

    @Override
    public boolean isVisible() {
        return this.getVisibility() == View.VISIBLE;
    }

    private void init() {
        this.setGravity(Gravity.CENTER);
        this.setOrientation(VERTICAL);

        this.addView(getTitleView());
        this.addView(getAdView());
    }

    private View getTitleView() {
        TextView title = new AutoFitTextView(getContext());
        title.setGravity(Gravity.CENTER);

        title.setText("Advertisement");
        title.setTextSize(60f);
        title.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "font/google_font.ttf"));
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, PixelUtils.pxFromDp(getContext(), 60));
        title.setLayoutParams(params);
        return title;
    }

    private View getAdView() {

        AdView adView = new AdView(getContext());
        adView.setAdSize(AdSize.MEDIUM_RECTANGLE);
        adView.setAdUnitId(AD_UNIT_ID);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("EFFF94558B3107A860AC2CEA28BF46B8")
                .build();
        adView.loadAd(adRequest);
        return adView;
    }
}
