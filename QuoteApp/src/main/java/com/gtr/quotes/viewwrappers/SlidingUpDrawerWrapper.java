package com.gtr.quotes.viewwrappers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Point;
import android.os.SystemClock;
import android.view.Display;
import android.view.View;

import com.gtr.quotes.R;
import com.gtr.quotes.pager.DynamicViewPager;
import com.gtr.quotes.tracking.AnalyticsHandler;
import com.gtr.quotes.tracking.FavoritesDrawerOpenEvent;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener;

public class SlidingUpDrawerWrapper {

    public interface Listener {
        public void onPanelCollapsed();
    }

    private Activity activity;
    private Listener listener = null;
    private SlidingUpPanelLayout slidingPanel;

    private ActionBarLayoutWrapper barWrapper;

    private AnalyticsHandler analyticsHandler;

    private int drawerFade = 0x44;

    public SlidingUpDrawerWrapper(Activity activity, ActionBarLayoutWrapper barWrapper, AnalyticsHandler analyticsHandler) {
        this.analyticsHandler = analyticsHandler;
        this.activity = activity;
        this.barWrapper = barWrapper;

        slidingPanel = (SlidingUpPanelLayout) activity.findViewById(R.id.sliding_layout);
        slidingPanel.setPanelSlideListener(new PanelListener());
        slidingPanel.setDragView(activity.findViewById(R.id.drag_view));
    }

    public boolean isExpanded() {
        return slidingPanel.isExpanded();
    }

    public void expand() {
        slidingPanel.expandPane();
    }

    public void collapse() {
        slidingPanel.collapsePane();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    private int getScreenWidth() {
        Display display = activity.getWindowManager().getDefaultDisplay();
        if (android.os.Build.VERSION.SDK_INT >= 13) {
            Point size = new Point();
            display.getSize(size);
            return size.x;
        } else {
            return display.getWidth();
        }
    }

    private class PanelListener implements PanelSlideListener {

        private View dragView = null;

        // The view used for dragging the panel
        private View dragViewIcon = null;

        private int screenWidth = -1;

        private View dragViewText = null;

        private PanelEventHandler panelEventHandler;

        // The view underneeth the panel
        private DynamicViewPager underView = null;

        public PanelListener() {
            panelEventHandler = new PanelEventHandler();
            screenWidth = getScreenWidth();

            dragView = activity.findViewById(R.id.drag_view);

            dragViewIcon = activity.findViewById(R.id.drag_view_icon);
            dragViewIcon.setRotation(180);

            dragViewText = activity.findViewById(R.id.drag_view_text);
            dragViewText.setAlpha(0);

            underView = (DynamicViewPager) activity.findViewById(R.id.pager);
        }

        @Override
        public void onPanelSlide(View panel, float slideOffset) {

            screenWidth = getScreenWidth();

            int alpha = drawerFade - (int) (slideOffset * drawerFade);
            dragView.setBackgroundColor(Color.argb(alpha, 0, 0, 0));

            dragViewIcon.setRotation(slideOffset * 180);
            float halfScreen = screenWidth / 2.7f;
            float x = ((-1) * (halfScreen - (halfScreen * slideOffset)));
            dragViewIcon.setX(x);

            dragViewText.setAlpha(1 - slideOffset);

            // The way up
            if (slideOffset < 0.5 && barWrapper.isShowing()) {
                barWrapper.hide();
                underView.hideChildren();
            }
            // The way down
            if (slideOffset > 0.5 && barWrapper.isShowing() == false) {
                barWrapper.show();
                underView.showChildren();
            }
        }

        @Override
        public void onPanelCollapsed(View panel) {
            barWrapper.backToDefaultTitle();
            barWrapper.backToDefault();
            if (listener != null) {
                listener.onPanelCollapsed();
            }
            panelEventHandler.panelClosed();
        }

        @Override
        public void onPanelExpanded(View panel) {
            barWrapper.setTitle("Favorites");
            barWrapper.setMenuLayout(R.menu.favorites_menu);
            panelEventHandler.panelOpened();
        }

        @Override
        public void onPanelAnchored(View panel) {
        }

    }

    private class PanelEventHandler {
        private double lastTick = 0;

        public PanelEventHandler() {
            lastTick = SystemClock.uptimeMillis();
        }

        public void panelOpened() {
            lastTick = SystemClock.uptimeMillis();
        }

        public void panelClosed() {
            analyticsHandler.sendEvent(new FavoritesDrawerOpenEvent(SystemClock.uptimeMillis() - lastTick));
        }
    }

}
