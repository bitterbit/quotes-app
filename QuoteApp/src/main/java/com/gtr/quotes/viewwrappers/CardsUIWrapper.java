package com.gtr.quotes.viewwrappers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.fima.cardsui.objects.AbstractCard;
import com.fima.cardsui.objects.Card;
import com.fima.cardsui.objects.CardStack;
import com.fima.cardsui.views.CardUI;
import com.gtr.quotes.quote.Quote;
import com.gtr.quotes.views.CardFavoriteQuoteView;
import com.gtr.quotes.views.CardNoFavoritesView;

import java.util.ArrayList;


public class CardsUIWrapper extends CardUI {

    private int cardCount = 0;

    public CardsUIWrapper(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setSwipeable(true);
    }

    public CardsUIWrapper(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setSwipeable(true);
    }

    public CardsUIWrapper(Context context) {
        super(context);
        this.setSwipeable(true);
    }

    private void decreaseCardCount() {
        if (cardCount > 0)
            cardCount--;
    }

    @Override
    public void clearCards() {
        cardCount = 0;
        super.clearCards();
    }

    @Override
    public void addCard(Card card, boolean refresh) {

        if (card instanceof CardNoFavoritesView == false)
            cardCount++;
        else if (containsEmptyFavCard())
            return;

        super.addCard(card, refresh);
    }

    public void removeQuoteCard(Quote quote) {
        for (int i = 0; i < mStacks.size(); i++) {
            AbstractCard card = mStacks.get(i);
            if (card instanceof CardStack) {
                CardStack cardStack = ((CardStack) card);
                for (int k = 0; k < cardStack.getCards().size(); k++) {
                    CardFavoriteQuoteView innerCard = (CardFavoriteQuoteView) cardStack.getCards().get(k);
                    if (innerCard != null && innerCard.getQuote().equals(quote)) {
                        cardStack.remove(k);
                        decreaseCardCount();
                    }
                }
            }
        }
        refresh();
    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    @Override
    @SuppressLint("NewApi")
    public void refresh() {

        if (cardCount >= 1)
            showDefaultScreen();

        else if (cardCount == 0)
            showEmptyScreen();

        super.refresh();
    }

    private void showDefaultScreen() {
        mStacks = removeEmptyFavCard();
        this.setSwipeable(true);
    }

    private void showEmptyScreen() {
        this.setSwipeable(false);
        Card card = new CardNoFavoritesView();
        this.addCard(card);
    }

    private boolean containsEmptyFavCard() {
        for (AbstractCard card : mStacks) {

            AbstractCard innerCard = card;

            if (card instanceof CardStack)
                if (((CardStack) card).getCards().size() > 0)
                    innerCard = ((CardStack) card).get(0);

            if (innerCard instanceof CardNoFavoritesView) {
                return true;
            }
        }
        return false;
    }

    private ArrayList<AbstractCard> removeEmptyFavCard() {
        ArrayList<AbstractCard> newStack = new ArrayList<AbstractCard>();

        for (AbstractCard card : mStacks) {

            AbstractCard innerCard = card;

            if (card instanceof CardStack)
                if (((CardStack) card).getCards().size() > 0)
                    innerCard = ((CardStack) card).get(0);

            if (innerCard instanceof CardNoFavoritesView == false) {
                newStack.add(card);
            }
        }
        return newStack;
    }
}
