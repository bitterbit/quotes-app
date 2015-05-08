package com.gtr.quotes.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gtr.quotes.util.PixelUtils;
import com.gtr.quotes.viewwrappers.ActionBarLayoutWrapper;

public class ShowcaseView extends RelativeLayout {

    private ActionBarLayoutWrapper actionBar = null;
    private final int WIDTH = PixelUtils.pxFromDp(getContext(), 150);
    private boolean isInitialized = false;

    public ShowcaseView(Context context) {
        super(context);
    }

    public ShowcaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ShowcaseView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setActionBar(ActionBarLayoutWrapper actionBar) {
        this.actionBar = actionBar;
    }

    private void initialize() {
        isInitialized = true;
        this.setGravity(Gravity.CENTER);
        this.addView(getHeartTooltip());
        this.addView(getArrowTooltip());
        this.addView(getSwipeTooltip());
        this.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ShowcaseView.this.setVisibility(View.GONE);

                if (actionBar != null)
                    actionBar.setDarkMode(false);
                // Block touches
                return true;
            }
        });
    }

    public void show() {

        if (!isInitialized)
            initialize();

        if (actionBar != null)
            actionBar.setDarkMode(true);

        this.setVisibility(View.VISIBLE);
    }

    private View getArrowTooltip() {
        TextView tooltip = new TextView(getContext());
        tooltip.setText("Tap to open favorties drawer");
        tooltip.setTextColor(Color.WHITE);
        tooltip.setTextSize(20);
        tooltip.setGravity(Gravity.CENTER);

        LayoutParams lp = new LayoutParams(WIDTH, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        lp.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        lp.setMargins(PixelUtils.pxFromDp(getContext(), 100), 0, 0, PixelUtils.pxFromDp(getContext(), 80));
        tooltip.setLayoutParams(lp);
        return tooltip;
    }

    private View getHeartTooltip() {
        TextView tooltip = new TextView(getContext());
        tooltip.setText("Tap the heart to like a quote");
        tooltip.setTextColor(Color.WHITE);
        tooltip.setTextSize(20);
        tooltip.setGravity(Gravity.CENTER);

        LayoutParams lp = new LayoutParams(WIDTH, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        lp.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        lp.setMargins(0, PixelUtils.pxFromDp(getContext(), 60), PixelUtils.pxFromDp(getContext(), 60), 0);
        tooltip.setLayoutParams(lp);
        return tooltip;
    }

    private View getSwipeTooltip() {
        TextView tooltip = new TextView(getContext());
        tooltip.setText("Swipe between quotes >>");
        tooltip.setTextColor(Color.WHITE);
        tooltip.setTextSize(20);
        tooltip.setGravity(Gravity.CENTER);

        LayoutParams lp = new LayoutParams(WIDTH * 2, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        tooltip.setLayoutParams(lp);
        return tooltip;
    }

}
