package com.ajin.ad.index.district;

import com.ajin.ad.index.IndexAware;
import com.ajin.ad.search.vo.feature.DistrictFeature;
import com.ajin.ad.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

/**
 * @Author: ajin
 * @Date: 2019/4/12 15:00
 */
@Slf4j
@Component
public class UnitDistrictIndex implements IndexAware<String, Set<Long>> {

    private static Map<String, Set<Long>> districtUnitMap;

    private static Map<Long, Set<String>> unitDistrictMap;

    static {
        districtUnitMap = new ConcurrentHashMap<>();
        unitDistrictMap = new ConcurrentHashMap<>();
    }

    @Override
    public Set<Long> get(String key) {
        return districtUnitMap.get(key);
    }

    @Override
    @SuppressWarnings("all")
    public void add(String key, Set<Long> value) {
        log.info("UnitDistrictIndex , before add : {}", unitDistrictMap);

        Set<Long> unitIds = CommonUtils.getOrCreate(key, districtUnitMap, ConcurrentSkipListSet::new);
        unitIds.addAll(value);

        for (Long unitId : value) {
            Set<String> districtSet = CommonUtils.
                    getOrCreate(unitId, unitDistrictMap, ConcurrentSkipListSet::new);
            districtSet.add(key);
        }

        log.info("UnitDistrictIndex , after add : {}", unitDistrictMap);
    }

    @Override
    public void update(String key, Set<Long> value) {

        log.error("unit district index can not support update");
    }

    @Override
    public void delete(String key, Set<Long> value) {

        log.info("UnitDistrictIndex , before delete : {}", unitDistrictMap);

        Set<Long> unitIds = CommonUtils.getOrCreate(key, districtUnitMap, ConcurrentSkipListSet::new);
        unitIds.removeAll(value);

        for (Long unitId : value) {
            Set<String> districtSet = CommonUtils.getOrCreate(unitId, unitDistrictMap, ConcurrentSkipListSet::new);

            districtSet.remove(key);
        }

        log.info("UnitDistrictIndex , after delete : {}", unitDistrictMap);


    }

    public boolean match(Long adUnitId, List<DistrictFeature.ProvinceAndCity> districts) {


        if (unitDistrictMap.containsKey(adUnitId)
                && CollectionUtils.isNotEmpty(unitDistrictMap.get(adUnitId))) {

            /**
             * jiangsu-nanjing 正序索引
             * */
            Set<String> unitDistricts = unitDistrictMap.get(adUnitId);

            /**
             * 媒体方传递的信息
             * */
            List<String> targetDistricts = districts.stream().map(
                    d -> CommonUtils.stringConcat(d.getProvince(), d.getCity())
            ).collect(Collectors.toList());
            /**
             * 判断前者是否是后者的子集
             * */
            return CollectionUtils.isSubCollection(targetDistricts, unitDistricts);
        }

        return false;
    }
}
