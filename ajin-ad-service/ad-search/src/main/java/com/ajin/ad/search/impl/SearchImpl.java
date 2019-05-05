package com.ajin.ad.search.impl;

import com.ajin.ad.index.CommonStatus;
import com.ajin.ad.index.DataTable;
import com.ajin.ad.index.adunit.AdUnitIndex;
import com.ajin.ad.index.adunit.AdUnitObject;
import com.ajin.ad.index.creative.CreativeIndex;
import com.ajin.ad.index.creative.CreativeObject;
import com.ajin.ad.index.creative_unit.CreativeUnitIndex;
import com.ajin.ad.index.district.UnitDistrictIndex;
import com.ajin.ad.index.interest.UnitItIndex;
import com.ajin.ad.index.keyword.UnitKeywordIndex;
import com.ajin.ad.search.ISearch;
import com.ajin.ad.search.vo.SearchRequest;
import com.ajin.ad.search.vo.SearchResponse;
import com.ajin.ad.search.vo.feature.DistrictFeature;
import com.ajin.ad.search.vo.feature.FutureRelations;
import com.ajin.ad.search.vo.feature.ItFeature;
import com.ajin.ad.search.vo.feature.KeywordFeature;
import com.ajin.ad.search.vo.media.AdSlot;
import com.alibaba.fastjson.JSON;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @Author: ajin
 * @Date: 2019/4/25 10:06
 */
@Component
@Slf4j
public class SearchImpl implements ISearch {

    public SearchResponse fallback(SearchRequest request, Throwable throwable) {
        return null;

    }

    @Override
    @HystrixCommand(fallbackMethod = "fallback")
    public SearchResponse fetchAds(SearchRequest request) {

        // 请求的广告位信息
        List<AdSlot> adSlots = request.getRequestInfo().getAdSlots();
        // 三个Feature
        KeywordFeature keywordFeature =
                request.getFeatureInfo().getKeywordFeature();
        ItFeature itFeature =
                request.getFeatureInfo().getItFeature();
        DistrictFeature districtFeature =
                request.getFeatureInfo().getDistrictFeature();

        FutureRelations relations = request.getFeatureInfo().getRelations();

        // 构造响应对象SearchResponse
        SearchResponse response = new SearchResponse();

        // key:广告位code  value ：多个广告创意
        Map<String, List<SearchResponse.Creative>> adSlot2Ads = response.getAdSlot2Ads();

        for (AdSlot adSlot : adSlots) {

            Set<Long> targetUnitIdSet;

            // 根据流量类型获取初始的  AdUnit

            Set<Long> adUnitIdSet = DataTable.of(AdUnitIndex.class).match(adSlot.getPosition());

            if (relations == FutureRelations.AND) {

                filterKeywordFeature(adUnitIdSet, keywordFeature);
                filterDistrictFeature(adUnitIdSet, districtFeature);
                filterItFeature(adUnitIdSet, itFeature);

                targetUnitIdSet = adUnitIdSet;
            } else {

                targetUnitIdSet = getORRelationUnitIds(
                        adUnitIdSet,
                        keywordFeature,
                        districtFeature,
                        itFeature);
            }
            // 获取AdUnitObject的List集合
            List<AdUnitObject> unitObjects = DataTable.of(AdUnitIndex.class).fetchCollection(targetUnitIdSet);

            fetchAdUnitAndPlanStatus(unitObjects, CommonStatus.VALID);

            // 获取创意id
            List<Long> adIds = DataTable.of(CreativeUnitIndex.class)
                    .selectAds(unitObjects);

            // 根据创意id获取创意对象
            List<CreativeObject> creatives = DataTable.of(CreativeIndex.class)
                    .fetch(adIds);

            // 通过 AdSlot实现对CreativeObject的过滤
            filterCreativeByAdSlot(
                    creatives,
                    adSlot.getWidth(),
                    adSlot.getHeight(),
                    adSlot.getType()
            );
            // 目前是只返回一个广告创意对象，方便测试,可拓展
            adSlot2Ads.put(
                    adSlot.getAdSlotCode(),
                    buildCreativeResponse(creatives)
            );
        }
        log.info("fetchAds : {}-{}",
                JSON.toJSONString(request),
                JSON.toJSONString(response));

        return response;
    }

    private Set<Long> getORRelationUnitIds(Set<Long> adUnitIdSet,
                                           KeywordFeature keywordFeature,
                                           DistrictFeature districtFeature,
                                           ItFeature itFeature) {
        if (CollectionUtils.isNotEmpty(adUnitIdSet)) {
            return Collections.emptySet();
        }
        // 副本
        Set<Long> keywordUnitIdSet = new HashSet<>(adUnitIdSet);
        Set<Long> districtUnitIdSet = new HashSet<>(adUnitIdSet);
        Set<Long> itUnitIdSet = new HashSet<>(adUnitIdSet);

        filterKeywordFeature(keywordUnitIdSet, keywordFeature);
        filterDistrictFeature(districtUnitIdSet, districtFeature);
        filterItFeature(itUnitIdSet, itFeature);

        // 求并集
        return new HashSet<>(
                CollectionUtils.union(
                        CollectionUtils.union(keywordUnitIdSet, districtUnitIdSet), itUnitIdSet
                )
        );
    }


    private void filterKeywordFeature(
            Collection<Long> adUnitIds, KeywordFeature keywordFeature) {

        // adUnitIds为空  直接返回
        if (CollectionUtils.isEmpty(adUnitIds)) {
            return;
        }
        // 判断推广单元的关键词是否为空
        if (CollectionUtils.isNotEmpty(keywordFeature.getKeywords())) {

            CollectionUtils.filter(
                    adUnitIds,
                    // 判断媒体方请求的关键词是否存在索引对象中
                    adUnitId ->
                            DataTable.of(UnitKeywordIndex.class)
                                    .match(adUnitId, keywordFeature.getKeywords())
            );

        }

    }

    private void filterDistrictFeature(Collection<Long> adUnitIds, DistrictFeature districtFeature) {

        if (CollectionUtils.isEmpty(adUnitIds)) {
            return;
        }

        if (CollectionUtils.isNotEmpty(districtFeature.getDistricts())) {

            CollectionUtils.filter(
                    adUnitIds,
                    adUnitId ->
                            DataTable.of(UnitDistrictIndex.class)
                                    .match(adUnitId, districtFeature.getDistricts())
            );
        }
    }

    private void filterItFeature(Collection<Long> adUnitIds, ItFeature itFeature) {

        if (CollectionUtils.isEmpty(adUnitIds)) {
            return;
        }

        if (CollectionUtils.isNotEmpty(itFeature.getIts())) {

            CollectionUtils.filter(
                    adUnitIds,
                    adUnitId ->
                            DataTable.of(UnitItIndex.class)
                                    .match(adUnitId, itFeature.getIts())
            );
        }
    }

    /**
     * 判断推广单元和推广计划是否是有效状态
     */
    private void fetchAdUnitAndPlanStatus(List<AdUnitObject> unitObjects, CommonStatus status) {

        if (CollectionUtils.isEmpty(unitObjects)) {
            return;
        }

        CollectionUtils.filter(
                unitObjects,
                unitObject -> unitObject.getUnitStatus().equals(status.getStatus())
                        && unitObject.getAdPlanObject().getPlanStatus().equals(status.getStatus())
        );

    }

    /**
     * 通过媒体方请求信息中的AdSlot实现对广告创意的过滤
     */
    private void filterCreativeByAdSlot(List<CreativeObject> creativeObjects,
                                        Integer width,
                                        Integer height,
                                        List<Integer> type) {
        if (CollectionUtils.isEmpty(creativeObjects)) {
            return;
        }

        CollectionUtils.filter(
                creativeObjects,
                creativeObject ->
                        creativeObject.getAuditStatus().equals(CommonStatus.VALID.getStatus())
                                && creativeObject.getWidth().equals(width)
                                && creativeObject.getHeight().equals(height)
                                && type.contains(creativeObject.getType())
        );
    }

    /**
     * 将CreativeObject转换成Creative
     */
    private List<SearchResponse.Creative> buildCreativeResponse(List<CreativeObject> creatives) {

        if (CollectionUtils.isEmpty(creatives)) {
            return Collections.emptyList();
        }

        CreativeObject randomObject = creatives.get(
                Math.abs(new Random().nextInt()) % creatives.size()
        );

        return Collections.singletonList(
                SearchResponse.convert(randomObject)
        );
    }
}
