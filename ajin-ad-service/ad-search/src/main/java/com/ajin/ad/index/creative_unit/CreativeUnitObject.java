package com.ajin.ad.index.creative_unit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Auther: ajin
 * @Date: 2019/4/12 15:46
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreativeUnitObject {

    private Long adId;
    private Long unitId;

    // adId-unitId结合在一起
}
