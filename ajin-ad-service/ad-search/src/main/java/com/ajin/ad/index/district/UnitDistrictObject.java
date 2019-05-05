package com.ajin.ad.index.district;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 地域索引对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnitDistrictObject {

    private Long unitId;
    private String province;
    private String city;

    // <String, Set<Long> >
    // province-city
}
