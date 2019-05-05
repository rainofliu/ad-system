package com.ajin.ad.index.adunit;

import com.ajin.ad.index.IndexAware;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: ajin
 * @Date: 2019/4/12 13:11
 */
@Slf4j
@Component
public class AdUnitIndex implements IndexAware<Long, AdUnitObject> {

    private static Map<Long, AdUnitObject> objectMap;

    static {
        objectMap = new ConcurrentHashMap<>();
    }

    /**
     * 根据positionType对AdUnitIndex进行匹配
     * <p>
     * 大白话：根据流量类型实现对推广单元的预筛选
     */
    public Set<Long> match(Integer positionType) {

        Set<Long> adUnitIds = new HashSet<>();

        /**
         * 根据媒体方传入的positionType筛选出符合条件的推广单元id
         * */
        objectMap.forEach((k, v) -> {
            if (AdUnitObject.isAdSlotTypeOK(positionType, v.getPositionType())) {
                adUnitIds.add(k);
            }
        });

        return adUnitIds;
    }

    /**
     * 根据推广单元id获取AdUnitObject索引对象
     */
    public List<AdUnitObject> fetchCollection(Collection<Long> adUnitIds) {

        if (CollectionUtils.isEmpty(adUnitIds)) {
            return Collections.emptyList();
        }
        List<AdUnitObject> result = new ArrayList<>();
        adUnitIds.forEach(i -> {
            AdUnitObject object = objectMap.get(i);
            if (object == null) {
                log.error("AdUnitObject not found");
                return;
            }
            result.add(object);
        });

        return result;
    }


    @Override
    public AdUnitObject get(Long key) {
        return objectMap.get(key);
    }

    @Override
    public void add(Long key, AdUnitObject value) {

        log.info("before add : {}", objectMap);
        objectMap.put(key, value);
        log.info("after add : {}", objectMap);
    }

    @Override
    public void update(Long key, AdUnitObject value) {
        log.info("before update : {}", objectMap);

        AdUnitObject oldObject = objectMap.get(key);

        if (null == oldObject) {
            objectMap.put(key, value);
        } else {
            oldObject.update(value);
        }

        log.info("after update : {}", objectMap);

    }

    @Override
    public void delete(Long key, AdUnitObject value) {
        log.info("before delete : {}", objectMap);
        objectMap.remove(key);
        log.info("after delete : {}", objectMap);
    }
}
