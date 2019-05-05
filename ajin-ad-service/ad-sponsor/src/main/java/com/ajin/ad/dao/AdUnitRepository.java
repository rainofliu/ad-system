package com.ajin.ad.dao;

import com.ajin.ad.entity.AdUnit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Auther: ajin
 * @Date: 2019/4/5 16:32
 */
public interface AdUnitRepository extends JpaRepository<AdUnit, Long> {

    /**
     * 根据planId和unitName唯一确定一条推广单元信息
     */
    AdUnit findByPlanIdAndUnitName(Long planId, String unitName);

    List<AdUnit> findAllByUnitStatus(Integer unitStatus);

    List<AdUnit> findAllByIdInAndPlanId(List<Long> ids,Long planId);

}
