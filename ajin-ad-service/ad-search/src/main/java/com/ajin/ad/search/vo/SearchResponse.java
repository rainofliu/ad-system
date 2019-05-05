package com.ajin.ad.search.vo;

import com.ajin.ad.index.creative.CreativeObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: ajin
 * @Date: 2019/4/25 09:18
 * 请求响应对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchResponse {

    /**
     * key :广告位Code
     * value:多个广告创意
     */
    public Map<String, List<Creative>> adSlot2Ads = new HashMap<>();

    /**
     * 广告创意
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Creative {

        private Long adId;
        private String adUrl;
        private Integer height;
        private Integer width;
        private Integer type;
        private Integer materialType;

        /**
         * 展示监测 url 告诉第三方已经展示了url
         */
        private List<String> showMonitorUrl =
                Arrays.asList("www.baidu.com", "www.alibaba.com");

        /**
         * 点击监测 url  媒体方告诉url已经被点击了
         */
        private List<String> clickMonitorUrl =
                Arrays.asList("www.baidu.com", "www.alibaba.com");

    }

    /**
     * 将索引对象CreativeObject转化成Creative
     *
     * @param object
     * @return
     */
    public static Creative convert(CreativeObject object) {

        Creative creative = new Creative();
        creative.setAdId(object.getAdId());
        creative.setAdUrl(object.getAdUrl());
        creative.setWidth(object.getWidth());
        creative.setHeight(object.getHeight());
        creative.setType(object.getType());
        creative.setMaterialType(object.getMaterialType());

        return creative;
    }
}
