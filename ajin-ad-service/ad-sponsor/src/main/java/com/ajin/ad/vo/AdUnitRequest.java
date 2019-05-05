package com.ajin.ad.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

/**
 * @Author: ajin
 * @Date: 2019/4/5 18:18
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdUnitRequest {
    private Long id;
    private Long planId;
    private String unitName;

    private Integer positionType;
    private Long budget;

    public boolean createValidate() {
        return null != planId && planId > 0
                && !StringUtils.isEmpty(unitName)
                && null != positionType && positionType > 0
                && null != budget && budget > 0;
    }

    public boolean updateValidate() {
        return id != null && planId != null && planId > 0 && !StringUtils.isEmpty(unitName);
    }

    public boolean deleteValidate() {
        return id != null && planId != null && planId > 0 && !StringUtils.isEmpty(unitName);
    }
}
