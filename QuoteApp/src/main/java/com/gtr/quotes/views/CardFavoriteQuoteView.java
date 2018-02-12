package com.gtr.quotes.views;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;

import com.fima.cardsui.objects.RecyclableCard;
import com.gtr.quotes.R;
import com.gtr.quotes.quote.Quote;
import com.gtr.quotes.util.ShareUtil;

public class CardFavoriteQuoteView extends RecyclableCard implements OnClickListener {

    public interface DeleteListener {
        void onDeleteRequest(CardFavoriteQuoteView quoteCard);
    }

    private Quote quote;
    private PopupMenu popupMenu = null;

    private DeleteListener listener = null;

    private static final int ACTION_SHARE = 1;
    private static final int ACTION_DELETE = 2;
    private static final int ACTION_WIKIPEDIA = 3;

    public CardFavoriteQuoteView(Quote quote) {
        super(quote.getAuthor(), quote.getText(), "#fff", "#000", true, false);
        this.quote = quote;
    }

    public CardFavoriteQuoteView(String title, int image) {
        super(title, image);
    }


    public String getQuoteId() {
        return quote.getId();
    }

    public Quote getQuote() {
        return quote;
    }

    public void setOnDeleteListener(DeleteListener listener) {
        this.listener = listener;
    }

    @Override
    protected int getCardLayoutId() {
        return R.layout.quote_card_layout;
    }

    @Override
    protected void applyTo(View convertView) {
        ((TextView) convertView.findViewById(R.id.title)).setText(quote.getAuthor());
        ((TextView) convertView.findViewById(R.id.description)).setText(quote.getText());
        ((RoundImageView) convertView.findViewById(R.id.round_author_image)).setImageUrl(quote.getArtistIconUrl());

        popupMenu = new PopupMenu(convertView.getContext(), convertView.findViewById(R.id.quote_card_menu));
        popupMenu.getMenu().add(Menu.NONE, ACTION_SHARE, Menu.NONE, "Share");
        popupMenu.getMenu().add(Menu.NONE, ACTION_DELETE, Menu.NONE, "Remove");
        popupMenu.getMenu().add(Menu.NONE, ACTION_WIKIPEDIA, Menu.NONE, "Wikipedia");

        popupMenu.setOnMenuItemClickListener(new MenuItemListener(convertView.getContext()));
        convertView.findViewById(R.id.quote_card_menu).setOnClickListener(this);

    }

    private class MenuItemListener implements OnMenuItemClickListener {

        private Context context;

        public MenuItemListener(Context context) {
            this.context = context;
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case ACTION_SHARE:
                    ShareUtil.getInstance().shareQuote(context, quote);
                    break;
                case ACTION_WIKIPEDIA:
                    String url = quote.getWikipediaLink();
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    context.startActivity(browserIntent);
                    break;
                case ACTION_DELETE:
                    if (listener != null)
                        listener.onDeleteRequest(CardFavoriteQuoteView.this);
                    break;

            }
            return false;
        }

    }

    @Override
    public void onClick(View arg0) {
        if (popupMenu != null) {
            popupMenu.show();
        }

    }

}