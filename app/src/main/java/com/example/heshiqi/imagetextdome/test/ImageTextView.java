package com.example.heshiqi.imagetextdome.test;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;


/**
 * Created by heshiqi on 17/10/25.
 * <p>
 * 用于显示图文混排的内容
 */

public class ImageTextView extends TextView {

    /**
     * 大图宽度不超过235px，超过则固定宽度235px，高度按原尺寸等比缩放
     */
    public static final int BIG_MAX_WIDTH = 235;
    /**
     * 小图高度不超过18px，超过则固定高度18px，宽度按原尺寸等比缩放
     */
    public static final int SMALL_MAX_WIDTH = 18;

    /**
     * 所要显示的内容,接口返回的元数据
     */
    private String originalContent = "";

    /**
     * 将接口返回的数据经过处理 得到我们所要显示格式的内容 最终要渲染到控件上的
     */
    private String displayContent = "";

    private Map<String, ImageTextEntity> imageTextEntities = new LinkedHashMap<>();

    private int maxImageWidth;

    /**
     * 接受大图点击的回调
     */
    private OnImageClickListener imageClickListener;

    public ImageTextView(Context context) {
        this(context, null);
    }

    public ImageTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        maxImageWidth=ImageTextView.dpToPx(context, BIG_MAX_WIDTH);
    }

    public void setOnImageClickListener(OnImageClickListener imageClickListener) {
        this.imageClickListener = imageClickListener;
    }

    /**
     * 设置所要显示的图文内容
     *
     * @param content
     */
    public void setContent(String content) {
        this.originalContent = content;
        if (parserContent()) {
            displayContent();
        }
    }

    /**
     * 解析接口返回的数据 得到我们要显示的格式文本
     *
     * @return true 解析成功 false 解析失败
     */
    private boolean parserContent() {
        if (TextUtils.isEmpty(originalContent)) {
            return false;
        }
        //替换成html支持的换行符
        originalContent = originalContent.replaceAll("\\[br\\]", "<br>");
        try {
            JSONArray array = new JSONArray(originalContent);
            for (int i = 0; i < array.length(); i++) {
                JSONObject json = array.optJSONObject(i);
                ImageTextEntity entity = new ImageTextEntity();
                int type = json.optInt("type");
                String value = json.optString("value");
                entity.setType(json.optInt("type"));
                entity.setValue(value);
                String[] values = value.split("\\|");
                //  "value": "b|642|531|http://club2.autoimg.cn/g10/M0F/BE/DF/wKgH4FnvDSqABh8uAAEy3eTj0cM238_w642_h531.jpg"
                if (type == 1 && values != null && values.length == 4) {
                    entity.setBig("b".equals(values[0]));
                    entity.setWidth(Integer.parseInt(values[1]));
                    entity.setHeight(Integer.parseInt(values[2]));
                    entity.setImageUrl(values[3]);
                }
                imageTextEntities.put(entity.getKey(), entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        //将所有的实体对象内容拼接成html格式的文本
        StringBuilder sb = new StringBuilder();
        for (String key : imageTextEntities.keySet()) {
            ImageTextEntity entity = imageTextEntities.get(key);
            if (ImageTextEntity.TYPE_TEXT == entity.getType()) {//文本
                sb.append(entity.getValue());
            } else {//图片
                //拼接img标签
                sb.append("<")
                        .append("img")
                        .append(" src=")
                        .append("'")
                        .append(entity.getImageUrl())
                        .append("'")
                        .append(">");
            }
        }

        this.displayContent = sb.toString();
        return true;
    }


    /**
     * 显示图文内容
     */
    private void displayContent() {
        URLImageParser urlParserChebien = new URLImageParser(this, getContext(), maxImageWidth, imageTextEntities);
        Spannable htmlSpan = (Spannable) Html.fromHtml(displayContent, urlParserChebien, null);
        ImageSpan[] imageSpens = htmlSpan.getSpans(0, htmlSpan.length(), ImageSpan.class);
        for (ImageSpan span : imageSpens) {
            int flags = htmlSpan.getSpanFlags(span);
            int start = htmlSpan.getSpanStart(span);
            int end = htmlSpan.getSpanEnd(span);
            String imageUrl = span.getSource();
            ImageTextEntity entity = imageTextEntities.get(imageUrl);
            if (entity == null) {
                continue;
            }
            if (entity.isBig()) {
                //大图设置点击事件
                htmlSpan.setSpan(new ImageClickableSpan(span.getSource(), entity, imageClickListener), start, end, flags);
            }
        }
        setText(htmlSpan);
        setMovementMethod(new ImageMovementMethod(maxImageWidth));
    }

    /**
     * Created by heshiqi on 17/10/30.
     * 处理大图点击事件
     */
    static class ImageClickableSpan extends ClickableSpan {
        OnImageClickListener onImageClickListener;
        String sourceUrl = "";
        ImageTextEntity entity;

        public ImageClickableSpan(String url, ImageTextEntity entity, OnImageClickListener onImageClickListener) {
            this.sourceUrl = url;
            this.entity = entity;
            this.onImageClickListener = onImageClickListener;
        }


        @Override
        public void onClick(View view) {
            if (onImageClickListener != null) {
                onImageClickListener.onImageClick(view, sourceUrl);
            }
        }
    }

    public static class ImageMovementMethod extends LinkMovementMethod {
        private int maxImageWidth;

        //要把按下和抬起的ClickableSpan记录下来  解决按下时点击图片然后移动手指到另一张图片上，这种情况响应的点击事件是最终我们手指抬起时所在的位置，我们要避免这种情况(在抬起手指时通过比对按下和抬起时的ClickableSpan是否是同一个)
        private ClickableSpan downClickableSpan;//按下
        private ClickableSpan upClickableSpan;//抬起

        public ImageMovementMethod(int maxWidth) {
            this.maxImageWidth = maxWidth;
        }

        @Override
        public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
            int action = event.getAction();


            if (action == MotionEvent.ACTION_UP ||
                    action == MotionEvent.ACTION_DOWN) {
                int x = (int) event.getX();
                int y = (int) event.getY();

                x -= widget.getTotalPaddingLeft();
                y -= widget.getTotalPaddingTop();

                x += widget.getScrollX();
                y += widget.getScrollY();

                Layout layout = widget.getLayout();
                int line = layout.getLineForVertical(y);
                int off = layout.getOffsetForHorizontal(line, x);

                ClickableSpan[] link = buffer.getSpans(off, off, ClickableSpan.class);

                if (link.length != 0) {
                    ClickableSpan clickableSpan = link[0];
                    if (action == MotionEvent.ACTION_UP) {
                        upClickableSpan=clickableSpan;
                        if(downClickableSpan==null){
                            return true;
                        }
                        if(!downClickableSpan.equals(upClickableSpan)){
                            return true;
                        }
                        if (clickableSpan instanceof ImageClickableSpan) {
                            ImageTextEntity entity = ((ImageClickableSpan) clickableSpan).entity;
                            int w = ImageTextView.dpToPx(widget.getContext(), entity.getWidth());
                            int displayWidth = w > maxImageWidth ? maxImageWidth : w;
                            if (event.getX() <= displayWidth) {
                                //只有在图片当中点击才能响应
                                clickableSpan.onClick(widget);
                            }
                        }
                        downClickableSpan=null;
                        upClickableSpan=null;
                    } else if (action == MotionEvent.ACTION_DOWN) {
                          downClickableSpan=clickableSpan;
                    }
                    return true;
                } else {
                    downClickableSpan=null;
                    upClickableSpan=null;
                    Selection.removeSelection(buffer);
                }
            }

            return super.onTouchEvent(widget, buffer, event);
        }
    }

    public static int dpToPx(Context context, float dp) {
        return context == null ? -1 : (int) (dp * context.getResources().getDisplayMetrics().density + 0.5F);
    }

    public interface OnImageClickListener {

        void onImageClick(View view, String sourceUrl);

    }
}
