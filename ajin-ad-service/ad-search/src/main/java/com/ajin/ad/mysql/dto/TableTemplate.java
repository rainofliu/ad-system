package com.ajin.ad.mysql.dto;

import com.ajin.ad.mysql.contant.OpType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author:ajin
 *
 * 方便读取表的信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableTemplate {

    private String tableName;

    private String level;
    /**
     * 操作类型 -> 字段     映射关系
     *
     */
    private Map<OpType, List<String>> opTypeFieldSetMap=new HashMap<>();

    /**
     * 字段索引 -> 字段名  映射关系
     * */
    private Map<Integer,String> positionMap=new HashMap<>();
}
