package com.tdl.seckill.consumer.service;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Description.
 *
 * @author : CeaserWang
 * @version : 1.0
 * @since : 2018/10/27 21:22
 */
public class ConsumerGroup {
    private static Logger LOGGER = LoggerFactory.getLogger(ConsumerGroup.class);
    /**
     * 线程池
     */
    private ExecutorService threadPool;

    private List<ConsumerTask> consumers ;

    private static final ThreadFactory namedThradFactory = new ThreadFactoryBuilder().setNameFormat("pool-handler-%d").build();

    public ConsumerGroup(int threadNum, String groupId, String topic, String brokerList) {
        LOGGER.info("kafka parameter={},{},{},{}",threadNum,groupId,topic,brokerList);
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("pool-handler-%d").build();

        threadPool = new ThreadPoolExecutor(threadNum,threadNum,0L,
                TimeUnit.MILLISECONDS,new LinkedBlockingDeque<Runnable>(1024),
                namedThradFactory,new ThreadPoolExecutor.AbortPolicy());


        consumers = new ArrayList<ConsumerTask>(threadNum);
        for (int i = 0; i < threadNum; i++) {
            ConsumerTask consumerThread = new ConsumerTask(brokerList, groupId, topic);
            consumers.add(consumerThread);
        }
    }

}