package com.ajin.ad.index.adunit;

import com.ajin.ad.index.adplan.AdPlanObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AdUnit索引对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdUnitObject {

    private Long unitId;
    private Integer unitStatus;

    /**
     * 流量类型
     */
    private Integer positionType;
    private Long planId;

    private AdPlanObject adPlanObject;

    void update(AdUnitObject newObject) {

        if (null != newObject.getUnitId()) {
            this.unitId = newObject.getUnitId();
        }

        if (null != newObject.getUnitStatus()) {
            this.unitStatus = newObject.getUnitStatus();
        }

        if (null != newObject.getPositionType()) {
            this.positionType = newObject.getPositionType();
        }

        if (null != newObject.getPlanId()) {
            this.planId = newObject.getPlanId();
        }

        if (null != newObject.getAdPlanObject()) {
            this.adPlanObject = newObject.getAdPlanObject();
        }


    }

    /**
     * 判断流量类型是否是开屏
     */
    private static boolean isKaiPing(int positionType) {

        return (positionType & AdUnitConstants.POSITION_TYPE.KAIPING) > 0;
    }

    private static boolean isTiePian(int positionType) {

        return (positionType & AdUnitConstants.POSITION_TYPE.TIEPIAN) > 0;
    }

    private static boolean isTiePianMiddle(int positionType) {

        return (positionType & AdUnitConstants.POSITION_TYPE.TIEPIAN_MIDDLE) > 0;
    }

    private static boolean isTiePianPause(int positionType) {

        return (positionType & AdUnitConstants.POSITION_TYPE.TIEPIAN_PASUE) > 0;
    }

    private static boolean isTiePianPost(int positionType) {

        return (positionType & AdUnitConstants.POSITION_TYPE.TIEPIAN_POST) > 0;
    }

    /**
     * 判断adSlotType和positionType是否匹配
     */
    @SuppressWarnings("all")
    public static boolean isAdSlotTypeOK(int adSlotType, int positionType) {

        switch (adSlotType) {

            case AdUnitConstants.POSITION_TYPE.KAIPING:
                return isKaiPing(positionType);
            case AdUnitConstants.POSITION_TYPE.TIEPIAN:
                return isTiePian(positionType);
            case AdUnitConstants.POSITION_TYPE.TIEPIAN_MIDDLE:
                return isTiePianMiddle(positionType);
            case AdUnitConstants.POSITION_TYPE.TIEPIAN_PASUE:
                return isTiePianPause(positionType);
            case AdUnitConstants.POSITION_TYPE.TIEPIAN_POST:
                return isTiePianPost(positionType);
            default:
                return false;
        }
    }

}
