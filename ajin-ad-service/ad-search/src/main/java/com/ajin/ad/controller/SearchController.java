package com.ajin.ad.controller;

import com.ajin.ad.annotation.IgnoreResponseAdvice;
import com.ajin.ad.client.SponsorClient;
import com.ajin.ad.client.vo.AdPlan;
import com.ajin.ad.client.vo.AdPlanGetRequest;
import com.ajin.ad.search.ISearch;
import com.ajin.ad.search.vo.SearchRequest;
import com.ajin.ad.search.vo.SearchResponse;
import com.ajin.ad.vo.CommonResponse;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @Auther: ajin
 * @Date: 2019/4/12 10:41
 */
@Slf4j
@RestController
public class SearchController {

    private final RestTemplate restTemplate;

    private final SponsorClient sponsorClient;

    private final ISearch search;

    @Autowired
    public SearchController(RestTemplate restTemplate, SponsorClient sponsorClient, ISearch search) {
        this.restTemplate = restTemplate;
        this.sponsorClient = sponsorClient;
        this.search = search;
    }

    /**
     * 媒体方请求获得广告创意
     **/
    @PostMapping("/fetchAds")
    public SearchResponse fetchAds(@RequestBody SearchRequest request) {

        log.info("ad-search : fetchAds -> {}",
                JSON.toJSONString(request));

        return search.fetchAds(request);

    }

    /**
     * 使用feign来调用广告投放系统的接口
     */
    @IgnoreResponseAdvice
    @PostMapping("/getAdPlans")
    public CommonResponse<List<AdPlan>> getAdPlans(@RequestBody AdPlanGetRequest request) {
        log.info("ad-search getAdPlansByFeign ->{}", JSON.toJSONString(request));
        return sponsorClient.getAdPlans(request);
    }

    @SuppressWarnings("all")
    // 不想使用统一的响应
    @IgnoreResponseAdvice
    @PostMapping("/getAdPlansByRibbon")
    public CommonResponse<List<AdPlan>> getAdPlansByRibbon(@RequestBody AdPlanGetRequest request) {

        log.info("ad-search getAdPlanByRibbon -> {}", JSON.toJSONString(request));

        return restTemplate.postForEntity("http://eureka-client-ad-sponsoreureka-client-ad-sponsor/ad-sponsor/get/adPlan"
                , request, CommonResponse.class).getBody();
    }
}
