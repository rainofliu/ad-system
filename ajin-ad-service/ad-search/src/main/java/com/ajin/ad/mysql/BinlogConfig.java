package com.ajin.ad.mysql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: ajin
 * @Date: 2019/4/24 17:36
 */
@Component
@ConfigurationProperties(prefix = "adconf.mysql")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BinlogConfig {

    private String host;

    private Integer port;

    private String username;

    private String password;

    private String binlogName;

    private Long position;


}
