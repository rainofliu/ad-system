package com.ajin.ad.index.interest;

import com.ajin.ad.index.IndexAware;
import com.ajin.ad.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * @Auther: ajin
 * @Date: 2019/4/12 14:23
 */
@Slf4j
@Component
public class UnitItIndex implements IndexAware<String, Set<Long>> {

    // <itTag, adUnit set>
    private static Map<String, Set<Long>> itUnitMap;
    // <unitId, itTag set>
    private static Map<Long, Set<String>> unitItMap;

    static {
        itUnitMap = new ConcurrentHashMap<>();
        unitItMap = new ConcurrentHashMap<>();
    }

    @Override
    public Set<Long> get(String key) {
        return itUnitMap.get(key);
    }

    @Override
    @SuppressWarnings("all")
    public void add(String key, Set<Long> value) {

        log.info("UnitItIndex before add: {}", unitItMap);

        Set<Long> unitIds = CommonUtils.getOrCreate(key, itUnitMap, ConcurrentSkipListSet::new);

        unitIds.addAll(value);

        for (Long unitId : value) {

            Set<String> its = CommonUtils.getOrCreate(unitId, unitItMap, ConcurrentSkipListSet::new);

            its.add(key);


        }

        log.info("UnitItIndex ,after add : {}", unitItMap);
    }

    @Override
    public void update(String key, Set<Long> value) {

        log.error("it index can not support update");
    }

    @SuppressWarnings("all")
    @Override
    public void delete(String key, Set<Long> value) {

        log.info("UnitItIndex ,before delete : {}", unitItMap);

        Set<Long> unitIds = CommonUtils.getOrCreate(key, itUnitMap, ConcurrentSkipListSet::new);

        unitIds.removeAll(value);

        for (Long unitId : value) {
            Set<String> itTags = CommonUtils.getOrCreate(unitId, unitItMap, ConcurrentSkipListSet::new);

            itTags.remove(key);
        }

        log.info("UnitItIndex ,after delete : {}", unitItMap);


    }

    public boolean match(Long unitId, List<String> itTags) {

        if (unitItMap.containsKey(unitId)
                && CollectionUtils.isNotEmpty(unitItMap.get(unitId))) {

            Set<String> unitIts = unitItMap.get(unitId);

            // 判断前一个是否是后一个的子集
            return CollectionUtils.isSubCollection(itTags, unitIts);
        }

        return false;

    }
}
