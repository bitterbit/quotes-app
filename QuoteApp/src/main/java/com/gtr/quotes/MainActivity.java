package com.gtr.quotes;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.gtr.quotes.quote.Quote;
import com.gtr.quotes.quote.QuoteManager;
import com.gtr.quotes.tracking.AnalyticsHandler;
import com.gtr.quotes.tracking.MixpanelAgent;
import com.gtr.quotes.tracking.WikiEvent;
import com.gtr.quotes.util.ShareUtil;
import com.gtr.quotes.views.ShowcaseView;
import com.gtr.quotes.viewwrappers.ActionBarLayoutWrapper;
import com.gtr.quotes.viewwrappers.QuotePagerWrapper;
import com.gtr.quotes.viewwrappers.SlidingUpDrawerWrapper;
import com.gtr.quotes.viewwrappers.SlidingUpDrawerWrapper.Listener;

public class MainActivity extends Activity {

    private QuoteManager quoteManager;

    private ActionBarLayoutWrapper actionBarLayoutWrapper;
    private SlidingUpDrawerWrapper drawerWrapper;
    private QuotePagerWrapper quotePagerWrapper;

    private AnalyticsHandler analytics;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        analytics = new AnalyticsHandler();
        analytics.addAgent(new MixpanelAgent(this));

        quoteManager = new QuoteManager(this, analytics);

        actionBarLayoutWrapper = new ActionBarLayoutWrapper(this, getActionBar(), R.menu.activity_main);

        quotePagerWrapper = new QuotePagerWrapper(this, quoteManager, actionBarLayoutWrapper);
        quotePagerWrapper.setAnalyticsHandler(analytics);

        drawerWrapper = new SlidingUpDrawerWrapper(this, actionBarLayoutWrapper, analytics);
        drawerWrapper.setListener(new Listener() {

            @Override
            public void onPanelCollapsed() {
                updateActionBarLayout();
                quoteManager.refreshFavView();
            }
        });

        ShareUtil.getInstance().setAnalyticsHandler(analytics);

        // TODO: Put only on the first open
        //showHelpView();
    }

    private void showHelpView() {
        ShowcaseView showcaseView = (ShowcaseView) findViewById(R.id.showcase_layout);
        showcaseView.setActionBar(actionBarLayoutWrapper);
        showcaseView.show();
    }

    private void updateActionBarLayout() {
        Quote quote = quotePagerWrapper.getCurrentQuote();
        if (quote != null) {
            if (quoteManager.isQuoteFavorite(quote))
                actionBarLayoutWrapper.setMenuLayout(R.menu.quotes_red_heart);
            else
                actionBarLayoutWrapper.setMenuLayout(R.menu.activity_main);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        quoteManager.saveWaitingQuotes();
        analytics.updateUserValue("Number of favorites", quoteManager.getFavoriteQuotesCount());
        analytics.dispose();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        actionBarLayoutWrapper.inflate(menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_like:
                Quote quote = quotePagerWrapper.getCurrentQuote();
                if (quote != null)
                    quoteManager.quoteLikeClicked(quote);
                updateActionBarLayout();
                return true;

            case R.id.action_wikipedia:
                analytics.sendEvent(new WikiEvent(quotePagerWrapper.getCurrentQuote()));

                String url = quotePagerWrapper.getCurrentQuote().getWikipediaLink();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
                return true;

            case R.id.action_help:
                showHelpView();
                return true;

            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_share:
                ShareUtil.getInstance().shareQuote(this, quotePagerWrapper.getCurrentQuote());
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerWrapper.isExpanded())
            drawerWrapper.collapse();
        else
            super.onBackPressed();
    }
}