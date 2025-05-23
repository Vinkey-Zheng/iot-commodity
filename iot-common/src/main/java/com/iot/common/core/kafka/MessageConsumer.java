package com.iot.common.core.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;

public class MessageConsumer {
    private final KafkaConsumer<String, String> consumer;
    private static final Logger logger = LoggerFactory.getLogger(MessageConsumer.class);

    public MessageConsumer(KafkaConsumer<String, String> consumer){
        this.consumer = consumer;
        this.consumer.subscribe(Collections.singletonList("topic"));
        // Collections.singletonList("topic") 用于创建一个只包含指定元素 "topic" 的不可变列表。
        // 此处用于订阅单个 Kafka 主题。
    }

    public  void consumeMessage(){
        while (true){
            var records = consumer.poll(Duration.ofMillis(1000));
            for(ConsumerRecord<String, String> record: records){
                processMessage(record.value());
                System.out.println("Consumed message: " + record.value());
            }
        }
    }

    private void processMessage(String message){
        // 处理消息逻辑
        logger.info("Processing message : {}", message);
    }
}
