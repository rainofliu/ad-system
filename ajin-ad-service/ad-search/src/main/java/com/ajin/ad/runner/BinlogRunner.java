package com.ajin.ad.runner;

import com.ajin.ad.mysql.BinlogClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Spring Boot程序启动的时候执行对MySQL的连接与监听
 */
@Slf4j
@Component
public class BinlogRunner implements CommandLineRunner {

    private final BinlogClient client;

    @Autowired
    public BinlogRunner(BinlogClient client) {
        this.client = client;
    }

    /**
     * Spring Boot启动的时候，会执行run方法
     * */
    @Override
    public void run(String... args) throws Exception {

        log.info("Coming in BinlogRunner");
        client.connect();
    }
}
