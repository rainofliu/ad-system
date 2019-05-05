package com.ajin.ad.utils;

import com.ajin.ad.exception.AdException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.time.DateUtils;

import java.util.Date;

/**
 * 工具类
 */
public class CommonUtils {

    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy/MM/dd", "yyyy.MM.dd"
    };

    /**
     * <h2>获取一个字符串的md5</h2>
     */
    public static String md5(String value) {

        return DigestUtils.md5Hex(value).toUpperCase();
    }

    /**
     * <h2>将String转换成Date</h2>
     */
    public static Date parseStringDate(String dateString) throws AdException {

        try {
            return DateUtils.parseDate(
                    dateString, parsePatterns);
        } catch (Exception e) {
            throw new AdException(e.getMessage());
        }
    }
}
