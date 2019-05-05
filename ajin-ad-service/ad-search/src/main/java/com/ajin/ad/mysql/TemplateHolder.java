package com.ajin.ad.mysql;

import com.ajin.ad.mysql.contant.OpType;
import com.ajin.ad.mysql.dto.ParseTemplate;
import com.ajin.ad.mysql.dto.TableTemplate;
import com.ajin.ad.mysql.dto.Template;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * @Author: ajin
 * @Date: 2019/4/24 11:15
 */
@Slf4j
@Component
public class TemplateHolder {

    private ParseTemplate template;

    private final JdbcTemplate jdbcTemplate;

    //select table_schema,table_name ,column_name,ordinal_position
    //from information_schema.columns where table_schema='imooc_ad_data' and table_name='ad_unit';

    // 执行结果：
//        +---------------+------------+---------------+------------------+
//                | table_schema  | table_name | column_name   | ordinal_position |
//            +---------------+------------+---------------+------------------+
//            | imooc_ad_data | ad_unit    | id            |                1 |
//            | imooc_ad_data | ad_unit    | plan_id       |                2 |
//            | imooc_ad_data | ad_unit    | unit_name     |                3 |
//            | imooc_ad_data | ad_unit    | unit_status   |                4 |
//            | imooc_ad_data | ad_unit    | position_type |                5 |
//            | imooc_ad_data | ad_unit    | budget        |                6 |
//            | imooc_ad_data | ad_unit    | create_time   |                7 |
//            | imooc_ad_data | ad_unit    | update_time   |                8 |
//            +---------------+------------+---------------+------------------+
//            8 rows in set

    private String SQL_SCHEMA = "select table_schema,table_name," +
            "column_name,ordinal_position from information_schema.columns" +
            " where table_schema =? and table_name= ?";

    @Autowired
    public TemplateHolder(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * IoC容器启动的时候，加载json文件，并将其进行反序列化成Template,
     * 并根据Template的信息，完成字段索引 ->字段名的映射
     */
    @PostConstruct
    private void init() {
        loadJson("template.json");
    }

    /**
     * Description: 根据tableName获取TableTemplate
     */
    public TableTemplate getTable(String tableName) {
        return template.getTableTemplateMap().get(tableName);
    }

    /**
     * 加载template.json文件
     */
    private void loadJson(String path) {
        // 获取ClassLoader
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        // 获取文件的输入流
        InputStream inStream = cl.getResourceAsStream(path);

        try {
            // 将输入流的JSON模板文件进行反序列化
            Template template = JSON.parseObject(inStream, Charset.defaultCharset(), Template.class);
            this.template = ParseTemplate.parse(template);
            loadMeta();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("fail to parse json file");
        }

    }

    private void loadMeta() {

        for (Map.Entry<String, TableTemplate> entry :
                template.getTableTemplateMap().entrySet()) {

            TableTemplate table = entry.getValue();

            List<String> updateFields = table.getOpTypeFieldSetMap().get(OpType.UPDATE);
            List<String> insertFields = table.getOpTypeFieldSetMap().get(OpType.ADD);
            List<String> deleteFields = table.getOpTypeFieldSetMap().get(OpType.DELETE);

            jdbcTemplate.query(SQL_SCHEMA, new Object[]{
                    template.getDatabase(), table.getTableName()
            }, (rs, i) -> {

                int pos = rs.getInt("ORDINAL_POSITION");
                String colName = rs.getString("COLUMN_NAME");

                if ((null != updateFields && updateFields.contains(colName))
                        || (null != insertFields && insertFields.contains(colName))
                        || (null != deleteFields && deleteFields.contains(colName))) {
                    table.getPositionMap().put(pos - 1, colName);
                }

                return null;
            });
        }
    }

}
