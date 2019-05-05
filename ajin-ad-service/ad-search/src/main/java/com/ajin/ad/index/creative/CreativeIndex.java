package com.ajin.ad.index.creative;

import com.ajin.ad.index.IndexAware;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: ajin
 * @Date: 2019/4/12 15:30
 */
@Component
@Slf4j
public class CreativeIndex implements IndexAware<Long, CreativeObject> {

    private static Map<Long, CreativeObject> objectMap;

    static {
        objectMap = new ConcurrentHashMap<>();
    }

    /**
     * 根据广告创意id的集合 获取CreativeObject的集合
     */
    public List<CreativeObject> fetch(Collection<Long> adIds) {

        if (CollectionUtils.isEmpty(adIds)) {
            return Collections.emptyList();
        }

        List<CreativeObject> result = new ArrayList<>();

        adIds.forEach(i -> {
            CreativeObject object = get(i);
            if (object == null) {
                log.error("CreativeObject not found");
                return;
            }
            result.add(object);

        });

        return result;
    }

    @Override

    public CreativeObject get(Long key) {
        return objectMap.get(key);
    }

    @Override
    public void add(Long key, CreativeObject value) {
        log.info("before add : {}", objectMap);
        objectMap.put(key, value);
        log.info("after add : {}", objectMap);

    }

    @Override
    public void update(Long key, CreativeObject value) {
        log.info("before update : {}", objectMap);

        CreativeObject oldObject = objectMap.get(key);
        if (oldObject == null) {
            objectMap.put(key, value);
        } else {
            oldObject.update(value);
        }

        log.info("after update : {}", objectMap);

    }

    @Override
    public void delete(Long key, CreativeObject value) {
        log.info("before delete : {}", objectMap);
        objectMap.remove(key);
        log.info("before delete : {}", objectMap);

    }
}
