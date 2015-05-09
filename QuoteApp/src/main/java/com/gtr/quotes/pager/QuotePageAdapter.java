package com.gtr.quotes.pager;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.gtr.quotes.quote.Quote;
import com.gtr.quotes.quote.QuoteManager;
import com.gtr.quotes.views.QuoteAdView;
import com.gtr.quotes.views.QuotePlaceholderView;
import com.gtr.quotes.views.QuoteViewFactory;
import com.jfeinstein.jazzyviewpager.JazzyViewPager;

import java.util.Random;

public class QuotePageAdapter extends PagerAdapter {

    private int lastPosition = -1;
    private QuoteManager quoteManager;

    public QuotePageAdapter(QuoteManager quoteManager) {
        this.quoteManager = quoteManager;
    }

    @Override
    public int getCount() {
        return 99999;
    }


    private View getViewToDisplay(Context context, int position) {
        View v;
        if (position < lastPosition) {
            v = new QuotePlaceholderView(context);
        } else if (shouldShowAndAd()) {
            v = new QuoteAdView(context);
        } else {
            Quote quote = quoteManager.getQuote();
            v = QuoteViewFactory.getQuoteView(context, quote);
        }
        lastPosition = position;
        return v;
    }

    /**
     * Create the page for the given position. The adapter is responsible for
     * adding the view to the container given here, although it only must ensure
     * this is done by the time it returns from
     * {@link #finishUpdate(android.view.ViewGroup)}.
     *
     * @param collection The containing View in which the page will be shown.
     * @param position   The page position to be instantiated.
     * @return Returns an Object representing the new page. This does not need
     * to be a View, but can be some other container of the page.
     */
    @Override
    public Object instantiateItem(ViewGroup collection, int position) {

        View view = getViewToDisplay(collection.getContext(), position);

        collection.addView(view, 0);
        ((JazzyViewPager) collection).setObjectForPosition(view, position);

        return view;
    }

    private boolean lastViewWasAd = false;

    private boolean shouldShowAndAd() {
        if (lastViewWasAd) {
            lastViewWasAd = false;
        } else {
            Random rnd = new Random();
            lastViewWasAd = rnd.nextInt(18) == 1;
        }
        return lastViewWasAd;
    }

    /**
     * Remove a page for the given position. The adapter is responsible for
     * removing the view from its container, although it only must ensure this
     * is done by the time it returns from
     * {@link #finishUpdate(android.view.ViewGroup)}.
     *
     * @param collection The containing View from which the page will be removed.
     * @param position   The page position to be removed.
     * @param view       The same object that was returned by
     *                   {@link #instantiateItem(android.view.View, int)}.
     */
    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    /**
     * Determines whether a page View is associated with a specific key object
     * as returned by instantiateItem(ViewGroup, int). This method is required
     * for a PagerAdapter to function properly.
     *
     * @param view   Page View to check for association with object
     * @param object Object to check for association with view
     * @return
     */
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }

    /**
     * Called when the a change in the shown pages has been completed. At this
     * point you must ensure that all of the pages have actually been added or
     * removed from the container as appropriate.
     *
     * @param arg0 The containing View which is displaying this adapter's page
     *             views.
     */
    @Override
    public void finishUpdate(ViewGroup arg0) {
    }

    @Override
    public void restoreState(Parcelable parcelable, ClassLoader arg1) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void startUpdate(ViewGroup arg0) {
    }
}