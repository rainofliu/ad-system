package com.ajin.ad.dao;

import com.ajin.ad.entity.AdPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Auther: ajin
 * @Date: 2019/4/5 16:26
 */
public interface AdPlanRepository extends JpaRepository<AdPlan,Long> {
    /**
     * <h2>根据主键id和userId查询某一条推广计划信息</h2>
     * */
    AdPlan findByIdAndUserId(Long id,Long userId);

    List<AdPlan> findAllByIdInAndUserId(List<Long>ids,Long userId);

    AdPlan findByUserIdAndPlanName(Long userId,String planName);

    List<AdPlan> findAllByPlanStatus(Integer status);
}
