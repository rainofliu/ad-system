package com.ajin.ad.search.vo.media;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: ajin
 * @Date: 2019/4/24 21:25
 * 媒体方的终端信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class App {
    /**
     * 应用编码
     */
    private String appCode;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 应用包名
     * */
    private String packageName;

    /**
     * activity 名称
     * */
    private String activityName;

}
