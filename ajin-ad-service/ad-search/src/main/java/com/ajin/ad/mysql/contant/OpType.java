package com.ajin.ad.mysql.contant;


import com.github.shyiko.mysql.binlog.event.EventType;

/**
 * @Author: ajin
 * @Date: 2019/4/16 17:54
 */
@SuppressWarnings("all")
public enum OpType {

    ADD,
    UPDATE,
    DELETE,
    OTHER;

    /**
     * 将EventType转化成OpType
     */
    public static OpType to(EventType eventType) {

        switch (eventType) {
            case EXT_WRITE_ROWS:
                return ADD;

            case EXT_UPDATE_ROWS:
                return UPDATE;

            case EXT_DELETE_ROWS:
                return DELETE;

            default:
                return OTHER;
        }
    }
}
