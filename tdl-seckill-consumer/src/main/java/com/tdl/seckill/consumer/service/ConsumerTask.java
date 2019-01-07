package com.tdl.seckill.consumer.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.tdl.seckill.api.SeckillOrderService;
import com.tdl.seckill.biz.SeckillOrderBiz;
import com.tdl.seckill.bo.SeckillGoodsBO;
import com.tdl.seckill.consumer.util.SpringContextUtil;
import com.tdl.seckill.dos.SeckillGoodsDo;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

/**
 * Description.
 *
 * @author : CeaserWang
 * @version : 1.0
 * @since : 2018/10/29 21:12
 */
public class ConsumerTask implements Runnable {
    private static Logger LOGGER = LoggerFactory.getLogger(ConsumerTask.class);


    private SeckillOrderBiz seckillOrderBiz;

    private SpringContextUtil springContextUtil;

    /**
     * 每个线程维护KafkaConsumer实例
     */
    private final KafkaConsumer<String, String> consumer;

    public ConsumerTask(String brokerList, String groupId, String topic){
        this.seckillOrderBiz = springContextUtil.getBean(SeckillOrderBiz.class) ;

        Properties props = new Properties();
        props.put("bootstrap.servers", brokerList);
        props.put("group.id", groupId);
        //自动提交位移
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        this.consumer = new KafkaConsumer<String, String>(props);
        consumer.subscribe(Arrays.asList(topic));
    }

    @Override
    public void run() {
        boolean flag = true;
        while (flag) {
            // 使用200ms作为获取超时时间
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(200));
            for (ConsumerRecord<String, String> record : records) {
                // 简单地打印消息
                LOGGER.info("threadId : {},  recordValue {}， consumed partition {}， message with offset：{}", Thread.currentThread().getId(),  record.value() , record.partition() ,  record.offset());
                dealMessage(record.value()) ;
            }
        }

    }

    private void dealMessage(String value) {
        SeckillGoodsBO stock = JSON.parseObject(value, new TypeReference<SeckillGoodsBO>() {});
        LOGGER.info("consumer stock={}",JSON.toJSONString(stock));
        SeckillGoodsDo seckillGoodsDo = new SeckillGoodsDo();
        BeanUtils.copyProperties(stock,seckillGoodsDo);

        //创建订单
        seckillOrderBiz.createOrder(seckillGoodsDo,stock.getUserId());
    }
}
