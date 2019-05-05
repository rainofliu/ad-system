package com.ajin.ad.sender.kafka;

import com.ajin.ad.mysql.dto.MysqlRowData;
import com.ajin.ad.sender.ISender;
import com.alibaba.fastjson.JSON;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @Author: ajin
 * @Date: 2019/4/24 20:08
 * kafka来投递增量索引给子系统，比如数据分析子系统，日志系统
 */
@Component("kafkaSender")
public class KafkaSender implements ISender {

    @Value("${adconf.kafka.topic}")
    private String topic;


    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public KafkaSender(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void send(MysqlRowData rowData) {

        kafkaTemplate.send(topic,
                JSON.toJSONString(rowData));
    }

    /**
     * 监听Topic
     * groupId代表谁能接收到消息
     */
    @KafkaListener(topics = {"ad-search-mysql-data"}, groupId = "ad-search")
    public void processMysqlRowData(ConsumerRecord<?, ?> record) {

        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            // 获取kafka中的消息
            Object message = kafkaMessage.get();
            MysqlRowData rowData = JSON.parseObject(
                    message.toString(), MysqlRowData.class);
            System.out.println("kafka processMysqlRowData :" +
                    JSON.toJSONString(rowData));
        }
    }
}
