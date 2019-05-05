package com.ajin.ad.index;

import lombok.Getter;

/**
 * @Author: ajin
 * @Date: 2019/4/25 11:25
 */
@Getter
@SuppressWarnings("all")
public enum CommonStatus {

    VALID(1, "有效状态"),
    INVALID(0, "无效状态");


    private Integer status;
    private String desc;

    CommonStatus(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }
}
