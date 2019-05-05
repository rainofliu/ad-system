package com.ajin.ad.controller;

import com.ajin.ad.entity.unit_condition.AdUnitDistrict;
import com.ajin.ad.exception.AdException;
import com.ajin.ad.service.IAdUnitService;
import com.ajin.ad.vo.*;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: ajin
 * @Date: 2019/4/5 20:44
 */
@Slf4j
@RestController
public class AdUnitOPController {

    private final IAdUnitService adUnitService;

    @Autowired
    public AdUnitOPController(IAdUnitService adUnitService) {
        this.adUnitService = adUnitService;
    }

    @PostMapping("/create/adUnit")
    public AdUnitResponse createAdUnit(
            @RequestBody AdUnitRequest request
    ) throws AdException {
        log.info("ad-sponsor: createAdUnit -> {}", JSON.toJSONString(request));

        return adUnitService.createUnit(request);
    }

    @PostMapping("/create/unitKetword")
    public AdUnitKeywordResponse createUnitKeyword
            (@RequestBody AdUnitKeywordRequest request) throws AdException{
        log.info("ad-sponsor: createUnitKeyword -> {}", JSON.toJSONString(request));

        return adUnitService.createUnitKeyword(request);
    }

    @PostMapping("/create/unitIt")
    public AdUnitItResponse createUnitIt(
            @RequestBody AdUnitItRequest request)throws AdException{
        log.info("ad-sponsor: createUnitIt -> {}", JSON.toJSONString(request));

        return adUnitService.createUnitIt(request);
    }

    @PostMapping("/create/unitDistrict")
    public AdUnitDistrictResponse createUnitDistrict(
            @RequestBody AdUnitDistrictRequest request)throws AdException{
        log.info("ad-sponsor: createUnitDistrict -> {}", JSON.toJSONString(request));

        return adUnitService.createUnitDistrict(request);
    }

    @PostMapping("/create/creativeUnit")
    public CreativeUnitResponse createCreativeUnit(
            @RequestBody CreativeUnitRequest request)throws AdException{
        log.info("ad-sponsor: createCreativeUnit -> {}", JSON.toJSONString(request));

        return adUnitService.createCreativeUnit(request);
    }
}
