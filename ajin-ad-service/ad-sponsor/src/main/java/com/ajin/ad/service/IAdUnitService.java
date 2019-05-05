package com.ajin.ad.service;

import com.ajin.ad.entity.AdUnit;
import com.ajin.ad.exception.AdException;
import com.ajin.ad.vo.*;

import java.util.List;

/**
 * @Auther: ajin
 * @Date: 2019/4/5 18:18
 */
public interface IAdUnitService {

    AdUnitResponse createUnit(AdUnitRequest request) throws AdException;

    List<AdUnit> getUnitByIds(AdUnitGetRequest request) throws AdException;

    AdUnitResponse updateUnit(AdUnitRequest request) throws AdException;

    void deleteUnit(AdUnitRequest request) throws AdException;


    /**
     * <h2>创建推广单元的关键字限制</h2>
     */
    AdUnitKeywordResponse createUnitKeyword(AdUnitKeywordRequest request)
            throws AdException;

    AdUnitItResponse createUnitIt(AdUnitItRequest request)
            throws AdException;

    AdUnitDistrictResponse createUnitDistrict(AdUnitDistrictRequest request)
            throws AdException;


    CreativeUnitResponse createCreativeUnit(CreativeUnitRequest request)
            throws AdException;
}
