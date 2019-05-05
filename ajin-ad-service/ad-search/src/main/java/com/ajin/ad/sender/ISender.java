package com.ajin.ad.sender;

import com.ajin.ad.mysql.dto.MysqlRowData;

/**
 * 投递增量数据的接口
 */
@SuppressWarnings("all")
public interface ISender {

    void send(MysqlRowData rowData);
}
