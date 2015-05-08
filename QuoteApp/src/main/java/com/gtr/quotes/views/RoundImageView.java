package com.gtr.quotes.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Transformation;

public class RoundImageView extends FrameLayout {

    private ImageView imageView;
    private Picasso cacheManager;

    public RoundImageView(Context context) {
        super(context);
        init(context);
    }

    public RoundImageView(Context context, String url) {
        super(context);
        init(context);
        this.setImageUrl(url);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }


    private void init(Context context) {
        imageView = new ImageView(context);
        this.addView(imageView);
        cacheManager = Picasso.with(context);
        //cacheManager.setDebugging(true);
    }


    public void setImageUrl(String url) {
        RequestCreator request = cacheManager.load(url);
        request.placeholder(com.gtr.quotes.R.drawable.unknown_person);
        request.transform(new CircleTransformation());
        request.into(imageView);
    }

    public void setImageUri(Uri uri) {
        RequestCreator request = cacheManager.load(uri);
        request.placeholder(com.gtr.quotes.R.drawable.unknown_person);
        request.transform(new CircleTransformation());
        request.into(imageView);
    }

    public void setImageRes(int res) {
        RequestCreator request = cacheManager.load(res);
        request.placeholder(com.gtr.quotes.R.drawable.unknown_person);
        request.transform(new CircleTransformation());
        request.into(imageView);
    }

    private class CircleTransformation implements Transformation {

        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            Bitmap finalBitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(finalBitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float radius = size / 2f;
            canvas.drawCircle(radius, radius, radius, paint);

            source.recycle();

            return finalBitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }

}
