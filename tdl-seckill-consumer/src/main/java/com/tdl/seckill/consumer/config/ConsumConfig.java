package com.tdl.seckill.consumer.config;

import com.alibaba.fastjson.JSONObject;
import com.tdl.seckill.consumer.service.ConsumerGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Description.
 *consumer.broker.list
 * @author : CeaserWang
 * @version : 1.0
 * @since : 2018/10/29 21:09
 */

@Configuration
public class ConsumConfig {
    static{
        System.out.println();
    }
    private final static Logger LOGGER = LoggerFactory.getLogger(ConsumConfig.class);
    @Value("${consumer.broker.list}")
    private String brokerList;

    @Value("${consumer.group.id}")
    private String groupId;

    @Value("${consumer.topic}")
    private String topic;

    @Value("${consumer.thread}")
    private int threadNum;


    @Bean
    public ConsumerGroup createConsumerGroup() {
        ConsumerGroup consumerGroup = new ConsumerGroup(threadNum, groupId, topic, brokerList);
        LOGGER.info("ConsumerGroup 初始化成功");
        return consumerGroup;
    }

}
