package com.example.heshiqi.imagetextdome.test;

/**
 * Created by heshiqi on 17/10/30.
 */

public class ImageTextEntity {


    public static final int TYPE_TEXT = 0;
    public static final int TYPE_IMAGE = 1;

    /**
     * 0  文本  1 图片
     */
    private int type;

    /**
     * 内容值 图片类型|原图宽|原图高|原图链接
     * 图片类型： s 小图 b 大图
     * 原图宽： 单位px
     * 原图高： 单位px
     * 原图链接 ： 站内图片链接
     * 图片参数以竖线分割
     */
    private String value;
//  b|642|531|http://club2.autoimg.cn/g10/M0F/BE/DF/wKgH4FnvDSqABh8uAAEy3eTj0cM238_w642_h531.jpg
    /**
     * 是否为大图
     */
    private boolean isBig;

    /**
     * 原图宽
     */
    private int width;

    /**
     * 原图高
     */
    private int height;

    /**
     * 站内图片链接
     */
    private String imageUrl;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isBig() {
        return isBig;
    }

    public void setBig(boolean big) {
        isBig = big;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getKey() {
        return type == 0 ? value : imageUrl;
    }
}
