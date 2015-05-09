package com.gtr.quotes.views;

import android.content.Context;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.fima.cardsui.objects.Card;

public class CardAdView extends Card {

    private static final String AD_UNIT_ID = "ca-app-pub-5093250322451066/6637524237";
    private static boolean hasLoaded = false;

    @Override
    public View getCardContent(Context context) {
        return getAdView(context);
    }

    @Override
    public boolean convert(View convertCardView) {
        return true;
    }

    private static AdView adView = null;

    private View getAdView(Context context) {

        if (adView == null) {
            adView = new AdView(context);
            adView.setAdSize(AdSize.BANNER);
            adView.setAdUnitId(AD_UNIT_ID);
            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .addTestDevice("EFFF94558B3107A860AC2CEA28BF46B8")
                    .build();
            adView.loadAd(adRequest);
        }

        return adView;
    }
}
