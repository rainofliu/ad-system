package com.ajin.ad.search.vo;

import com.ajin.ad.search.vo.feature.DistrictFeature;
import com.ajin.ad.search.vo.feature.FutureRelations;
import com.ajin.ad.search.vo.feature.ItFeature;
import com.ajin.ad.search.vo.feature.KeywordFeature;
import com.ajin.ad.search.vo.media.AdSlot;
import com.ajin.ad.search.vo.media.App;
import com.ajin.ad.search.vo.media.Device;
import com.ajin.ad.search.vo.media.Geo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: ajin
 * @Date: 2019/4/24 21:16
 * 媒体方请求对象的定义
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequest {

    /**
     * 媒体方请求标识
     */
    private String mediaId;

    /**
     * 媒体方请求基本信息
     */

    private RequestInfo requestInfo;

    /**
     * 匹配信息
     */
    private FeatureInfo featureInfo;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RequestInfo {

        private String requestId;

        private List<AdSlot> adSlots;

        private App app;

        private Geo geo;

        private Device device;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FeatureInfo {

        private KeywordFeature keywordFeature;
        private ItFeature itFeature;
        private DistrictFeature districtFeature;

        /**
         * 默认是AND
         */
        private FutureRelations relations = FutureRelations.AND;

    }


}
