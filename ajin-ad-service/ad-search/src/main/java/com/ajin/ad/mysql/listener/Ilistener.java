package com.ajin.ad.mysql.listener;

import com.ajin.ad.mysql.dto.BinlogRowData;

/**
 * @Author: ajin
 * @Date: 2019/4/24 12:19
 *
 * 事件监听器接口,可以自定义实现不同的监听器
 */
public interface Ilistener {

    void register();

    void onEvent(BinlogRowData eventData);
}
