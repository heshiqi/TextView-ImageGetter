package com.example.heshiqi.imagetextdome.test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.heshiqi.imagetextdome.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Map;


/**
 * Created by heshiqi on 17/10/25.
 * 用于获取html文本中<img>标签中的图片
 */

public class URLImageParser implements Html.ImageGetter {

    private final TextView mTextView;
    private Context mContext;
    private int imageHeight;//图片的高度 以文本的高度为基准
    private Map<String, ImageTextEntity> imageTextEntities;
    private int maxWidth;
    private int canvasOffset;//绘制图片时Y轴的偏移量 保证与文本的上端对齐

    public URLImageParser(TextView ctx, Context tv,int maxWidth, Map<String, ImageTextEntity> imageTextEntities) {
        this.mTextView = ctx;
        this.mContext = tv;
        this.imageTextEntities = imageTextEntities;
        this.maxWidth = maxWidth;
        Paint.FontMetrics fontMetrics = mTextView.getPaint().getFontMetrics();
        canvasOffset=(int) (Math.abs(fontMetrics.top)-Math.abs(fontMetrics.ascent));
//        imageHeight = (int) (Math.abs(fontMetrics.top) + Math.abs(fontMetrics.bottom));
        imageHeight = (int) Math.ceil(mTextView.getTextSize());
    }

    @Override
    public Drawable getDrawable(final String source) {
        final UrlDrawable urlDrawable = new UrlDrawable();
        ImageLoader.getInstance().loadImage(source, getDisplayImageOptions(R.mipmap.ic_launcher),
                new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        final Bitmap LoadBitmap= BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.ic_launcher);
                        displayImage(source,urlDrawable,LoadBitmap);
                    }

                    @Override
                    public void onLoadingComplete(final String imageUri, View view, Bitmap loadedImage) {
                        displayImage(imageUri,urlDrawable,loadedImage);
                    }
                });
        return urlDrawable;
    }


    private void displayImage(String imageUri, UrlDrawable urlDrawable,Bitmap loadedImage){
        if (imageTextEntities == null) {
            return;
        }
        ImageTextEntity entity = imageTextEntities.get(imageUri);
        if (entity == null) {
            return;
        }
        final BitmapDrawable drawable = new BitmapDrawable(mContext.getResources(), loadedImage);
        int displayWidth;
        int displayHeight;
        int originalHeight = entity.getHeight();
        int originalWidth = entity.getWidth();
        if (entity.isBig()) {
            int w = ImageTextView.dpToPx(mContext, entity.getWidth());
            displayWidth = w > maxWidth ? maxWidth : w;
            displayHeight = originalHeight * (displayWidth) / originalWidth;
        } else {
            displayWidth = originalWidth * (imageHeight) / originalHeight;
            displayHeight = imageHeight;
        }
        Rect rect = new Rect(0, 0, displayWidth, displayHeight);
        drawable.setBounds(rect);
        urlDrawable.setBounds(rect);
        urlDrawable.setDrawable(drawable);
        mTextView.setText(mTextView.getText());
        mTextView.invalidate();
    }

    public static final DisplayImageOptions getDisplayImageOptions(int placeholderImage) {
        DisplayImageOptions.Builder builder = getBaseDisplayImageOptionsBuilder();
        builder.showImageForEmptyUri(placeholderImage)
                .showImageOnFail(placeholderImage)
                .showImageOnLoading(placeholderImage);
        builder.bitmapConfig(Bitmap.Config.RGB_565);
        return builder.build();
    }

    private static final DisplayImageOptions.Builder getBaseDisplayImageOptionsBuilder() {
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder().cacheOnDisk(true).cacheInMemory(true).bitmapConfig(Bitmap.Config.RGB_565)// 图片默认低彩显示,省内存,如果觉得不够亮,可以更改
                .considerExifParams(true).imageScaleType(ImageScaleType.IN_SAMPLE_INT);
        return builder;
    }

    private class UrlDrawable extends BitmapDrawable {

        private Drawable drawable;

        @Override
        public void draw(Canvas canvas) {
            if (drawable != null) {
                canvas.save();
                canvas.translate(0, -canvasOffset);
                drawable.draw(canvas);
                canvas.restore();
            }
        }

        public Drawable getDrawable() {
            return drawable;
        }

        public void setDrawable(Drawable drawable) {
            this.drawable = drawable;
        }
    }
}