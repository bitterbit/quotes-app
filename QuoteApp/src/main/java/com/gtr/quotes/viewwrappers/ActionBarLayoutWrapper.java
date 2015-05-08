package com.gtr.quotes.viewwrappers;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Menu;
import android.view.MenuInflater;

public class ActionBarLayoutWrapper {

    private Activity activity;
    private ActionBar actionBar;

    private int currentMenuRes;

    private int defaultMenuRes = -1;
    private String defualtTitle = "";

    private int color = Color.WHITE;

    private boolean isDarken = false;

    public ActionBarLayoutWrapper(Activity activity, ActionBar actionBar, int defaultMenuRes) {
        this.activity = activity;
        this.actionBar = actionBar;

        this.defaultMenuRes = defaultMenuRes;
        this.currentMenuRes = defaultMenuRes;

        defualtTitle = activity.getString(com.gtr.quotes.R.string.app_name);
    }

    public void setDefaultMenuRes(int menuRes) {
        this.defaultMenuRes = menuRes;
    }

    public void backToDefault() {
        if (defaultMenuRes != -1)
            setMenuLayout(defaultMenuRes);
    }

    public void backToDefaultTitle() {
        setTitle(defualtTitle);
    }

    public void setMenuLayout(int resLayout) {
        currentMenuRes = resLayout;
        activity.invalidateOptionsMenu();
    }

    public void setTitle(String title) {
        actionBar.setTitle(title);
    }

    public void show() {
        actionBar.show();
    }

    public void hide() {
        actionBar.hide();
    }

    public boolean isShowing() {
        return actionBar.isShowing();
    }

    public void setColor(int color) {
        this.color = color;
        if (isDarken == false) {
            actionBar.setBackgroundDrawable(new ColorDrawable(color));
        }
    }

    public int getColor() {
        return this.color;
    }

    public void setDarkMode(boolean shouldDarken) {
        isDarken = shouldDarken;
        if (isDarken) {
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));
        } else {
            actionBar.setBackgroundDrawable(new ColorDrawable(color));
        }
    }

    public void inflate(Menu menu) {
        MenuInflater inflater = activity.getMenuInflater();
        inflater.inflate(currentMenuRes, menu);
    }


}
