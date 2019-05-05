package com.ajin.ad.index.adunit;

/**
 * @Author: ajin
 * @Date: 2019/4/25 09:32
 * 推广单元的流量类型
 */

public class AdUnitConstants {

    public static class POSITION_TYPE {

        /**
         * 开屏
         */
        public static final int KAIPING = 1;
        /**
         * 贴片:如电影播放的开头
         */
        public static final int TIEPIAN = 2;

        /**
         * 中间贴      如：电影播放的中间
         */
        public static final int TIEPIAN_MIDDLE = 4;

        /**
         * 暂停贴    播放视频暂停时，弹出的广告
         */
        public static final int TIEPIAN_PASUE = 8;

        /**
         * 后贴
         */
        public static final int TIEPIAN_POST = 16;
    }
}
