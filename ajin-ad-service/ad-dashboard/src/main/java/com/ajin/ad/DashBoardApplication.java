package com.ajin.ad;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

/**
 * @Author: ajin
 * @Date: 2019/4/26 14:28
 */
@EnableEurekaClient
@SpringBootApplication
@EnableHystrixDashboard
public class DashBoardApplication {

    public static void main(String[] args) {

        SpringApplication.run(DashBoardApplication.class, args);
    }
}
