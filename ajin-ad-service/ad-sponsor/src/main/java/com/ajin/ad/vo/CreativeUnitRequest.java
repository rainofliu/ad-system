package com.ajin.ad.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Auther: ajin
 * @Date: 2019/4/5 20:09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreativeUnitRequest {

    private List<CreativeUnitItem> unitItems;

    // 批量创建
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreativeUnitItem{

      private Long creativeId;
      private Long unitId;
    }
}
