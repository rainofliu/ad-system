package com.ajin.ad.mysql;

import com.ajin.ad.mysql.listener.AggregationListener;
import com.github.shyiko.mysql.binlog.BinaryLogClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Author: ajin
 * @Date: 2019/4/24 17:39
 */
@Slf4j
@Component
public class BinlogClient {

    private BinaryLogClient client;

    private final BinlogConfig config;
    private final AggregationListener listener;

    @Autowired
    public BinlogClient(BinlogConfig config, AggregationListener listener) {
        this.config = config;
        this.listener = listener;
    }

    /**
     * 监听binlog
     */
    @SuppressWarnings("all")
    public void connect() {
        /**
         * 开启一个新的线程来监听
         * */
        new Thread(() -> {
            client = new BinaryLogClient(
                    config.getHost(),
                    config.getPort(),
                    config.getUsername(),
                    config.getPassword()
            );

            if (!StringUtils.isEmpty(config.getBinlogName()) &&
                    !config.getPosition().equals(-1)) {

                client.setBinlogFilename(config.getBinlogName());
                client.setBinlogPosition(config.getPosition());
            }
            /**
             * 设置监听器
             * */
            client.registerEventListener(listener);

            try {
                log.info("connecting to mysql start");
                // 伪装成slave连接MySQL
                client.connect();
                log.info("connected to mysql done");
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }).start();
    }

    /**
     * 与MySQL断开连接
     */
    public void close() {
        try {
            client.disconnect();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
