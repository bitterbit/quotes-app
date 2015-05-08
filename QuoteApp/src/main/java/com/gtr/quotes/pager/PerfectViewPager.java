package com.gtr.quotes.pager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import com.gtr.jazzyviewpager.JazzyViewPager;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class PerfectViewPager extends JazzyViewPager {

    private static final int AUTO_SCROLL_VELOCITY = 1000;
    private static final int MAX_SETTLE_DURATION = 800; // ms

    private Method setCurrentItemMethod;

    public PerfectViewPager(Context context) {
        super(context);
        init();
    }

    public PerfectViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void scrollTo(int index) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        setCurrentItemMethod.invoke(this, index, true, true, AUTO_SCROLL_VELOCITY);
    }

    private void setMaxSettleField() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field max_settle_field = ViewPager.class.getDeclaredField("MAX_SETTLE_DURATION");
        max_settle_field.setAccessible(true);
        max_settle_field.set(int.class, MAX_SETTLE_DURATION);
    }

    public void scrollToItem(int itemIndex) {
        try {
            scrollTo(itemIndex);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        try {
            setCurrentItemMethod = ViewPager.class.getDeclaredMethod("setCurrentItemInternal", int.class, boolean.class, boolean.class, int.class);
            setCurrentItemMethod.setAccessible(true);
            setMaxSettleField();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

}
