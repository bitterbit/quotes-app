package com.gtr.quotes.util;

import android.content.Context;

public class PixelUtils {

    public static float dpFromPx(Context context, float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static int pxFromDp(Context context, float dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }
}
