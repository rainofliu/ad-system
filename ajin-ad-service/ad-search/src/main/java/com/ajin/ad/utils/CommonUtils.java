package com.ajin.ad.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

/**
 * 工具类
 */
@Slf4j
public class CommonUtils {

    public static <K, V> V getOrCreate(K key, Map<K, V> map,
                                       Supplier<V> factory) {

        return map.computeIfAbsent(key, k -> factory.get());

    }

    /**
     * 字符串拼接
     */
    public static String stringConcat(String... args) {

        StringBuilder result = new StringBuilder();

        for (String arg : args) {
            result.append(arg);
            result.append("-");
        }
        // 删除多余的"-"
        result.deleteCharAt(result.length() - 1);

        return result.toString();
    }

    /**
     * 实现对binglog监听到的日期格式的解析，转化成Java中的Date格式
     * Fri Apr 19 05:40:33 CST 2019
     */
    public static Date parseStringDate(String dateString) {

        try {

            DateFormat dateFormat = new SimpleDateFormat(
                    "EEE MMM dd HH:mm:ss zzz yyyy", Locale.US
            );
            return DateUtils.addHours(
                    dateFormat.parse(dateString),
                    -8);
        } catch (ParseException e) {
            log.error("parseStringDate error: {}", dateString);
            return null;
        }
    }
}
