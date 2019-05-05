package com.ajin.ad.index;

import lombok.Getter;

/**
 * 定义索引中的数据层级枚举类
 */
@Getter
@SuppressWarnings("all")
public enum DataLevel {

    LEVEL2("2","level2"),
    LEVEL3("3","level3"),
    LEVEL4("4","level4");

    private String level;
    private String desc;

    private DataLevel(String level, String desc) {
        this.level = level;
        this.desc = desc;
    }

}
