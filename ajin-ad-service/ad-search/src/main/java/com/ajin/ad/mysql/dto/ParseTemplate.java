package com.ajin.ad.mysql.dto;

import com.ajin.ad.mysql.contant.OpType;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * 实现对模板对象的解析
 */
@Data
public class ParseTemplate {

    private String database;

    /**
     * 表的名称 -> TableTemplate
     */
    private Map<String, TableTemplate> tableTemplateMap = new HashMap<>();

    public static ParseTemplate parse(Template _template) {

        ParseTemplate template = new ParseTemplate();
        template.setDatabase(_template.getDatabase());

        for (JsonTable table : _template.getTableList()) {

            // 表名称
            String name = table.getTableName();
            // 表的层级
            Integer level = table.getLevel();

            TableTemplate tableTemplate = new TableTemplate();
            tableTemplate.setLevel(level.toString());
            tableTemplate.setTableName(name);

            template.tableTemplateMap.put(name, tableTemplate);

            // 遍历操作类型对应的列
            Map<OpType, List<String>> opTypeFiledSetMap = tableTemplate.getOpTypeFieldSetMap();

            for (JsonTable.Column column : table.getInsert()) {

                getAndCreateIfNeed(OpType.ADD, opTypeFiledSetMap, ArrayList::new)
                        .add(column.getColumn());
            }

            for (JsonTable.Column column : table.getUpdate()) {

                getAndCreateIfNeed(OpType.UPDATE, opTypeFiledSetMap, ArrayList::new)
                        .add(column.getColumn());
            }

            for (JsonTable.Column column : table.getDelete()) {

                getAndCreateIfNeed(OpType.DELETE, opTypeFiledSetMap, ArrayList::new)
                        .add(column.getColumn());
            }

        }
        return template;

    }

    private static <T, R> R getAndCreateIfNeed(T key, Map<T, R> map,
                                               Supplier<R> factory) {

        return map.computeIfAbsent(key, k -> factory.get());
    }

}
