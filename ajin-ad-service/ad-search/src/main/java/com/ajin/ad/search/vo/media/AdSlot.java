package com.ajin.ad.search.vo.media;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: ajin
 * @Date: 2019/4/24 21:21
 * 广告位信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdSlot {
    /**
     * 广告位编码
     */
    private String adSlotCode;

    /**
     * 流量类型
     */
    private Integer position;

    /**
     * 广告位宽度
     * */
    private Integer width;

    private Integer height;

    /**
     * 广告物料的类型：图片，视频
     * */
    private List<Integer> type;

    /**
     * 最低出价
     * */
    private Integer minCpm;
}
