package com.ajin.ad.index.adplan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 推广计划索引对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdPlanObject {

    private Long planId;
    private Long userId;
    private Integer planStatus;
    private Date startDate;
    private Date endDate;

    /**
     * 更新推广计划的索引数据，这里很可能是部分更新,优点类似于mybatis的动态SQL
     */
    public void update(AdPlanObject newPlanObject) {

        if (null != newPlanObject.getPlanId()) {
            this.planId = newPlanObject.getPlanId();
        }
        if (null != newPlanObject.getUserId()) {
            this.userId = newPlanObject.getUserId();
        }
        if (null != newPlanObject.getPlanStatus()) {
            this.planStatus = newPlanObject.getPlanStatus();
        }
        if (null != newPlanObject.getStartDate()) {
            this.startDate = newPlanObject.getStartDate();
        }
        if (null != newPlanObject.getEndDate()) {
            this.endDate = newPlanObject.getEndDate();
        }


    }

}
