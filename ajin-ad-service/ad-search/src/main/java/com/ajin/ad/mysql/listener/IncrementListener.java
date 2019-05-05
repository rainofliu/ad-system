package com.ajin.ad.mysql.listener;

import com.ajin.ad.mysql.contant.Constant;
import com.ajin.ad.mysql.contant.OpType;
import com.ajin.ad.mysql.dto.BinlogRowData;
import com.ajin.ad.mysql.dto.MysqlRowData;
import com.ajin.ad.mysql.dto.TableTemplate;
import com.ajin.ad.sender.ISender;
import com.github.shyiko.mysql.binlog.event.EventType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: ajin
 * @Date: 2019/4/24 17:01
 */
@Slf4j
@Component
public class IncrementListener implements Ilistener {

    @Resource(name = "indexSender")
    private ISender sender;

    private final AggregationListener aggregationListener;

    @Autowired
    public IncrementListener(AggregationListener aggregationListener) {
        this.aggregationListener = aggregationListener;
    }

    /**
     * 给各个表注册处理器
     */
    @Override
    @PostConstruct // IncrementListener加载进IoC容器的时候，就会加载该方法：实现对表注册处理器
    public void register() {

        log.info("IncrementListener register db and table info");
        /**
         * 给各个表注册处理器
         * */
        Constant.table2Db.forEach((k, v) ->
                aggregationListener.register(v, k, this));
    }

    /**
     * 1. 将BinlogRowData转换成MysqlRowData
     * 2. 将MysqlRowData投递出去（kafka）
     */
    @Override
    public void onEvent(BinlogRowData eventData) {

        TableTemplate table = eventData.getTable();
        EventType eventType = eventData.getEventType();

        // 包装成最后需要投递的数据
        MysqlRowData rowData = new MysqlRowData();
        rowData.setTableName(table.getTableName());
        rowData.setLevel(eventData.getTable().getTableName());

        OpType type = OpType.to(eventType);
        rowData.setType(type);

        // 取出模板中该操作对应的字段列表
        List<String> fieldList = table.getOpTypeFieldSetMap().get(type);

        if (fieldList == null) {
            log.warn("{} not support {}", type, table.getTableName());
            return;
        }

        for (Map<String, String> afterMap : eventData.getAfter()) {

            // 获取发生变化的列&&列的值

            Map<String, String> _afterMap = new HashMap<>();

            for (Map.Entry<String, String> entry : afterMap.entrySet()) {

                String colName = entry.getKey();
                String colValue = entry.getValue();

                _afterMap.put(colName, colValue);
            }

            rowData.getFieldValueMap().add(_afterMap);
        }

        // 投递rowData数据
        // TODO :需要实现该方法
        sender.send(rowData);
    }
}
