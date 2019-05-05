package com.ajin.ad.handler;

import com.ajin.ad.dmp.table.*;
import com.ajin.ad.index.DataTable;
import com.ajin.ad.index.IndexAware;
import com.ajin.ad.index.adplan.AdPlanIndex;
import com.ajin.ad.index.adplan.AdPlanObject;
import com.ajin.ad.index.adunit.AdUnitIndex;
import com.ajin.ad.index.adunit.AdUnitObject;
import com.ajin.ad.index.creative.CreativeIndex;
import com.ajin.ad.index.creative.CreativeObject;
import com.ajin.ad.index.creative_unit.CreativeUnitIndex;
import com.ajin.ad.index.creative_unit.CreativeUnitObject;
import com.ajin.ad.index.district.UnitDistrictIndex;
import com.ajin.ad.index.interest.UnitItIndex;
import com.ajin.ad.index.keyword.UnitKeywordIndex;
import com.ajin.ad.mysql.contant.OpType;
import com.ajin.ad.utils.CommonUtils;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 1. 索引之间存在着层级的划分，也就是依赖关系的划分
 * 2. 加载全量索引，其实是增量索引“添加”的一种特殊实现
 * <p>
 * 层级索引是一种逻辑概念，这里的核心思想是各层级索引之间的依赖关系，
 * 第三级的索引需要有第二级存在，第四级的索引有第三级的索引存在。
 * 它并不会影响实际的功能，但是，抽象出来索引之间的层级关系能够更好的梳理代码逻辑，方便排查问题。
 * <p>
 * 例如：当你看到第三级的索引的时候，就知道，它一定是有第二级的索引为依赖。
 * 所以，当第二级的索引不存在，那么，第三级的索引一定不能构建。
 * Created by ajin.
 */
@Slf4j
public class AdLevelDataHandler {

    /**
     * 第二层级索引操作1
     */
    public static void handleLevel2(AdPlanTable adPlanTable, OpType type) {

        AdPlanObject planObject = new AdPlanObject(
                adPlanTable.getId(),
                adPlanTable.getUserId(),
                adPlanTable.getPlanStatus(),
                adPlanTable.getStartDate(),
                adPlanTable.getEndDate()
        );

        handleBinLogEvent(DataTable.of(AdPlanIndex.class),
                planObject.getPlanId(),
                planObject,
                type);

    }

    /**
     * 第二层级索引操作2
     */
    public static void handleLevel2(AdCreativeTable creativeTable, OpType type) {

        CreativeObject creativeObject = new CreativeObject(
                creativeTable.getAdId(),
                creativeTable.getName(),
                creativeTable.getType(),
                creativeTable.getMaterialType(),
                creativeTable.getHeight(),
                creativeTable.getWidth(),
                creativeTable.getAuditStatus(),
                creativeTable.getAdUrl()
        );

        handleBinLogEvent(DataTable.of(CreativeIndex.class),
                creativeObject.getAdId(),
                creativeObject,
                type);
    }

    /**
     * 第三层级索引1
     */
    public static void handleLevel3(AdUnitTable unitTable, OpType type) {

        // 判断推广单元是否和推广计划已经关联
        AdPlanObject adPlanObject = DataTable.of(AdPlanIndex.class).get(unitTable.getPlanId());

        if (null == adPlanObject) {
            log.error("handleLever3 found AdPlanObject error :{}",
                    unitTable.getPlanId());
            return;
        }

        AdUnitObject adUnitObject = new AdUnitObject(
                unitTable.getUnitId(),
                unitTable.getUnitStatus(),
                unitTable.getPositionType(),
                unitTable.getPlanId(),
                adPlanObject
        );

        handleBinLogEvent(DataTable.of(AdUnitIndex.class),
                adUnitObject.getUnitId(),
                adUnitObject,
                type);


    }

    public static void handleLevel3(AdCreativeUnitTable creativeUnitTable, OpType type) {

        if (type == OpType.UPDATE) {
            log.error("CreativeUnitIndex not support update");
            return;
        }
        AdUnitObject unitObject = DataTable.of(AdUnitIndex.class).get(creativeUnitTable.getUnitId());
        CreativeObject creativeObject = DataTable.of(CreativeIndex.class).get(creativeUnitTable.getAdId());

        if (unitObject == null || creativeObject == null) {
            log.error("AdCreativeUnitTable index error : {}",
                    JSON.toJSONString(creativeUnitTable));
            return;
        }

        CreativeUnitObject creativeUnitObject = new CreativeUnitObject(
                creativeUnitTable.getAdId(),
                creativeUnitTable.getUnitId()
        );

        handleBinLogEvent(DataTable.of(CreativeUnitIndex.class),
                CommonUtils.stringConcat(creativeUnitObject.getAdId().toString(),
                        creativeUnitObject.getUnitId().toString()),
                creativeUnitObject,
                type);
    }

    /**
     * <h2>第四层级索引</h2>
     **/
    public static void handleLevel4(AdUnitDistrictTable unitDistrictTable, OpType type) {

        if (type == OpType.UPDATE) {
            log.error("District Index not support update");
            return;
        }
        AdUnitObject unitObject = DataTable.of(AdUnitIndex.class).get(unitDistrictTable.getUnitId());
        if (null == unitObject) {
            log.error("AdUnitDistrictTable index error :{}", unitDistrictTable.getUnitId());
            return;
        }

        String key = CommonUtils.stringConcat(unitDistrictTable.getProvince(),
                unitDistrictTable.getCity());
        Set<Long> value = new HashSet<>(
                Collections.singleton(unitDistrictTable.getUnitId())
        );
        handleBinLogEvent(DataTable.of(UnitDistrictIndex.class), key, value, type);

    }

    public static void handleLevel4(AdUnitItTable unitItTable, OpType type) {

        if (type == OpType.UPDATE) {
            log.error("it index can not support update");
            return;
        }

        AdUnitObject adUnitObject = DataTable.of(AdUnitIndex.class).get(unitItTable.getUnitId());
        if (adUnitObject == null) {
            log.error("AdUnitItTable index error: {}" + unitItTable.getUnitId());
            return;
        }

        Set<Long> value = new HashSet<>(
                Collections.singleton(unitItTable.getUnitId())
        );

        handleBinLogEvent(DataTable.of(UnitItIndex.class),
                unitItTable.getItTag(),
                value,
                type);

    }

    public static void handleLevel4(AdUnitKeywordTable unitKeywordTable, OpType type) {

        if (type == OpType.UPDATE) {
            log.error("keyword index can not support update");
            return;
        }

        AdUnitObject unitObject = DataTable.of(AdUnitIndex.class).get(unitKeywordTable.getUnitId());
        if (unitObject == null) {
            log.error("AdUnitKeywordTable index error: {}", unitKeywordTable.getUnitId());
            return;
        }

        Set<Long> value = new HashSet<>(
                Collections.singleton(unitKeywordTable.getUnitId())
        );
        handleBinLogEvent(
                DataTable.of(UnitKeywordIndex.class),
                unitKeywordTable.getKeyword(),
                value,
                type
        );

    }

    /**
     * K:索引的键
     * V：索引的值
     */
    @SuppressWarnings("all")
    private static <K, V> void handleBinLogEvent(IndexAware<K, V> index,
                                                 K key, V value,
                                                 OpType type) {
        switch (type) {
            case ADD:
                index.add(key, value);
                break;
            case UPDATE:
                index.update(key, value);
                break;

            case DELETE:
                index.delete(key, value);
                break;
            default:
                break;
        }
    }


}
