package com.ajin.ad.mysql.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 表达template.json整个模板文件
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Template {

    private String database;

    private List<JsonTable> tableList;
}
