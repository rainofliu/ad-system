package com.ajin.ad.controller;

import com.ajin.ad.entity.AdPlan;
import com.ajin.ad.exception.AdException;
import com.ajin.ad.service.IAdPlanService;
import com.ajin.ad.vo.AdPlanGetRequest;
import com.ajin.ad.vo.AdPlanRequest;
import com.ajin.ad.vo.AdPlanResponse;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Auther: ajin
 * @Date: 2019/4/5 20:34
 */
@Slf4j
@RestController
public class AdPlanOPController {

    private final IAdPlanService adPlanService;

    @Autowired
    public AdPlanOPController(IAdPlanService adPlanService) {
        this.adPlanService = adPlanService;
    }

    @PostMapping("/create/adPlan")
    public AdPlanResponse createAdPlan(
            @RequestBody AdPlanRequest request) throws AdException {

        log.info("ad-sponsor:createAdPlan-> {}", JSON.toJSONString(request));

        return adPlanService.createAdPlan(request);
    }

    @PostMapping("/get/adPlan")
    public List<AdPlan> getAdPlanByIds(
            @RequestBody AdPlanGetRequest request) throws AdException {

        log.info("ad-sponsor:getAdPlanByIds -> {}", JSON.toJSONString(request));

        return adPlanService.getAdPlanByIds(request);
    }

    @PutMapping("/update/adPlan")
    public AdPlanResponse updateAdPlan(
            @RequestBody AdPlanRequest request) throws AdException {

        log.info("ad-sponsor:updateAdPlan -> {}", JSON.toJSONString(request));

        return adPlanService.updateAdPlan(request);
    }

    @DeleteMapping("/delete/adPlan")
    public void deleteAdPlan(
            @RequestBody AdPlanRequest request) throws AdException {

        log.info("ad-sponsor:deleteAdPlan -> {}", JSON.toJSONString(request));

        adPlanService.deleteAdPlan(request);
    }

}
