package com.ajin.ad.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Auther: ajin
 * @Date: 2019/4/5 18:45
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdUnitGetRequest {
    private Long planId;
    private List<Long> ids;

    public boolean validate() {
        return planId != null && planId > 0
                && ids != null && !CollectionUtils.isEmpty(ids);
    }
}
