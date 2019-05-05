package com.ajin.ad.mysql.listener;

import com.ajin.ad.mysql.TemplateHolder;
import com.ajin.ad.mysql.dto.BinlogRowData;
import com.ajin.ad.mysql.dto.TableTemplate;
import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: ajin
 * @Date: 2019/4/24 12:25
 */
@Slf4j
@Component
public class AggregationListener implements BinaryLogClient.EventListener {

    private String dbName;
    private String tableName;

    /**
     * Key: dbName:tableName
     */
    private Map<String, Ilistener> listenerMap = new HashMap<>();

    private final TemplateHolder templateHolder;

    @Autowired
    public AggregationListener(TemplateHolder templateHolder) {
        this.templateHolder = templateHolder;
    }

    /**
     * 生成listenerMap的key
     */
    private String genKey(String dbName, String tableName) {
        return dbName + ":" + tableName;
    }

    /**
     * 给表注册一个监听器
     * 注册 给Ilistener接口的实现类调用
     */
    public void register(String _dbName, String _tableName,
                         Ilistener ilistener) {

        log.info("register : {}-{}", _dbName, _tableName);
        this.listenerMap.put(genKey(_dbName, _tableName), ilistener);

    }

    /**
     * 对binlog进行监听
     */
    @Override
    public void onEvent(Event event) {

        // 获取EventType
        EventType type = event.getHeader().getEventType();
        log.debug("event type : {}", type);

        if (type == EventType.TABLE_MAP) {
            // 记录下 一个操作所对应的表信息，存储了数据库名和表名
            TableMapEventData data = event.getData();
            // 表名
            this.tableName = data.getTable();
            // 数据库名
            this.dbName = data.getDatabase();
            return;
        }
        // 不是增删改，直接返回 不处理
        if (type != EventType.EXT_UPDATE_ROWS &&
                type != EventType.EXT_WRITE_ROWS && type != EventType.EXT_DELETE_ROWS) {
            return;
        }

        // 表名和库名是否已经完成了填充
        if (StringUtils.isEmpty(dbName) || StringUtils.isEmpty(tableName)) {
            log.error("no meta data event");
            return;
        }

        // 找出对应表有兴趣的监听器
        String key = genKey(this.dbName, this.tableName);
        Ilistener listener = this.listenerMap.get(key);
        if (null == listener) {
            log.debug("skip {}", key);
        }

        log.info("trigger event : {}", type.name());

        try {
            BinlogRowData rowData = buildRowData(event.getData());
            if (rowData == null) {
                return;
            }

            rowData.setEventType(type);
            // 对BinlogRowData进行处理，并将增量数据发送出去 kafka...
            listener.onEvent(rowData);

        } catch (Exception ex) {
            ex.printStackTrace();
            log.error(ex.getMessage());
        } finally {
            // 清空dbName tableName
            this.dbName = "";
            this.tableName = "";
        }

    }

    /**
     * 获取binlog中的rows信息，如果是更新数据，获取的就是更新后的那一行数据记录
     */
    private List<Serializable[]> getAfterValues(EventData eventData) {

        if (eventData instanceof WriteRowsEventData) {
            return ((WriteRowsEventData) eventData).getRows();
        }

        if (eventData instanceof UpdateRowsEventData) {
            return ((UpdateRowsEventData) eventData).getRows().stream()
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
        }

        if (eventData instanceof DeleteRowsEventData) {

            return ((DeleteRowsEventData) eventData).getRows();
        }
        return Collections.emptyList();

    }

    /**
     * 将EventData转化成BinlogRowData
     */
    private BinlogRowData buildRowData(EventData eventData) {

        //从templateHolder中获取TableTemplate
        TableTemplate table = templateHolder.getTable(tableName);

        // 说明不是我们需要的表
        if (null == table) {
            log.warn("table {} not found", tableName);
            return null;
        }

        List<Map<String, String>> afterMapList = new ArrayList<>();

        for (Serializable[] after : getAfterValues(eventData)) {

            Map<String, String> afterMap = new HashMap<>(16);

            int colLen = after.length;

            for (int ix = 0; ix < colLen; ix++) {

                // 取出当前的位置对应的列名
                String colName = table.getPositionMap().get(ix);

                // 如果没有，则说明不关心这个列
                if (StringUtils.isEmpty(colName)) {
                    log.debug("ignore position : {}", ix);
                    // 跳出此次循环，直接进入下一次循环
                    continue;
                }

                String colValue = after[ix].toString();
                // 字段名 -> 字段对应的值
                afterMap.put(colName, colValue);
            }
            afterMapList.add(afterMap);

        }

        // 返回BinlogRowData对象
        BinlogRowData rowData = new BinlogRowData();
        rowData.setAfter(afterMapList);
        rowData.setTable(table);

        return rowData;
    }
}
