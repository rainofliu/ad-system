package com.ajin.ad.constant;

import lombok.Getter;

/**
 * 创意类型对应的物料类型，如jpg...
 */
@Getter
public enum CreativeMaterialType {
    JPG(1,"jpg"),
    BMP(2,"bmp"),

    MP4(3,"mp4"),
    AVI(4,"avi"),

    TEXT(5,"text");

    private int type;

    private String desc;

    CreativeMaterialType(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }}
