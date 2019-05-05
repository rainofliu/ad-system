package com.ajin.ad.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

/**
 * @Auther: ajin
 * @Date: 2019/4/5 17:21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdPlanRequest {
    private Long id;
    private Long userId;
    private String planName;
    private String startDate;
    private String endDate;

    // 创建adPlan时进行的参数校验
    public boolean createValidate() {

        return userId != null
                && !StringUtils.isEmpty(planName)
                && !StringUtils.isEmpty(startDate)
                && !StringUtils.isEmpty(endDate);
    }

    // 更新adPlan时进行的参数校验
    public boolean updateValidate() {
        return id != null && userId != null;
    }

    // 删除adPlan进行的参数校验
    public boolean deleteValidate() {
        return id != null && userId != null;
    }
}
