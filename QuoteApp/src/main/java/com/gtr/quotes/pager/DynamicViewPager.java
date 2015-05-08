package com.gtr.quotes.pager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.AttributeSet;

import com.gtr.quotes.viewwrappers.ActionBarLayoutWrapper;

import java.util.Random;

public class DynamicViewPager extends PerfectViewPager {

    private ActionBarLayoutWrapper actionBar = null;
    private int baseColorIndex = 0;
    private static final int JUMP = 4;

    private static float freq = 0.1f;

    private boolean hasLoaded = false;

    public DynamicViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        initBackground();
    }

    public DynamicViewPager(Context context) {
        super(context);
        initBackground();
    }

    public void setActionBarWrapper(ActionBarLayoutWrapper actionBar) {
        this.actionBar = actionBar;
    }

    public void hideChildren() {
        setChildrenVisibilty(false);
    }

    public void showChildren() {
        setChildrenVisibilty(true);
    }

    private void setChildrenVisibilty(boolean visible) {
        float opacity = visible ? 1 : 0.01f;

        for (int i = 0; i < this.getChildCount(); i++) {
            this.getChildAt(i).setAlpha(opacity);
        }
    }

    private void initBackground() {
        Random rnd = new Random();
        baseColorIndex = rnd.nextInt(1000);
        setGradient(getColor(baseColorIndex), getColor(baseColorIndex + JUMP));
        setActionBarColor(getColorActionBar(baseColorIndex));

        // Set the transition effect
        //this.setTransitionEffect(TransitionEffect.CubeOut);
        this.setDrawingCacheEnabled(true);
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    private void setGradient(int startColor, int endColor) {
        ShapeDrawable mDrawable = new ShapeDrawable(new RectShape());
        Shader linearShader = new LinearGradient(0, 0, 0, this.getHeight(), startColor, endColor, Shader.TileMode.CLAMP);
        mDrawable.getPaint().setShader(linearShader);

        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            setBackgroundDrawable(mDrawable);
        } else {
            setBackground(mDrawable);
        }
    }

    private void setActionBarColor(int color) {
        if (actionBar != null) {
            actionBar.setColor(color);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (!hasLoaded) {
            hasLoaded = true;
            initBackground();
        }
    }

    @Override
    public void onPageScrolled(int pageNum, float arg1, int arg2) {

        super.onPageScrolled(pageNum, arg1, arg2);

        float index = (pageNum * 10) + (arg1 * 10);
        index /= 2;
        index += baseColorIndex;

        setGradient(getColor(index), getColor(index + JUMP));
        setActionBarColor(getColorActionBar(index));

    }

    private static int getColor(float i) {
        return getColor(i, 140);
    }

    private static int getColor(float i, int center) {
        int width = 255 - center;

        int red = (int) (Math.sin(freq * i) * width + center);
        int green = (int) (Math.sin(freq * i + 4) * width + center);
        int blue = (int) (Math.sin(freq * i + 8) * width + center);

        return Color.argb(200, red, green, blue);
    }

    private static int getColorActionBar(float i) {
        return darken(getColor(i), 0.7);
    }

    private static int darken(int color, double fraction) {

        int newRed = (int) Math.round(Color.red(color) * fraction);
        int newGreen = (int) Math.round(Color.green(color) * fraction);
        int newBlue = (int) Math.round(Color.blue(color) * fraction);

        return Color.argb(Color.alpha(color), newRed, newGreen, newBlue);

    }


}
