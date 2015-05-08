package com.gtr.quotes.views;

import android.view.View;
import android.widget.TextView;

import com.gtr.cardsuilib.objects.RecyclableCard;
import com.gtr.quotes.R;

public class CardNoFavoritesView extends RecyclableCard {

    private final String text = "You have no favorite Quotes. Add some by pressing the heart in the top right corner in the main page";
    private final String title = "You Have No Favorites";

    @Override
    protected void applyTo(View convertView) {
        ((TextView) convertView.findViewById(R.id.title)).setText(title);
        ((TextView) convertView.findViewById(R.id.description)).setText(text);
    }

    @Override
    protected int getCardLayoutId() {
        return R.layout.no_favs_card_layout;
    }

}
