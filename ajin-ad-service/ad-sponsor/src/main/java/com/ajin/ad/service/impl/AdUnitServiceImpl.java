package com.ajin.ad.service.impl;

import com.ajin.ad.constant.CommonStatus;
import com.ajin.ad.constant.Constants;
import com.ajin.ad.dao.AdPlanRepository;
import com.ajin.ad.dao.AdUnitRepository;
import com.ajin.ad.dao.CreativeRepository;
import com.ajin.ad.dao.unit_condition.AdUnitDistrictRepository;
import com.ajin.ad.dao.unit_condition.AdUnitItRepository;
import com.ajin.ad.dao.unit_condition.AdUnitKeywordRepository;
import com.ajin.ad.dao.unit_condition.CreativeUnitRepository;
import com.ajin.ad.entity.AdPlan;
import com.ajin.ad.entity.AdUnit;
import com.ajin.ad.entity.unit_condition.AdUnitDistrict;
import com.ajin.ad.entity.unit_condition.AdUnitIt;
import com.ajin.ad.entity.unit_condition.AdUnitKeyword;
import com.ajin.ad.entity.unit_condition.CreativeUnit;
import com.ajin.ad.exception.AdException;
import com.ajin.ad.service.IAdUnitService;
import com.ajin.ad.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Auther: ajin
 * @Date: 2019/4/5 18:26
 */
@Slf4j
@Service
public class AdUnitServiceImpl implements IAdUnitService {

    private final AdUnitRepository unitRepository;

    private final AdPlanRepository planRepository;

    private final AdUnitKeywordRepository keywordRepository;

    private final AdUnitItRepository itRepository;

    private final AdUnitDistrictRepository districtRepository;

    private final CreativeRepository creativeRepository;

    private final CreativeUnitRepository creativeUnitRepository;

    @Autowired
    public AdUnitServiceImpl(AdUnitRepository unitRepository, AdPlanRepository planRepository,
                             AdUnitKeywordRepository keywordRepository,
                             AdUnitItRepository itRepository,
                             AdUnitDistrictRepository districtRepository, CreativeRepository creativeRepository, CreativeUnitRepository creativeUnitRepository) {
        this.unitRepository = unitRepository;
        this.planRepository = planRepository;
        this.keywordRepository = keywordRepository;
        this.itRepository = itRepository;
        this.districtRepository = districtRepository;
        this.creativeRepository = creativeRepository;
        this.creativeUnitRepository = creativeUnitRepository;
    }

    @Override
    @Transactional
    public AdUnitResponse createUnit(AdUnitRequest request) throws AdException {

        // 参数校验
        if (!request.createValidate()) {
            throw new AdException(Constants.ErrMsg.REQUEST_PARAM_ERROR);
        }
        // 查询该推广单元关联的推广计划是否存在,不存在则抛出异常
        Optional<AdPlan> adPlan =
                planRepository.findById(request.getPlanId());
        if (!adPlan.isPresent()) {
            throw new AdException(Constants.ErrMsg.CAN_NOT_FIND_RECORD);
        }

        // 查询推广单元是否已经存在
        AdUnit oldUnit = unitRepository.findByPlanIdAndUnitName(
                request.getPlanId(), request.getUnitName()
        );

        if (oldUnit != null) {
            throw new AdException(Constants.ErrMsg.SAME_NAME_UNIT_ERROR);
        }

        AdUnit newAdUnit = unitRepository.save(
                new AdUnit(
                        request.getPlanId(),
                        request.getUnitName(),
                        request.getPositionType(),
                        request.getBudget())
        );

        return new AdUnitResponse(newAdUnit.getId(), newAdUnit.getUnitName());
    }

    @Override
    public List<AdUnit> getUnitByIds(AdUnitGetRequest request) throws AdException {

        if (!request.validate()) {
            throw new AdException(Constants.ErrMsg.REQUEST_PARAM_ERROR);
        }

        return unitRepository.findAllByIdInAndPlanId(request.getIds(), request.getPlanId());
    }

    @Override
    @Transactional
    public AdUnitResponse updateUnit(AdUnitRequest request) throws AdException {

        if (!request.updateValidate()) {
            throw new AdException(Constants.ErrMsg.REQUEST_PARAM_ERROR);
        }

        AdUnit adUnit = unitRepository.findByPlanIdAndUnitName(request.getPlanId(), request.getUnitName());
        if (adUnit == null) {
            throw new AdException(Constants.ErrMsg.CAN_NOT_FIND_RECORD);
        }

        if (request.getPositionType() != null) {
            adUnit.setPositionType(request.getPositionType());
        }
        if (request.getBudget() != null) {
            adUnit.setBudget(request.getBudget());
        }
        adUnit.setUpdateTime(new Date());
        adUnit = unitRepository.save(adUnit);

        return new AdUnitResponse(adUnit.getId(), adUnit.getUnitName());
    }

    @Override
    @Transactional
    public void deleteUnit(AdUnitRequest request) throws AdException {

        if (!request.deleteValidate()) {
            throw new AdException(Constants.ErrMsg.REQUEST_PARAM_ERROR);
        }

        AdUnit adUnit = unitRepository.findByPlanIdAndUnitName(request.getPlanId(), request.getUnitName());

        if (adUnit == null) {
            throw new AdException(Constants.ErrMsg.CAN_NOT_FIND_RECORD);
        }
        adUnit.setUpdateTime(new Date());
        adUnit.setUnitStatus(CommonStatus.INVALID.getStatus());

        unitRepository.save(adUnit);
    }

    @Override
    @Transactional
    public AdUnitKeywordResponse createUnitKeyword(AdUnitKeywordRequest request) throws AdException {

        List<Long> unitIds = request.getUnitKeywords().stream()
                .map(AdUnitKeywordRequest.UnitKeyword::getUnitId)
                .collect(Collectors.toList());

        if (!isRelatedUnitExist(unitIds)) {
            throw new AdException(Constants.ErrMsg.REQUEST_PARAM_ERROR);
        }

        List<Long> ids = Collections.emptyList();

        List<AdUnitKeyword> unitKeywords = new ArrayList<>();

        if (!CollectionUtils.isEmpty(unitKeywords)) {

            request.getUnitKeywords().forEach(i -> unitKeywords.add(
                    new AdUnitKeyword(i.getUnitId(), i.getKeyword())
            ));
            ids = keywordRepository.saveAll(unitKeywords).stream()
                    .map(AdUnitKeyword::getId)
                    .collect(Collectors.toList());
        }
        return new AdUnitKeywordResponse(ids);
    }

    @Override
    @Transactional
    public AdUnitItResponse createUnitIt(AdUnitItRequest request) throws AdException {

        List<Long> unitIds = request.getUnitIts().stream()
                .map(AdUnitItRequest.UnitIt::getUnitId)
                .collect(Collectors.toList());

        if (!isRelatedUnitExist(unitIds)) {
            throw new AdException(Constants.ErrMsg.REQUEST_PARAM_ERROR);
        }

        List<AdUnitIt> unitIts = new ArrayList<>();

        request.getUnitIts().forEach(i -> unitIts.add(
                new AdUnitIt(i.getUnitId(), i.getItTag())
        ));
        List<Long> ids = itRepository.saveAll(
                unitIts).stream().map(
                AdUnitIt::getId
        ).collect(Collectors.toList());

        return new AdUnitItResponse(ids);
    }

    @Override
    @Transactional
    public AdUnitDistrictResponse createUnitDistrict(AdUnitDistrictRequest request) throws AdException {

        List<Long> unitDistrictIds = request.getUnitDistricts().stream()
                .map(AdUnitDistrictRequest.UnitDistrict::getUnitId)
                .collect(Collectors.toList());

        if (!isRelatedUnitExist(unitDistrictIds)) {
            throw new AdException(Constants.ErrMsg.REQUEST_PARAM_ERROR);
        }

        List<AdUnitDistrict> unitDistricts = new ArrayList<>();

        request.getUnitDistricts().forEach(i -> unitDistricts.add(
                new AdUnitDistrict(i.getUnitId(), i.getProvince(), i.getCity())
        ));
        List<Long> ids = districtRepository.saveAll(unitDistricts)
                .stream().map(AdUnitDistrict::getId)
                .collect(Collectors.toList());

        return new AdUnitDistrictResponse(ids);
    }

    @Override
    @Transactional
    public CreativeUnitResponse createCreativeUnit(CreativeUnitRequest request) throws AdException {

        List<Long> unitIds = request.getUnitItems().stream()
                .map(CreativeUnitRequest.CreativeUnitItem::getUnitId)
                .collect(Collectors.toList());

        List<Long> creativeIds = request.getUnitItems().stream()
                .map(CreativeUnitRequest.CreativeUnitItem::getCreativeId)
                .collect(Collectors.toList());

        if (!(isRelatedUnitExist(unitIds) && isRelatedCreativeExist(creativeIds))) {
            throw new AdException(Constants.ErrMsg.REQUEST_PARAM_ERROR);
        }

        List<CreativeUnit> creativeUnits=new ArrayList<>();
        request.getUnitItems().forEach(i->creativeUnits.add(
                new CreativeUnit(i.getCreativeId(),i.getUnitId())
        ));

        List<Long> ids=creativeUnitRepository.saveAll(creativeUnits).stream()
                .map(CreativeUnit::getId)
                .collect(Collectors.toList());


        return new CreativeUnitResponse(ids);
    }

    // 判断相关推广单元是否存在
    private boolean isRelatedUnitExist(List<Long> unitIds) {

        if (CollectionUtils.isEmpty(unitIds)) {
            return false;
        }
        return unitRepository.findAllById(unitIds).size() ==
                new HashSet<Long>(unitIds).size();
    }

    // 判断创意是否存在
    private boolean isRelatedCreativeExist(List<Long> creativeIds) {
        if (CollectionUtils.isEmpty(creativeIds)) {
            return false;
        }
        return creativeRepository.findAllById(creativeIds).size() ==
                new HashSet<Long>(creativeIds).size();

    }


}
