package com.ajin.ad.mysql.dto;

import com.ajin.ad.mysql.contant.OpType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 构造增量数据
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MysqlRowData {

    private String tableName;
    /**
     * 索引的层级关系
     */
    private String level;

    /**
     * 我们自定义的操作类型,根据EventType转化为OpType
     */
    private OpType type;

    private List<Map<String, String>> fieldValueMap = new ArrayList<>();
}
