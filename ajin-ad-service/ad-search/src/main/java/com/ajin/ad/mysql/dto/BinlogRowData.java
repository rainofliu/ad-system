package com.ajin.ad.mysql.dto;

import com.github.shyiko.mysql.binlog.event.EventType;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @Author: ajin
 * @Date: 2019/4/24 12:13
 */
@Data
public class BinlogRowData {

    private TableTemplate table;

    private EventType eventType;

    private List<Map<String, String>> after;

    /**
     * 只是为了定义，实际不使用
     */
    private List<Map<String, String>> before;
}
