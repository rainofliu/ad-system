package com.ajin.ad.index.keyword;

import com.ajin.ad.index.IndexAware;
import com.ajin.ad.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * 倒排索引（推广单元的关键词限制）
 */
@Slf4j
@Component
public class UnitKeywordIndex implements IndexAware<String, Set<Long>> { // String: 关键词  Set<Long> 保存推广单元Id

    // 关键词   推广单元id  1:N 倒排索引
    private static Map<String, Set<Long>> keywordUnitMap;

    // 推广单元Id -->推广单元的关键词  1:N 正向索引
    private static Map<Long, Set<String>> unitKeywordMap;

    static {
        keywordUnitMap = new ConcurrentHashMap<>();
        unitKeywordMap = new ConcurrentHashMap<>();
    }

    @Override
    public Set<Long> get(String key) {

        if (StringUtils.isEmpty(key)) {
            // 返回空集合
            return Collections.emptySet();
        }
        // 根据关键词获取推广单元 ，一个关键词对应多个推广单元
        Set<Long> result = keywordUnitMap.get(key);
        // 如果关键词没有映射到任何一个推广单元
        if (result == null) {
            return Collections.emptySet();
        }
        return result;
    }

    @Override
    public void add(String key, Set<Long> value) {

        log.info("UnitKeywordIndex , before add : {}", unitKeywordMap);
        // 添加关键词与推广单元Id的映射关系
        Set<Long> unitIdSet = CommonUtils.getOrCreate(
                key, keywordUnitMap, ConcurrentSkipListSet::new
        );
        unitIdSet.addAll(value);

        // 添加推广单元Id与关键词的映射关系
        for (Long unitId : value) {
            Set<String> keywordSet = CommonUtils.getOrCreate(unitId, unitKeywordMap, ConcurrentSkipListSet::new);

            keywordSet.add(key);
        }

        log.info("UnitKeywordIndex ,after add ：{}", unitKeywordMap);

    }

    @Override
    public void update(String key, Set<Long> value) {

        // 更新索引的成本太高，这里不支持更新
        log.error("keyword index can not support update");
    }

    @Override
    public void delete(String key, Set<Long> value) {

        log.info("UnitKeywordIndex ,before delete", unitKeywordMap);

        Set<Long> unitIds = CommonUtils.getOrCreate(key, keywordUnitMap, ConcurrentSkipListSet::new);

        unitIds.removeAll(value);

        for (Long unitId : value) {
            Set<String> keywordSet = CommonUtils.getOrCreate(unitId, unitKeywordMap, ConcurrentSkipListSet::new);

            keywordSet.remove(key);
        }

        log.info("UnitKeywordIndex ,after delete", unitKeywordMap);
    }

    public boolean match(Long unitId, List<String> keywords) {

        if (unitKeywordMap.containsKey(unitId)
                && CollectionUtils.isNotEmpty(unitKeywordMap.get(unitId))) {

            Set<String> unitKeywords = unitKeywordMap.get(unitId);

            // 如果前者是后者的子集  返回true
            return CollectionUtils.isSubCollection(keywords, unitKeywords);
        }
        return false;
    }
}
