package com.ajin.ad.search.vo.media;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Auther: ajin
 * @Date: 2019/4/24 21:30
 * <description>媒体方的设备信息</description>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Device {

    /**
     * 设备ID
     */
    private String deviceCode;

    /**
     * mac
     */
    private String mac;

    /**
     * IP
     */
    private String ip;

    /**
     * 机型编码
     */
    private String model;

    /**
     * 分辨率尺寸
     */
    private String displaySize;


    /**
     * 屏幕尺寸
     */
    private String screenSize;

    /**
     * 设备序列号
     */
    private String serialName;
}
