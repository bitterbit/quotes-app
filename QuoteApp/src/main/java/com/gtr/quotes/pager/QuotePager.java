package com.gtr.quotes.pager;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import com.gtr.quotes.quote.Quote;
import com.gtr.quotes.views.IQuoteView;

public class QuotePager extends DynamicViewPager {

    public interface Listener {
        void onPageChange(Quote quote);
    }

    private Listener listener = null;
    private Quote currentQuote;
    private boolean lockPagerForward = false;
    private boolean loadedFirstQuote = false;

    public QuotePager(Context context) {
        super(context);
        init();
    }

    public QuotePager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        this.setOnPageChangeListener(new PageScrollListener());
    }

    public Quote getCurrentQuote() {
        return currentQuote;
    }

    private IQuoteView getCurrentQuoteView() {
        return (IQuoteView) getChildAt(getOffscreenPageLimit());
    }

    private IQuoteView getPrevQuoteView() {
        return (IQuoteView) getChildAt(getOffscreenPageLimit() + 1);
    }

    private IQuoteView getNextQuoteView() {
        return (IQuoteView) getChildAt(getOffscreenPageLimit() - 1);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    protected void onMeasure(int arg0, int arg1) {
        super.onMeasure(arg0, arg1);
        if (!loadedFirstQuote) {
            currentQuote = getCurrentQuoteView().getQuote();
            loadedFirstQuote = true;
        }
    }

    private class PageScrollListener implements OnPageChangeListener {

        int lastPosition = -1;

        @Override
        public void onPageScrolled(int page, float inPagePos, int pagePixel) {
            IQuoteView quoteView = (IQuoteView) getChildAt(getOffscreenPageLimit());
            lockPagerForward = quoteView.isLoading();
        }

        @Override
        public void onPageSelected(int position) {
            updateCurrentPage(position);
            lockLogic(position);
        }

        @Override
        public void onPageScrollStateChanged(int position) {
        }

        private void updateCurrentPage(int position) {

            int velocity = position - lastPosition;

            if (velocity == 0)
                currentQuote = getCurrentQuoteView().getQuote();
            else if (velocity > 0)
                currentQuote = getNextQuoteView().getQuote();
            else if (velocity < 0) {
                try {
                    currentQuote = getPrevQuoteView().getQuote();
                } catch (NullPointerException e) {
                    currentQuote = getCurrentQuoteView().getQuote();
                }
            }

            if (listener != null) {
                listener.onPageChange(currentQuote);
            }

            Log.d("Quotes", "position: " + position + " last: " + lastPosition + "  Current quote: " + currentQuote);
        }

        private void lockLogic(int position) {

            IQuoteView prevQuote = getPrevQuoteView();

            // Going forward, delete previous quotes
            if (position >= lastPosition) {
                if (lockPagerForward) {
                    scrollToItem(lastPosition); // Go back to the origin
                } else if (prevQuote != null && position != lastPosition) {
                    prevQuote.setVisible(false);
                    Log.v("Quotes", "Making quote invisible");
                }

                lastPosition = position;
            }

            // Going back one page
            else if (lastPosition - position == 1 && prevQuote.isVisible()) { /* Do nothing */ }

            // Going backwards more than one page, dont allow
            else {
                scrollToItem(position + 1);
            }
        }
    }
}
